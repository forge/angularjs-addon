/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.angularjs;

import org.metawidget.util.simple.StringUtils;

import java.util.*;

/**
 * A Helper class to create simulated inspection results of various kinds.
 */
public class TestHelpers {
    
    static
    {
        STRING_CLASS = String.class.getName();
        SET_CLASS = Set.class.getName();
        DATE_CLASS = Date.class.getName();
    }

    public static final String PROJECT_ID = "testProject";
    
    public static final String ENTITY_NAME = "SampleEntity";

    public static final Map<String, String> ENTITY_VERSION_PROP = createVersionProperty();

    public static final Map<String, String> ENTITY_ID_PROP = createIdProperty();

    public static final Map<String, String> BASIC_STRING_PROP = createBasicStringProperty();

    public static final Map<String, String> STRING_PROP_WITH_MIN_LEN = createStringPropertyWithMinLen();

    public static final Map<String, String> STRING_PROP_WITH_MAX_LEN = createStringPropertyWithMaxLen();

    public static final Map<String, String> NUMBER_PROP = createNumberProperty();

    public static final Map<String, String> NUMBER_PROP_WITH_MIN_VAL = createNumberPropertyWithMinVal();

    public static final Map<String, String> NUMBER_PROP_WITH_MAX_VAL = createNumberPropertyWithMaxVal();

    public static final Map<String, String> DATE_PROP = createDateProperty();

    public static final Map<String, String> TIME_PROP = createTimeProperty();

    public static final Map<String, String> DATETIME_PROP = createDateTimeProperty();

    public static final Map<String, String> BOOLEAN_PROP = createBooleanProperty();

    public static final Map<String, String> ONE_TO_MANY_PROP = createOneToManyProperty();

    public static final Map<String, String> MANY_TO_MANY_PROP = createManyToManyProperty();

    public static final Map<String, String> ONE_TO_ONE_PROP = createOneToOneProperty();

    public static final Map<String, String> MANY_TO_ONE_PROP = createManyToOneProperty();
    
    public static final Map<String, String> ENUM_PROP = createEnumProperty();

    private static final String DATE_CLASS;

    private static final String SET_CLASS;

    private static final String STRING_CLASS;

    private static final String BOOLEAN = "boolean";

    private static final String NUMBER = "number";

    public static Map<String, Object> createGlobalRootmap() {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("projectId", StringUtils.camelCase(PROJECT_ID));
        root.put("projectTitle", StringUtils.uncamelCase(PROJECT_ID));
        root.put("entityNames", Arrays.asList(new String[]{ENTITY_NAME}));
        root.put("targetDir", "");
        return Collections.unmodifiableMap(root);
    }
    
    public static Map<String, Object> createEntityRootmap(List<Map<String, String>> entityAttributeProperties) {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("projectId", StringUtils.camelCase(PROJECT_ID));
        root.put("projectTitle", StringUtils.uncamelCase(PROJECT_ID));
        root.put("entityName", ENTITY_NAME);
        root.put("entityId", "id");
        root.put("resourceRootPath", "rest");
        root.put("resourcePath", ENTITY_NAME + "s");
        root.put("parentDirectories", "");
        root.put("properties", entityAttributeProperties);
        return Collections.unmodifiableMap(root);
    }

    public static Map<String, Object> createInspectionResultWrapper(String entityName, Map<String, String> propertyAttributes) {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", entityName);
        root.put("entityId", "id");
        root.put("property", propertyAttributes);
        return root;
    }

    private static Map<String, String> createVersionProperty() {
        String propertyName = "version";
        Map<String, String> properties = new PropertyBuilder().withName(propertyName).withIdentifier(propertyName)
                .withLabel(StringUtils.uncamelCase(propertyName)).asHidden().withType(NUMBER).create();
        return properties;
    }

    private static Map<String, String> createIdProperty() {
        String propertyName = "id";
        Map<String, String> properties = new PropertyBuilder().withName(propertyName).withIdentifier(propertyName)
                .withLabel(StringUtils.uncamelCase(propertyName)).asHidden().asRequired().withType(NUMBER).create();
        return properties;
    }

    private static Map<String, String> createBasicStringProperty() {
        String propertyName = "fullName";
        Map<String, String> properties = new PropertyBuilder().withName(propertyName).withIdentifier(propertyName)
                .withLabel(StringUtils.uncamelCase(propertyName)).withType(STRING_CLASS).create();
        return properties;
    }

    private static Map<String, String> createStringPropertyWithMinLen() {
        String propertyName = "fullName";
        String minimumLength = "5";
        Map<String, String> properties = new PropertyBuilder().withName(propertyName).withIdentifier(propertyName)
                .withLabel(StringUtils.uncamelCase(propertyName)).withType(STRING_CLASS).withMinLen(minimumLength).create();
        return properties;
    }

    private static Map<String, String> createStringPropertyWithMaxLen() {
        String propertyName = "fullName";
        String maximumLength = "100";
        Map<String, String> properties = new PropertyBuilder().withName(propertyName).withIdentifier(propertyName)
                .withLabel(StringUtils.uncamelCase(propertyName)).withType(STRING_CLASS).withMaxLen(maximumLength).create();
        return properties;
    }

