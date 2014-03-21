/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.angularjs;

import org.jboss.forge.addon.scaffold.util.ScaffoldUtil;

/**
 * An abstraction of a resource used by the Scaffold provider. The scaffold resources will specify the source from where they
 * can be read, and the destination to which they are to be copied to, so that this information is available to utlity classes
 * like the {@link ScaffoldUtil} class.
 * 
 * This abstraction is meant to reduce the verbosity involved in using the {@link ScaffoldUtil} class, since resource paths can
 * now be stored and passed around in collections, and iterated through.
 * 
 */
public class ScaffoldResource {

    String source;
    String destination;

    public ScaffoldResource(String source) {
        this(source, null);
    }

    public ScaffoldResource(String source, String destination) {
        this.source = source;
        this.destination = destination;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
    
}
