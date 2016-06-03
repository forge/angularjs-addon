/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.angularjs;

import org.jboss.forge.addon.projects.facets.WebResourcesFacet;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.scaffold.util.ScaffoldUtil;

/**
 * A {@link ProcessingStrategy} to copy the contents of the {@link ScaffoldResource} from it's source on the classpath
 * to it's destination in the project's web resources.
 */
public class CopyResourcesStrategy implements ProcessingStrategy
{

   private final WebResourcesFacet web;

   public CopyResourcesStrategy(WebResourcesFacet web)
   {
      this.web = web;
   }

   @Override
   public Resource<?> execute(ScaffoldResource scaffoldResource)
   {
      return ScaffoldUtil.createOrOverwrite(web.getWebResource(scaffoldResource.getDestination()), getClass()
               .getResourceAsStream(scaffoldResource.getSource()));
   }
}
