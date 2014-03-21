/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.angularjs;

import org.jboss.forge.addon.projects.Project;

import java.util.List;
import java.util.Map;

/**
 * A CDI event that is fired whenever inspection results are to be tranformed by Freemarker into generated content/source code
 * in the scaffold.
 */
public class ProcessWithFreemarkerEvent {

    private Project project;
    private List<ScaffoldResource> resources;
    private Map<String, Object> root;
    private boolean overwrite;

    /**
     * Creates an new instances of a {@link ProcessWithFreemarkerEvent} CDI event.
     * 
     * @param resources The Freemarker templates that are to be processed, represented as {@link ScaffoldResource}s. The source
     *        of the {@link ScaffoldResource} instance should point to the Freemarker template, while the destination should
     *        indicate the location where the generated content should be written to.
     * @param root The data model to be used when processing the Freemarker templates. This should contain the inspection
     *        results and other elements referenced in the templates.
     * @param overwrite A flag indicating whether existing resources at the destination should be overwritten should the
     *        template processing generate resources with the same location.
     */
    public ProcessWithFreemarkerEvent(Project project, List<ScaffoldResource> resources, Map<String, Object> root, boolean overwrite) {
        this.project = project;
        this.resources = resources;
        this.root = root;
        this.overwrite = overwrite;
    }

    public Project getProject() {
        return project;
    }

    /**
     * Provides the list of Freemarker templates that are to be processed.
     * 
     * @return The list of Freemarker templates represented as {@link ScaffoldResource}s.
     */
    public List<ScaffoldResource> getResources() {
        return resources;
    }

    /**
     * Provides the Freemarker data model to be utilized in templates.
     * 
     * @return The Freemarker data model to be used during template processing.
     */
    public Map<String, Object> getRoot() {
        return root;
    }

    /**
     * Indicates whether existing resources possibly generated from earlier scaffolding executions should be overwritten.
     * 
     * @return A flag indicating whether existing resources should be overwritten.
     */
    public boolean isOverwrite() {
        return overwrite;
    }

}
