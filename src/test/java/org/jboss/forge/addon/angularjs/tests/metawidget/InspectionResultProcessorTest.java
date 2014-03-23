/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.angularjs.tests.metawidget;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.angularjs.ProjectHelper;
import org.jboss.forge.addon.angularjs.tests.freemarker.Deployments;
import org.jboss.forge.addon.parser.java.resources.JavaResource;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.arquillian.AddonDependency;
import org.jboss.forge.arquillian.Dependencies;
import org.jboss.forge.arquillian.archive.ForgeArchive;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.angularjs.InspectionResultProcessor;
import org.jboss.forge.addon.scaffold.metawidget.MetawidgetInspectorFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.TemporalType;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static org.jboss.forge.addon.angularjs.matchers.InspectionResultMatcher.hasItemWithEntry;
import static org.junit.Assert.assertThat;

/**
 * Tests to verify that the {@link InspectionResultProcessor} enhances results correctly.
 */
@RunWith(Arquillian.class)
public class InspectionResultProcessorTest {

    private MetawidgetInspectorFacade metawidgetInspectorFacade;

    @Deployment
    @Dependencies({
            @AddonDependency(name = "org.jboss.forge.addon:projects"),
            @AddonDependency(name = "org.jboss.forge.addon:maven"),
            @AddonDependency(name = "org.jboss.forge.addon:scaffold-spi"),
            @AddonDependency(name = "org.jboss.forge.addon:javaee"),
            @AddonDependency(name = "org.jboss.forge.addon:templates"),
            @AddonDependency(name = "org.jboss.forge.addon:text"),
            @AddonDependency(name = "org.jboss.forge.addon:convert"),
            @AddonDependency(name = "org.jboss.forge.addon:parser-java")
    })
    public static ForgeArchive getDeployment()
    {
        return Deployments.getDeployment();
    }

    private InspectionResultProcessor angularResultEnhancer;

    @Inject
    private ProjectHelper projectHelper;

    private Project project;

    private JavaClass entityClass;

    @Before
    public void setup()
    {
        project = projectHelper.createWebProject();
        projectHelper.installJPA_2_0(project);
        metawidgetInspectorFacade = new MetawidgetInspectorFacade(project);
        angularResultEnhancer = new InspectionResultProcessor(project, metawidgetInspectorFacade);
    }

