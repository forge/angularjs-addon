/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.angularjs;

import static org.jboss.forge.addon.angularjs.AngularScaffoldProvider.SCAFFOLD_DIR;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.WebResourcesFacet;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.templates.Template;
import org.jboss.forge.addon.templates.TemplateFactory;
import org.jboss.forge.addon.templates.facets.TemplateFacet;
import org.jboss.forge.addon.templates.freemarker.FreemarkerTemplate;

/**
 * A strategy for generating the detail page with included files.
 */
public class DetailTemplateStrategy implements ProcessingStrategy
{
   private static final String BASIC_PROPERTY_DETAIL = "/views/includes/basicPropertyDetail.html.ftl";
   private static final String LOOKUP_PROPERTY_DETAIL = "/views/includes/lookupPropertyDetail.html.ftl";
   private static final String N_TO_MANY_PROPERTY_DETAIL = "/views/includes/nToManyPropertyDetail.html.ftl";
   private static final String N_TO_ONE_PROPERTY_DETAIL = "/views/includes/nToOnePropertyDetail.html.ftl";

   private final WebResourcesFacet web;

   private final Project project;

   private final Map<String, Object> dataModel;

   private final ResourceFactory resourceFactory;

   private final TemplateFactory templateFactory;

   private final boolean overwrite;

   public DetailTemplateStrategy(WebResourcesFacet web, ResourceFactory resourceFactory, Project project,
            TemplateFactory templateFactory, Map<String, Object> dataModel, boolean overwrite)
   {
      this.web = web;
      this.resourceFactory = resourceFactory;
      this.project = project;
      this.templateFactory = templateFactory;
      this.dataModel = dataModel;
      this.overwrite = overwrite;
   }

   @Override
   public Resource<?> execute(ScaffoldResource scaffoldResource)
   {
      @SuppressWarnings("unchecked")
      List<Map<String, String>> properties = (List<Map<String, String>>) dataModel.get("properties");
      StringBuilder formProperties = new StringBuilder();
      for (Map<String, String> property : properties)
      {
         dataModel.put("property", property);
         Include includeFile;
         if ("true".equals(property.get("many-to-one")) || "true".equals(property.get("one-to-one")))
         {
            includeFile = new Include(N_TO_ONE_PROPERTY_DETAIL);
         }
         else if ("true".equals(property.get("n-to-many")))
         {
            includeFile = new Include(N_TO_MANY_PROPERTY_DETAIL);
         }
         else if (property.containsKey("lookup"))
         {
            includeFile = new Include(LOOKUP_PROPERTY_DETAIL);
         }
         else
         {
            includeFile = new Include(BASIC_PROPERTY_DETAIL);
         }
         String output = includeFile.processInclude(dataModel);
         formProperties.append(output).append('\n');
      }
      dataModel.put("formProperties", formProperties.toString());
      ProcessTemplateStrategy strategy = new ProcessTemplateStrategy(web, resourceFactory, project,
               templateFactory, dataModel, overwrite);
      return strategy.execute(scaffoldResource);
   }

   class Include
   {
      private final String source;

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

         Template template = templateFactory.create(resource, FreemarkerTemplate.class);
         String output = null;
         try
         {
            output = template.process(dataModel);
         }
         catch (IOException ioEx)
         {
            throw new IllegalStateException(ioEx);
         }
         return output;
      }
   }
}