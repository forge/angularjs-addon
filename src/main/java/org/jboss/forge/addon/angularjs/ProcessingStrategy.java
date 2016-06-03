/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.angularjs;

import org.jboss.forge.addon.resource.Resource;

/**
 * The base class of a strategy pattern used to implement how {@link ScaffoldResource}s should be processed.
 */
public interface ProcessingStrategy
{
   public Resource<?> execute(ScaffoldResource scaffoldResource);
}