    private static Map<String, String> createNumberProperty() {
        String propertyName = "score";
        Map<String, String> properties = new PropertyBuilder().withName(propertyName).withIdentifier(propertyName)
                .withLabel(StringUtils.uncamelCase(propertyName)).withType(NUMBER).create();
        return properties;
    }

    private static Map<String, String> createNumberPropertyWithMinVal() {
        String propertyName = "score";
        String min = "0";
        Map<String, String> properties = new PropertyBuilder().withName(propertyName).withIdentifier(propertyName)
                .withLabel(StringUtils.uncamelCase(propertyName)).withType(NUMBER).withMinVal(min).create();
        return properties;
    }

    private static Map<String, String> createNumberPropertyWithMaxVal() {
        String propertyName = "score";
        String max = "100";
        Map<String, String> properties = new PropertyBuilder().withName(propertyName).withIdentifier(propertyName)
                .withLabel(StringUtils.uncamelCase(propertyName)).withType(NUMBER).withMaxVal(max).create();
        return properties;
    }

    private static Map<String, String> createDateProperty() {
        String propertyName = "dateOfBirth";
        Map<String, String> properties = new PropertyBuilder().withName(propertyName).withIdentifier(propertyName)
                .withLabel(StringUtils.uncamelCase(propertyName)).withType(DATE_CLASS).asDate().create();
        return properties;
    }

    private static Map<String, String> createTimeProperty() {
        String propertyName = "alarmTime";
        Map<String, String> properties = new PropertyBuilder().withName(propertyName).withIdentifier(propertyName)
                .withLabel(StringUtils.uncamelCase(propertyName)).withType(DATE_CLASS).asTime().create();
        return properties;
    }

    private static Map<String, String> createDateTimeProperty() {
        String propertyName = "auditTimestamp";
        Map<String, String> properties = new PropertyBuilder().withName(propertyName).withIdentifier(propertyName)
                .withLabel(StringUtils.uncamelCase(propertyName)).withType(DATE_CLASS).asDateTime().create();
        return properties;
    }

    private static Map<String, String> createBooleanProperty() {
        String propertyName = "optForMail";
        Map<String, String> properties = new PropertyBuilder().withName(propertyName).withIdentifier(propertyName)
                .withLabel(StringUtils.uncamelCase(propertyName)).withType(BOOLEAN).create();
        return properties;
    }

    private static Map<String, String> createOneToManyProperty() {
        String lhsProperty = "orders";
        String rhsType = "com.example.scaffoldtester.model.StoreOrder";
        String rhsSimpleType = "StoreOrder";
        String rhsOptionLabel = "id";
        Map<String, String> properties = new PropertyBuilder().withName(lhsProperty).withIdentifier(lhsProperty)
                .withLabel(StringUtils.uncamelCase(lhsProperty)).withType(SET_CLASS).asNToMany().withParameterizedType(rhsType)
                .withSimpleType(rhsSimpleType).withOptionLabel(rhsOptionLabel).create();
        return properties;
    }

    private static Map<String, String> createManyToManyProperty() {
        String lhsProperty = "users";
        String rhsType = "com.example.scaffoldtester.model.UserIdentity";
        String rhsSimpleType = "UserIdentity";
        String rhsOptionLabel = "id";
        Map<String, String> properties = new PropertyBuilder().withName(lhsProperty).withIdentifier(lhsProperty)
                .withLabel(StringUtils.uncamelCase(lhsProperty)).withType(SET_CLASS).asNToMany().withParameterizedType(rhsType)
                .withSimpleType(rhsSimpleType).withOptionLabel(rhsOptionLabel).create();
        return properties;
    }

    private static Map<String, String> createOneToOneProperty() {
        String lhsProperty = "voucher";
        String rhsType = "com.example.scaffoldtester.model.DiscountVoucher";
        String rhsSimpleType = "DiscountVoucher";
        String rhsOptionLabel = "id";
        Map<String, String> properties = new PropertyBuilder().withName(lhsProperty).withIdentifier(lhsProperty)
                .withLabel(StringUtils.uncamelCase(lhsProperty)).withType(rhsType).as1To1().withSimpleType(rhsSimpleType)
                .withOptionLabel(rhsOptionLabel).create();
        return properties;
    }

    private static Map<String, String> createManyToOneProperty() {
        String lhsProperty = "customer";
        String rhsType = "com.example.scaffoldtester.model.Customer";
        String rhsSimpleType = "Customer";
        String rhsOptionLabel = "id";
        Map<String, String> properties = new PropertyBuilder().withName(lhsProperty).withIdentifier(lhsProperty)
                .withLabel(StringUtils.uncamelCase(lhsProperty)).withType(rhsType).asMTo1().withSimpleType(rhsSimpleType)
                .withOptionLabel(rhsOptionLabel).create();
        return properties;
    }
    
    private static Map<String, String> createEnumProperty() {
        String propertyName = "paymentType";
        String lookupValues = "CASH,CREDIT_CARD,DEBIT_CARD";
        String propertyType = "com.example.scaffoldtester.model.DeliveryType";
        Map<String, String> properties = new PropertyBuilder().withName(propertyName).withIdentifier(propertyName)
                .withLabel(StringUtils.uncamelCase(propertyName)).withType(propertyType).withLookupValues(lookupValues).create();
        return properties;
    }

}
