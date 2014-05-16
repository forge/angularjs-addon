/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.angularjs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.javaee.jpa.JPAFacet;
import org.jboss.forge.addon.javaee.rest.generation.RestGenerationConstants;
import org.jboss.forge.addon.javaee.rest.generation.RestGenerationContext;
import org.jboss.forge.addon.javaee.rest.generation.RestResourceGenerator;
import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.parser.java.resources.JavaResource;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.Projects;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.scaffold.spi.ResourceCollection;
import org.jboss.forge.addon.text.Inflector;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.context.UINavigationContext;
import org.jboss.forge.addon.ui.context.UIValidationContext;
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.NavigationResult;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.addon.ui.wizard.UIWizardStep;
import org.jboss.forge.furnace.services.Imported;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.shrinkwrap.descriptor.api.persistence.PersistenceCommonDescriptor;
import org.jboss.shrinkwrap.descriptor.api.persistence.PersistenceUnitCommon;

/**
 * A wizard step to perform creation of REST resources.
 * It re-uses services from the Java EE addon to create JAX-RS based REST resources.
 */
public class JSONRestResourceFromEntityCommand implements UIWizardStep
{

   @Inject
   @WithAttributes(label = "Generator", required = true)
   private UISelectOne<RestResourceGenerator> generator;

   @Inject
   @WithAttributes(label = "Persistence Unit", required = true)
   private UISelectOne<String> persistenceUnit;

   @Inject
   @WithAttributes(label = "Target Package Name", required = true, type = InputType.JAVA_PACKAGE_PICKER)
   private UIInput<String> packageName;

   @Inject
   private Imported<RestResourceGenerator> resourceGenerators;

   @Inject
   private Inflector inflector;

   @Inject
   private ProjectFactory projectFactory;

   private List<JavaClassSource> targets;

   @Override
   public UICommandMetadata getMetadata(UIContext context)
   {
      return Metadata.forCommand(getClass()).name("REST: Generate Endpoints From Entities")
               .description("Generate REST endpoints from JPA entities");
   }

   @Override
   public boolean isEnabled(UIContext context)
   {
      return true;
   }

   @SuppressWarnings({ "rawtypes", "unchecked" })
   @Override
   public void initializeUI(UIBuilder builder) throws Exception
   {
      UIContext context = builder.getUIContext();
      Project project = getSelectedProject(context);
      JPAFacet<PersistenceCommonDescriptor> persistenceFacet = project.getFacet(JPAFacet.class);
      JavaSourceFacet javaSourceFacet = project.getFacet(JavaSourceFacet.class);
      List<String> persistenceUnits = new ArrayList<>();
      List<PersistenceUnitCommon> allUnits = persistenceFacet.getConfig().getAllPersistenceUnit();
      for (PersistenceUnitCommon persistenceUnit : allUnits)
      {
         persistenceUnits.add(persistenceUnit.getName());
      }
      if (!persistenceUnits.isEmpty())
      {
         persistenceUnit.setValueChoices(persistenceUnits).setDefaultValue(persistenceUnits.get(0));
      }

      // TODO: May detect where @Path resources are located
      packageName.setDefaultValue(javaSourceFacet.getBasePackage() + ".rest");

      RestResourceGenerator defaultResourceGenerator = null;
      for (RestResourceGenerator generator : resourceGenerators)
      {
         if (generator.getName().equals(RestGenerationConstants.JPA_ENTITY))
         {
            defaultResourceGenerator = generator;
         }
      }
      generator.setDefaultValue(defaultResourceGenerator);
      if (context.getProvider().isGUI())
      {
         generator.setItemLabelConverter(new Converter<RestResourceGenerator, String>()
         {
            @Override
            public String convert(RestResourceGenerator source)
            {
               return source == null ? null : source.getDescription();
            }
         });
      }
      else
      {
         generator.setItemLabelConverter(new Converter<RestResourceGenerator, String>()
         {
            @Override
            public String convert(RestResourceGenerator source)
            {
               return source == null ? null : source.getName();
            }
         });
      }
      builder.add(generator)
               .add(packageName)
               .add(persistenceUnit);
   }

   @Override
   public Result execute(final UIExecutionContext context) throws Exception
   {
      UIContext uiContext = context.getUIContext();
      ResourceCollection resourceCollection = (ResourceCollection) uiContext.getAttributeMap().get(
               ResourceCollection.class);

      targets = new ArrayList<>();
      for (Resource<?> resource : resourceCollection.getResources())
      {
         JavaResource javaResource = (JavaResource) resource;
         JavaClassSource javaClass = javaResource.getJavaType();
         targets.add(javaClass);
      }

      RestGenerationContext generationContext = createContextFor(uiContext);
      Set<JavaClassSource> endpoints = generateEndpoints(generationContext);
      Project project = generationContext.getProject();
      JavaSourceFacet javaSourceFacet = project.getFacet(JavaSourceFacet.class);
      List<JavaResource> selection = new ArrayList<>();

      for (JavaClassSource javaClass : endpoints)
      {
         selection.add(javaSourceFacet.saveJavaSource(javaClass));
      }
      uiContext.setSelection(selection);
      return Results.success("Endpoint created");
   }

   private Set<JavaClassSource> generateEndpoints(RestGenerationContext generationContext) throws Exception
   {
      RestResourceGenerator selectedGenerator = generator.getValue();
      Set<JavaClassSource> classes = new HashSet<>();
      for (JavaClassSource target : targets)
      {
         generationContext.setEntity(target);
         List<JavaClassSource> artifacts = selectedGenerator.generateFrom(generationContext);
         classes.addAll(artifacts);
      }
      return classes;
   }

   private RestGenerationContext createContextFor(final UIContext context)
   {
      RestGenerationContext generationContext = new RestGenerationContext();
      generationContext.setProject(getSelectedProject(context));
      generationContext.setContentType(MediaType.APPLICATION_JSON);
      generationContext.setPersistenceUnitName(persistenceUnit.getValue());
      generationContext.setTargetPackageName(packageName.getValue());
      generationContext.setInflector(inflector);
      return generationContext;
   }

   @Override
   public NavigationResult next(UINavigationContext context) throws Exception
   {
      return null;
   }

   @Override
   public void validate(UIValidationContext context)
   {
      // Do nothing
   }

   private Project getSelectedProject(UIContext context)
   {
      return Projects.getSelectedProject(projectFactory, context);
   }
}
