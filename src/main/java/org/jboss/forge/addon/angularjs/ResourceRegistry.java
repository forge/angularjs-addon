/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.angularjs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.ui.cdi.CommandScoped;

/**
 * A registry that serves as a store for resources that have been generated. This is mostly used to store state about
 * generated resources across CDI events fired during the scaffolding lifecycle, since observers cannot provide this
 * information through return values.
 */
@CommandScoped
public class ResourceRegistry
{

   private List<Resource<?>> resources;

   @Inject
   public ResourceRegistry()
   {
      this.resources = new ArrayList<>();
   }

   /**
    * Returns the state of the registry.
    * 
    * @return The collection of resources in the registry.
    */
   public List<? extends Resource<?>> getCreatedResources()
   {
      return resources;
   }

   /**
    * Add a resource to the registry.
    * 
    * @param resource The resource to be added.
    */
   public void add(Resource<?> resource)
   {
      resources.add(resource);
   }

   /**
    * Adds a collection of resources to the registry.
    * 
    * @param collection The resources to be added.
    */
   public void addAll(Collection<Resource<?>> collection)
   {
      resources.addAll(collection);
   }

   /**
    * Clears the state of the registry. This is usually invoked on all firings of CDI events so that the registry
    * contains only the generated resources for that event and not the prior ones.
    */
   public void clear()
   {
      resources.clear();
   }

}
