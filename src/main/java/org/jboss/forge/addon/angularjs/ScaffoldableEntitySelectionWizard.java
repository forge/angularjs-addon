package org.jboss.forge.addon.angularjs;

import java.util.Map;

import javax.inject.Inject;

import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.javaee.jpa.JPAFacet;
import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.Projects;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.scaffold.spi.ResourceCollection;
import org.jboss.forge.addon.scaffold.spi.ScaffoldGenerationContext;
import org.jboss.forge.addon.ui.context.*;
import org.jboss.forge.addon.ui.input.UISelectMany;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.NavigationResult;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.addon.ui.wizard.UIWizardStep;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.shrinkwrap.descriptor.api.persistence.PersistenceCommonDescriptor;

public class ScaffoldableEntitySelectionWizard implements UIWizardStep
{

   @Inject
   @WithAttributes(label = "Targets", required = true)
   private UISelectMany<JavaClass> targets;

   @Inject
   private ResourceFactory resourceFactory;

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
         for (JavaClass klass : targets.getValue())
         {
             Project project = getSelectedProject(uiContext);
             JavaSourceFacet javaSource = project.getFacet(JavaSourceFacet.class);
             Resource resource = javaSource.getJavaResource(klass);
             if (resource != null)
             {
                resourceCollection.addToCollection(resource);
             }
         }
      }

      attributeMap.put(ResourceCollection.class, resourceCollection);
      ScaffoldGenerationContext genCtx = (ScaffoldGenerationContext) attributeMap.get(ScaffoldGenerationContext.class);
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

      JPAFacet<PersistenceCommonDescriptor> persistenceFacet = project.getFacet(JPAFacet.class);
      targets.setValueChoices(persistenceFacet.getAllEntities());
      targets.setItemLabelConverter(new Converter<JavaClass, String>()
      {
         @Override
         public String convert(JavaClass source)
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
