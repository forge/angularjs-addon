/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.angularjs;

import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.WebResourcesFacet;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.templates.Template;
import org.jboss.forge.addon.templates.TemplateProcessor;
import org.jboss.forge.addon.templates.TemplateProcessorFactory;
import org.jboss.forge.addon.templates.facets.TemplateFacet;
import org.jboss.forge.addon.templates.freemarker.FreemarkerTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.jboss.forge.addon.angularjs.AngularScaffoldProvider.SCAFFOLD_DIR;

/**
 * A strategy for generating the search page with included files.
 */
public class SearchTemplateStrategy implements ProcessingStrategy
{
   private static final String SEARCH_FORM_INPUT = "/views/includes/searchFormInput.html.ftl";
   private static final String SEARCH_RESULTS = "/views/includes/searchResults.html.ftl";
   private static final String SEARCH_RESULTS_PAGINATOR = "/views/includes/searchResultsPaginator.html.ftl";

   private final WebResourcesFacet web;

   private Project project;

   private Map<String, Object> dataModel;

   private ResourceFactory resourceFactory;

   private TemplateProcessorFactory templateProcessorFactory;

   private final boolean overwrite;

   public SearchTemplateStrategy(WebResourcesFacet web, ResourceFactory resourceFactory, Project project,
            TemplateProcessorFactory templateProcessorFactory, Map<String, Object> dataModel, boolean overwrite)
   {
      this.web = web;
      this.resourceFactory = resourceFactory;
      this.project = project;
      this.templateProcessorFactory = templateProcessorFactory;
      this.dataModel = dataModel;
      this.overwrite = overwrite;
   }

   @Override
   public Resource execute(ScaffoldResource scaffoldResource)
   {
      List<Map<String, String>> properties = (List<Map<String, String>>) dataModel.get("properties");
      StringBuilder searchFormProperties = new StringBuilder();
      for (Map<String, String> property : properties)
      {
         dataModel.put("property", property);
         Include includeFile = new Include(SEARCH_FORM_INPUT);
         String output = includeFile.processInclude(dataModel);
         searchFormProperties.append(output).append('\n');
      }
      Include searchResultsInclude = new Include(SEARCH_RESULTS);
      Include searchResultsPaginatorInclude = new Include(SEARCH_RESULTS_PAGINATOR);
      String searchResults = searchResultsInclude.processInclude(dataModel);
      String searchResultsPaginator = searchResultsPaginatorInclude.processInclude(dataModel);
      dataModel.put("searchFormProperties", searchFormProperties.toString());
      dataModel.put("searchResults", searchResults);
      dataModel.put("searchResultsPaginator", searchResultsPaginator);
      ProcessTemplateStrategy strategy = new ProcessTemplateStrategy(web, resourceFactory, project,
               templateProcessorFactory, dataModel, overwrite);
      return strategy.execute(scaffoldResource);
   }

   class Include
   {
      private String source;

      Include(String source)
      {
         this.source = source;
      }

      private String processInclude(Map<String, Object> dataModel)
      {
         Resource<?> resource = resourceFactory.create(getClass().getResource(
                  SCAFFOLD_DIR + source));
         if (project.hasFacet(TemplateFacet.class))
         {
            TemplateFacet templates = project.getFacet(TemplateFacet.class);
            Resource<?> templateResource = templates.getResource(source);
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
         return output;
      }
   }
}
