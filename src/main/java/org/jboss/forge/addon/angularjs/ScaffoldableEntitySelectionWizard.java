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
import javax.persistence.Id;

import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.javaee.jpa.JPAFacet;
import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.Projects;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.scaffold.spi.ResourceCollection;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.context.UINavigationContext;
import org.jboss.forge.addon.ui.context.UIValidationContext;
import org.jboss.forge.addon.ui.input.UISelectMany;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.NavigationResult;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.addon.ui.wizard.UIWizardStep;
import org.jboss.forge.roaster.model.Member;
import org.jboss.forge.roaster.model.source.JavaClassSource;

public class ScaffoldableEntitySelectionWizard implements UIWizardStep
{

   @Inject
   @WithAttributes(label = "Targets", required = true)
   private UISelectMany<JavaClassSource> targets;

   @Inject
   private ProjectFactory projectFactory;

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
      // ScaffoldGenerationContext genCtx = (ScaffoldGenerationContext)
      // attributeMap.get(ScaffoldGenerationContext.class);
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
      builder.add(targets);
   }

   @Override
   public Result execute(UIExecutionContext context) throws Exception
   {
      return null;
   }

   @Override
   public void validate(UIValidationContext context)
   {
      // Do nothing
   }

   private Project getSelectedProject(UIContext uiContext)
   {
      return Projects.getSelectedProject(projectFactory, uiContext);
   }

}
