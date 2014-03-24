package org.jboss.forge.addon.angularjs;

import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.WebResourcesFacet;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.scaffold.util.ScaffoldUtil;
import org.jboss.forge.addon.templates.Template;
import org.jboss.forge.addon.templates.TemplateProcessor;
import org.jboss.forge.addon.templates.TemplateProcessorFactory;
import org.jboss.forge.addon.templates.facets.TemplateFacet;
import org.jboss.forge.addon.templates.freemarker.FreemarkerTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.jboss.forge.addon.angularjs.AngularScaffoldProvider.SCAFFOLD_DIR;

/**
 * Created by vineet on 3/25/14.
 */
public class ProcessTemplateCommand
{
   private final Project project;
   private final ResourceFactory resourceFactory;
   private final TemplateProcessorFactory templateProcessorFactory;

   public ProcessTemplateCommand(Project project, ResourceFactory resourceFactory,
            TemplateProcessorFactory templateProcessorFactory)
   {
      this.project = project;
      this.resourceFactory = resourceFactory;
      this.templateProcessorFactory = templateProcessorFactory;
   }

   public List<Resource<?>> execute(List<ScaffoldResource> scaffoldResources,
            Map<String, Object> dataModel, boolean overwrite)
   {
      List<Resource<?>> resources = new ArrayList<>();

      for (ScaffoldResource scaffoldResource : scaffoldResources)
      {
         WebResourcesFacet web = project.getFacet(WebResourcesFacet.class);

         Resource<?> resource = resourceFactory.create(getClass().getResource(
                  SCAFFOLD_DIR + scaffoldResource.getSource()));
         if (project.hasFacet(TemplateFacet.class))
         {
            TemplateFacet templates = project.getFacet(TemplateFacet.class);
            Resource<?> templateResource = templates.getResource(scaffoldResource.getSource());
            if (templateResource.exists())
            {
               resource = templateResource;
            }
         }

         Template template = new FreemarkerTemplate(resource);
         TemplateProcessor templateProcessor = templateProcessorFactory.fromTemplate(template);
         String output = null;
         try
         {
            output = templateProcessor.process(dataModel);
         }
         catch (IOException ioEx)
         {
            throw new IllegalStateException(ioEx);
         }
         resources.add(ScaffoldUtil.createOrOverwrite(web.getWebResource(scaffoldResource.getDestination()),
                  output, overwrite));
      }
      return resources;
   }
}
