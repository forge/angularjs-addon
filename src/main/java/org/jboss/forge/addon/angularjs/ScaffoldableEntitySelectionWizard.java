/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.angularjs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.jboss.forge.addon.angularjs.util.RestResourceTypeVisitor;
import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.javaee.jpa.JPAFacet;
import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
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
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.input.UISelectMany;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.NavigationResult;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.addon.ui.wizard.UIWizardStep;
import org.jboss.forge.roaster.model.Annotation;
import org.jboss.forge.roaster.model.JavaClass;
import org.jboss.forge.roaster.model.Member;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.util.Strings;

public class ScaffoldableEntitySelectionWizard implements UIWizardStep
{

   @Inject
   @WithAttributes(label = "Targets", required = true)
   private UISelectMany<JavaClassSource> targets;

   @Inject
   @WithAttributes(label = "Generate REST resources", required = false, description = "If enabled, REST resources for the selected JPA entities will be generated")
   private UIInput<Boolean> generateRestResources;

   @Inject
   private ProjectFactory projectFactory;

   @Inject
   private Inflector inflector;

   @Override
   public NavigationResult next(UINavigationContext context) throws Exception
   {
      UIContext uiContext = context.getUIContext();
      Map<Object, Object> attributeMap = uiContext.getAttributeMap();
      ResourceCollection resourceCollection = new ResourceCollection();
      if (targets.getValue() != null)
      {
         for (JavaClassSource klass : targets.getValue())
         {
            Project project = getSelectedProject(uiContext);
            JavaSourceFacet javaSource = project.getFacet(JavaSourceFacet.class);
            Resource<?> resource = javaSource.getJavaResource(klass);
            if (resource != null)
            {
               resourceCollection.addToCollection(resource);
            }
         }
      }

      attributeMap.put(ResourceCollection.class, resourceCollection);
      Boolean shouldGenerateRestResources = generateRestResources.getValue();
      if (shouldGenerateRestResources.equals(Boolean.TRUE))
      {
         return Results.navigateTo(JSONRestResourceFromEntityCommand.class);
      }
      return null;
   }

   @Override
   public UICommandMetadata getMetadata(UIContext context)
   {
      return Metadata.forCommand(getClass()).name("Select JPA entities")
               .description("Select the JPA entities to be used for scaffolding.");
   }

   @Override
   public boolean isEnabled(UIContext context)
   {
      return true;
   }

   @Override
   public void initializeUI(UIBuilder builder) throws Exception
   {
      UIContext uiContext = builder.getUIContext();
      Project project = getSelectedProject(uiContext);

      JPAFacet<?> persistenceFacet = project.getFacet(JPAFacet.class);
      List<JavaClassSource> allEntities = persistenceFacet.getAllEntities();
      List<JavaClassSource> supportedEntities = new ArrayList<>();
      for (JavaClassSource entity: allEntities)
      {
         for (Member<?> member : entity.getMembers())
         {
            // FORGE-823 Only add entities with @Id as valid entities for REST resource generation.
            // Composite keys are not yet supported.
            if (member.hasAnnotation(Id.class))
            {
               supportedEntities.add(entity);
            }
         }
      }
      targets.setValueChoices(supportedEntities);
      targets.setItemLabelConverter(new Converter<JavaClassSource, String>()
      {
         @Override
         public String convert(JavaClassSource source)
         {
            return source == null ? null : source.getQualifiedName();
         }
      });
      builder.add(targets).add(generateRestResources);
   }

   @Override
   public Result execute(UIExecutionContext context) throws Exception
   {
      return null;
   }

   @Override
   public void validate(UIValidationContext context)
   {
      // Verify if the selected JPA entities have corresponding JAX-RS resources.
      // If yes, then raise warnings if they will be overwritten
      Boolean shouldGenerateRestResources = generateRestResources.getValue();
      List<String> entitiesWithRestResources = new ArrayList<>();
      if (shouldGenerateRestResources.equals(Boolean.TRUE) && targets.getValue() != null)
      {
         for (JavaClassSource klass : targets.getValue())
         {
            Project project = getSelectedProject(context.getUIContext());
            JavaSourceFacet javaSource = project.getFacet(JavaSourceFacet.class);
            RestResourceTypeVisitor restTypeVisitor = new RestResourceTypeVisitor();
            String entityTable = getEntityTable(klass);
            String proposedResourcePath = "/" + inflector.pluralize(entityTable.toLowerCase());
            restTypeVisitor.setProposedPath(proposedResourcePath);
            javaSource.visitJavaSources(restTypeVisitor);
            if (restTypeVisitor.isFound())
            {
               entitiesWithRestResources.add(klass.getQualifiedName());
            }
         }
      }
      if (!entitiesWithRestResources.isEmpty())
      {
         context.addValidationWarning(targets, "Some of the selected entities " + entitiesWithRestResources.toString()
                  + " already have associated REST resources that will be overwritten.");
      }
   }

   public static String getEntityTable(final JavaClass<?> entity)
   {
      String table = entity.getName();
      if (entity.hasAnnotation(Entity.class))
      {
         Annotation<?> a = entity.getAnnotation(Entity.class);
         if (!Strings.isNullOrEmpty(a.getStringValue("name")))
         {
            table = a.getStringValue("name");
         }
         else if (!Strings.isNullOrEmpty(a.getStringValue()))
         {
            table = a.getStringValue();
         }
      }
      return table;
   }

   private Project getSelectedProject(UIContext uiContext)
   {
      return Projects.getSelectedProject(projectFactory, uiContext);
   }

}
