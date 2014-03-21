/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.angularjs.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A Hamcrest matcher to aid in verifying that inspection results represented as a List of Map of String key-value pairs,
 * contains a key-value pair in one of the Maps (i.e. one of the class/object properties).
 */
public class InspectionResultMatcher<T> extends TypeSafeMatcher<List<Map<String, String>>> {

    private String key;
    private String value;
    private Entry<String, String> attributeEntry;

    public InspectionResultMatcher(final String key, final String value) {
        this.key = key;
        this.value = value;
        this.attributeEntry = new Entry<String, String>() {

            @Override
            public String getKey() {
                return key;
            }

            @Override
            public String getValue() {
                return value;
            }

            @Override
            public String setValue(String value) {
                // Doing nothing here. We're just matching keys and values, not modifying them
                return null;
            }
        };
    }

    @Override
    protected boolean matchesSafely(List<Map<String, String>> allPropertyAttributes) {
        for (Map<String, String> singlePropertyAttributes : allPropertyAttributes) {
            for (Entry<String, String> propertyAttribute : singlePropertyAttributes.entrySet()) {
                if (propertyAttribute.equals(attributeEntry)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a Map with entry containing " + key + "= " + value);
    }

    public static Matcher<List<Map<String, String>>> hasItemWithEntry(String key, String value) {
        return new InspectionResultMatcher<List<Map<String, String>>>(key, value);
    }

}
