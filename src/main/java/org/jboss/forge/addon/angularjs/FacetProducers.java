/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.angularjs;

import javax.enterprise.inject.Produces;

import org.jboss.forge.addon.ui.cdi.CommandScoped;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.facets.MetadataFacet;
import org.jboss.forge.addon.projects.facets.WebResourcesFacet;

/**
 * A utlity class containing CDI producers for Forge project facets.
 */
public class FacetProducers
{

   @Produces
   @CommandScoped
   public MetadataFacet producesMetaData(Project project)
   {
      return project.getFacet(MetadataFacet.class);
   }

   @Produces
   @CommandScoped
   public WebResourcesFacet producesWebResource(Project project)
   {
      return project.getFacet(WebResourcesFacet.class);
   }

}
