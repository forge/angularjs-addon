/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.angularjs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A builder to help in creation of Metawidget inspection results to be used in tests, without actually inspecting classes or
 * objects.
 */
class PropertyBuilder {
    private Map<String, String> properties = new HashMap<String, String>();

    public Map<String, String> create() {
        return Collections.unmodifiableMap(properties);
    }

    public PropertyBuilder withName(String name) {
        properties.put("name", name);
        return this;
    }
    
    public PropertyBuilder withIdentifier(String identifier) {
        properties.put("identifier", identifier);
        return this;
    }

    public PropertyBuilder withLabel(String label) {
        properties.put("label", label);
        return this;
    }

    public PropertyBuilder withType(String type) {
        properties.put("type", type);
        return this;
    }

    public PropertyBuilder asRequired() {
        properties.put("required", "true");
        return this;
    }

    public PropertyBuilder asHidden() {
        properties.put("hidden", "true");
        return this;
    }

    public PropertyBuilder withMinLen(String len) {
        properties.put("minimum-length", len);
        return this;
    }

    public PropertyBuilder withMaxLen(String len) {
        properties.put("maximum-length", len);
        return this;
    }

    public PropertyBuilder withMinVal(String min) {
        properties.put("minimum-value", min);
        return this;
    }

    public PropertyBuilder withMaxVal(String min) {
        properties.put("maximum-value", min);
        return this;
    }

    public PropertyBuilder asDate() {
        properties.put("datetime-type", "date");
        return this;
    }

    public PropertyBuilder asTime() {
        properties.put("datetime-type", "time");
        return this;
    }

    public PropertyBuilder asDateTime() {
        properties.put("datetime-type", "both");
        return this;
    }

    public PropertyBuilder asNToMany() {
        properties.put("n-to-many", "true");
        return this;
    }

    public PropertyBuilder withParameterizedType(String parameterizedType) {
        properties.put("parameterized-type", parameterizedType);
        return this;
    }

    public PropertyBuilder withSimpleType(String simpleType) {
        properties.put("simpleType", simpleType);
        return this;
    }

    public PropertyBuilder withOptionLabel(String optionLabel) {
        properties.put("optionLabel", optionLabel);
        return this;
    }

    public PropertyBuilder as1To1() {
        properties.put("one-to-one", "true");
        return this;
    }

    public PropertyBuilder asMTo1() {
        properties.put("many-to-one", "true");
        return this;
    }

    public PropertyBuilder withLookupValues(String lookupValues) {
        properties.put("lookup", lookupValues);
        return this;
    }
}