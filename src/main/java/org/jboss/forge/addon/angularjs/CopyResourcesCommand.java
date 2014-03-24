package org.jboss.forge.addon.angularjs;

import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.WebResourcesFacet;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.scaffold.util.ScaffoldUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vineet on 3/25/14.
 */
public class CopyResourcesCommand
{

   private Project project;

   public CopyResourcesCommand(Project project)
   {
      this.project = project;;
   }

   public List<Resource<?>> execute(List<ScaffoldResource> scaffoldResources, boolean overwrite)
   {
      List<Resource<?>> resources = new ArrayList<>();
      for (ScaffoldResource scaffoldResource : scaffoldResources)
      {
         WebResourcesFacet web = project.getFacet(WebResourcesFacet.class);
         resources.add(ScaffoldUtil.createOrOverwrite(web.getWebResource(scaffoldResource.getDestination()), getClass()
                  .getResourceAsStream(scaffoldResource.getSource()), overwrite));
      }
      return resources;
   }
}