    @Test
    public void testInspectBasicField() throws Exception {
        String entityName = "Customer";
        String fieldName = "firstName";
        generateSimpleEntity(entityName);
        generateStringField(fieldName);
        JavaClass klass = getJavaClassFor(entityName);
        
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);
        
        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
    }

    @Test
    public void testInspectBasicFieldNotNull() throws Exception {
        String entityName = "Customer";
        String fieldName = "firstName";
        generateSimpleEntity(entityName);
        generateStringField(fieldName);
        generateNotNullConstraint(fieldName);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("required", "true"));
    }

    @Test
    public void testInspectBasicFieldMinSize() throws Exception {
        String entityName = "Customer";
        String fieldName = "firstName";
        generateSimpleEntity(entityName);
        generateStringField(fieldName);
        generateSizeConstraint(fieldName, "5", null);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("minimum-length", "5"));
    }

    @Test
    public void testInspectBasicFieldMaxSize() throws Exception {
        String entityName = "Customer";
        String fieldName = "firstName";
        generateSimpleEntity(entityName);
        generateStringField(fieldName);
        generateSizeConstraint(fieldName, null, "5");

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("maximum-length", "5"));
    }

    @Test
    public void testInspectBasicFieldMinAndMaxSize() throws Exception {
        String entityName = "Customer";
        String fieldName = "firstName";
        generateSimpleEntity(entityName);
        generateStringField(fieldName);
        generateSizeConstraint(fieldName, "5", "50");

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("minimum-length", "5"));
        assertThat(inspectionResult, hasItemWithEntry("maximum-length", "50"));
    }

    @Test
    public void testInspectBooleanField() throws Exception {
        String entityName = "Customer";
        String fieldName = "optForMail";
        generateSimpleEntity(entityName);
        generateBooleanField(fieldName);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("type", "boolean"));
    }
    
    @Test
    public void testInspectDateField() throws Exception {
        String entityName = "Customer";
        String fieldName = "dateOfBirth";
        generateSimpleEntity(entityName);
        generateTemporalField(fieldName, TemporalType.DATE);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("datetime-type", "date"));
    }

    @Test
    public void testInspectTimeField() throws Exception {
        String entityName = "Customer";
        String fieldName = "dailyAlertTime";
        generateSimpleEntity(entityName);
        generateTemporalField(fieldName, TemporalType.TIME);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("datetime-type", "time"));
    }

    @Test
    public void testInspectDateTimeField() throws Exception {
        String entityName = "Customer";
        String fieldName = "lastUpdated";
        generateSimpleEntity(entityName);
        generateTemporalField(fieldName, TemporalType.TIMESTAMP);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("datetime-type", "both"));
    }

    @Test
    public void testInspectIntNumberField() throws Exception {
        String entityName = "Customer";
        String fieldName = "age";
        generateSimpleEntity(entityName);
        generateNumericField(fieldName, "int");

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("type", "number"));
    }

    @Test
    public void testInspectLongNumberField() throws Exception {
        String entityName = "Customer";
        String fieldName = "age";
        generateSimpleEntity(entityName);
        generateNumericField(fieldName, "long");

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("type", "number"));
    }

    @Test
    public void testInspectFloatNumberField() throws Exception {
        String entityName = "Customer";
        String fieldName = "age";
        generateSimpleEntity(entityName);
        generateNumericField(fieldName, Float.class);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("type", "number"));
    }

    @Test
    public void testInspectDoubleNumberField() throws Exception {
        String entityName = "Customer";
        String fieldName = "age";
        generateSimpleEntity(entityName);
        generateNumericField(fieldName, Double.class);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("type", "number"));
    }

    @Test
    public void testInspectNumberFieldMinValue() throws Exception {
        String entityName = "Customer";
        String fieldName = "age";
        generateSimpleEntity(entityName);
        generateNumericField(fieldName, "int");
        generateMinConstraint(fieldName, "0");

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("type", "number"));
        assertThat(inspectionResult, hasItemWithEntry("minimum-value", "0"));
    }

    @Test
    public void testInspectNumberFieldMaxValue() throws Exception {
        String entityName = "Customer";
        String fieldName = "age";
        generateSimpleEntity(entityName);
        generateNumericField(fieldName, "int");
        generateMaxConstraint(fieldName, "100");

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("type", "number"));
        assertThat(inspectionResult, hasItemWithEntry("maximum-value", "100"));
    }

    @Test
    public void testInspectNumberFieldMinAndMaxValue() throws Exception {
        String entityName = "Customer";
        String fieldName = "age";
        generateSimpleEntity(entityName);
        generateNumericField(fieldName, "int");
        generateMinConstraint(fieldName, "0");
        generateMaxConstraint(fieldName, "100");

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("type", "number"));
        assertThat(inspectionResult, hasItemWithEntry("minimum-value", "0"));
        assertThat(inspectionResult, hasItemWithEntry("maximum-value", "100"));
    }
    
    @Test
    public void testInspectOneToOneField() throws Exception {
        String entityName = "Customer";
        String fieldName = "address";
        String relatedEntityName = "CustomerAddress";
        generateSimpleEntity(relatedEntityName);
        String relatedEntityType = getJavaClassNameFor(relatedEntityName);
        generateSimpleEntity(entityName);
        generateOneToOneField(fieldName, relatedEntityType, null, null, false, null);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("one-to-one", "true"));
        assertThat(inspectionResult, hasItemWithEntry("type", relatedEntityType));
    }
    
    @Test
    public void testInspectManyToOneField() throws Exception {
        String entityName = "Customer";
        String fieldName = "category";
        String relatedEntityName = "CustomCategory";
        generateSimpleEntity(relatedEntityName);
        String relatedEntityType = getJavaClassNameFor(relatedEntityName);
        generateSimpleEntity(entityName);
        generateManyToOneField(fieldName, relatedEntityType, null, null, false, null);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("many-to-one", "true"));
        assertThat(inspectionResult, hasItemWithEntry("type", relatedEntityType));
    }

    @Test
    public void testInspectOneToManyField() throws Exception {
        String entityName = "Customer";
        String fieldName = "orders";
        String relatedEntityName = "StoreOrder";
        generateSimpleEntity(relatedEntityName);
        String relatedEntityType = getJavaClassNameFor(relatedEntityName);
        generateSimpleEntity(entityName);
        generateOneToManyField(fieldName, relatedEntityType, null, null, null);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("n-to-many", "true"));
        assertThat(inspectionResult, hasItemWithEntry("type", "java.util.Set"));
        assertThat(inspectionResult, hasItemWithEntry("parameterized-type", relatedEntityType));
    }
    
    @Test
    public void testInspectManyToManyField() throws Exception {
        String entityName = "Customer";
        String fieldName = "visitedStores";
        String relatedEntityName = "Store";
        generateSimpleEntity(relatedEntityName);
        String relatedEntityType = getJavaClassNameFor(relatedEntityName);
        generateSimpleEntity(entityName);
        generateManyToManyField(fieldName, relatedEntityType, null, null, null);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("n-to-many", "true"));
        assertThat(inspectionResult, hasItemWithEntry("type", "java.util.Set"));
        assertThat(inspectionResult, hasItemWithEntry("parameterized-type", relatedEntityType));
    }
    
    @Test
    public void testInspectBidiOneToOneField() throws Exception {
        String entityName = "Customer";
        String fieldName = "address";
        String relatedEntityName = "CustomerAddress";
        generateSimpleEntity(relatedEntityName);
        String relatedEntityType = getJavaClassNameFor(relatedEntityName);
        generateSimpleEntity(entityName);
        generateOneToOneField(fieldName, relatedEntityType, "customer", null, false, null);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("one-to-one", "true"));
        assertThat(inspectionResult, hasItemWithEntry("type", relatedEntityType));
    }
    
    @Test
    public void testInspectBidiManyToOneField() throws Exception {
        String entityName = "Customer";
        String fieldName = "category";
        String relatedEntityName = "CustomCategory";
        generateSimpleEntity(relatedEntityName);
        String relatedEntityType = getJavaClassNameFor(relatedEntityName);
        generateSimpleEntity(entityName);
        generateManyToOneField(fieldName, relatedEntityType, "customer", null, false, null);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("many-to-one", "true"));
        assertThat(inspectionResult, hasItemWithEntry("type", relatedEntityType));
    }

    @Test
    public void testInspectBidiOneToManyField() throws Exception {
        String entityName = "Customer";
        String fieldName = "orders";
        String relatedEntityName = "StoreOrder";
        String inverseFieldName = "customer";
        generateSimpleEntity(relatedEntityName);
        String relatedEntityType = getJavaClassNameFor(relatedEntityName);
        generateSimpleEntity(entityName);
        generateOneToManyField(fieldName, relatedEntityType, inverseFieldName, null, null);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("n-to-many", "true"));
        assertThat(inspectionResult, hasItemWithEntry("type", "java.util.Set"));
        assertThat(inspectionResult, hasItemWithEntry("parameterized-type", relatedEntityType));
        assertThat(inspectionResult, hasItemWithEntry("inverse-relationship", inverseFieldName));
    }
    
    @Test
    public void testInspectBidiManyToManyField() throws Exception {
        String entityName = "Customer";
        String fieldName = "visitedStores";
        String relatedEntityName = "Store";
        String inverseFieldName = "customer";
        generateSimpleEntity(relatedEntityName);
        String relatedEntityType = getJavaClassNameFor(relatedEntityName);
        generateSimpleEntity(entityName);
        generateManyToManyField(fieldName, relatedEntityType, inverseFieldName, null, null);

        JavaClass klass = getJavaClassFor(entityName);
        List<Map<String, String>> inspectionResult = metawidgetInspectorFacade.inspect(klass);
        inspectionResult = angularResultEnhancer.enhanceResults(klass, inspectionResult);

        assertThat(inspectionResult, hasItemWithEntry("name", fieldName));
        assertThat(inspectionResult, hasItemWithEntry("n-to-many", "true"));
        assertThat(inspectionResult, hasItemWithEntry("type", "java.util.Set"));
        assertThat(inspectionResult, hasItemWithEntry("parameterized-type", relatedEntityType));
        // TODO: The inverse-relatioship is not available for ManyToMany bidi relations. Investigate. 
        //assertThat(inspectionResult, hasItemWithEntry("inverse-relationship", inverseFieldName));
    }

    private JavaClass getJavaClassFor(String entityName) throws FileNotFoundException {
        JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);
        return (JavaClass) java.getJavaResource(java.getBasePackage() + ".model." + entityName).getJavaSource();
    }
    
    private String getJavaClassNameFor(String entityName) throws FileNotFoundException {
        JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);
        return java.getBasePackage() + ".model." + entityName;
    }

    private void generateSimpleEntity(String entityName) throws Exception {
        JavaResource jpaEntity = projectHelper.createJPAEntity(project, entityName);
        entityClass = (JavaClass)jpaEntity.getJavaSource();
        saveJavaSource();
    }

    private void generateStringField(String fieldName) throws Exception {
        projectHelper.createStringField(entityClass, fieldName);
        saveJavaSource();
    }
    
    private void generateBooleanField(String fieldName) throws Exception {
        projectHelper.createBooleanField(entityClass, fieldName);
        saveJavaSource();
    }

    private void generateTemporalField(String fieldName, TemporalType type) throws Exception {
        projectHelper.createTemporalField(entityClass, fieldName, type);
        saveJavaSource();
    }

    private void generateNumericField(String fieldName, String type) throws Exception {
        switch (type)
        {
            case "int" :
                projectHelper.createIntField(entityClass, fieldName);
                break;
            case "long" :
                projectHelper.createLongField(entityClass, fieldName);
                break;
            default:
                throw new RuntimeException("Incorrect field type provided as input");
        }
        saveJavaSource();
    }

    private void generateNumericField(String fieldName, Class<? extends Number> klass) throws Exception {
        projectHelper.createNumericField(entityClass, fieldName, klass);
        saveJavaSource();
    }
    
    private void generateOneToOneField(String fieldName, String type, String inverseFieldName, FetchType fetchType, boolean required, Iterable<CascadeType> cascadeTypes) throws Exception {
        JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);
        JavaResource entityClassResource = java.getJavaResource(entityClass);
        projectHelper.createOneToOneField(project, entityClassResource, fieldName, type, inverseFieldName, fetchType, required, cascadeTypes);
        entityClass = getJavaClassFor(entityClass.getName());
    }

    private void generateManyToOneField(String fieldName, String type, String inverseFieldName, FetchType fetchType, boolean required, Iterable<CascadeType> cascadeTypes) throws Exception {
        JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);
        JavaResource entityClassResource = java.getJavaResource(entityClass);
        projectHelper.createManyToOneField(project, entityClassResource, fieldName, type, inverseFieldName, fetchType, required, cascadeTypes);
        entityClass = getJavaClassFor(entityClass.getName());
    }

    private void generateOneToManyField(String fieldName, String type, String inverseFieldName, FetchType fetchType, Iterable<CascadeType> cascadeTypes) throws Exception {
        JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);
        JavaResource entityClassResource = java.getJavaResource(entityClass);
        projectHelper.createOneToManyField(project, entityClassResource, fieldName, type, inverseFieldName, fetchType, cascadeTypes);
        entityClass = getJavaClassFor(entityClass.getName());
    }

    private void generateManyToManyField(String fieldName, String type, String inverseFieldName, FetchType fetchType, Iterable<CascadeType> cascadeTypes) throws Exception {
        JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);
        JavaResource entityClassResource = java.getJavaResource(entityClass);
        projectHelper.createManyToManyField(project, entityClassResource, fieldName, type, inverseFieldName, fetchType, cascadeTypes);
        entityClass = getJavaClassFor(entityClass.getName());
    }

    private void generateNotNullConstraint(String fieldName) throws Exception {
        projectHelper.addNotNullConstraint(project, entityClass, fieldName, false, null);
        entityClass = getJavaClassFor(entityClass.getName());
    }

    private void generateSizeConstraint(String fieldName, String minSize, String maxSize) throws Exception {
        projectHelper.addSizeConstraint(project, entityClass, fieldName, false, null, minSize, maxSize);
        entityClass = getJavaClassFor(entityClass.getName());
    }

    private void generateMinConstraint(String fieldName, String minValue) throws Exception {
        projectHelper.addMinConstraint(project, entityClass, fieldName, false, null, minValue);
        entityClass = getJavaClassFor(entityClass.getName());
    }

    private void generateMaxConstraint(String fieldName, String maxValue) throws Exception {
        projectHelper.addMaxConstraint(project, entityClass, fieldName, false, null, maxValue);
        entityClass = getJavaClassFor(entityClass.getName());
    }

    private void saveJavaSource() throws FileNotFoundException {
        JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);
        java.saveJavaSource(entityClass);
    }

}
