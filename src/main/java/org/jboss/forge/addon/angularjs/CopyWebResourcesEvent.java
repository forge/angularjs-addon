/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.angularjs;

import org.jboss.forge.addon.projects.Project;

import java.util.List;

/**
 * A CDI {@link javax.enterprise.event.Event} that is fired whenever {@link ScaffoldResource}s are to be copied to certain locations, at various points
 * in the scaffold generation lifecycle.
 * 
 */
public class CopyWebResourcesEvent {

    private Project project;
    private List<ScaffoldResource> resources;
    private boolean overwrite;

    /**
     * 
     * @param resources The list of static resources that are to be copied, represented as {@link ScaffoldResource}s. The source
     *        and destinations are provided by the {@link ScaffoldResource}s.
     * @param overwrite A flag indicating whether existing resources can be overwritten during copying. 
     */
    public CopyWebResourcesEvent(Project project, List<ScaffoldResource> resources, boolean overwrite) {
        this.project = project;
        this.resources = resources;
        this.overwrite = overwrite;
    }

    public Project getProject() {
        return project;
    }

    /**
     * 
     * @return A list of static resources that are to be copied, represented as {@link ScaffoldResource}s. The source and
     *         destinations are provided by the {@link ScaffoldResource}s.
     */
    public List<ScaffoldResource> getResources() {
        return resources;
    }

    /**
     * 
     * @return A flag indicating whether existing resources can be overwritten during copying.
     */
    public boolean isOverwrite() {
        return overwrite;
    }

}
