/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.angularjs.tests.freemarker;

import org.hamcrest.core.IsNull;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.templates.Template;
import org.jboss.forge.addon.templates.TemplateFactory;
import org.jboss.forge.addon.templates.freemarker.FreemarkerTemplate;
import org.jboss.forge.arquillian.AddonDependency;
import org.jboss.forge.arquillian.Dependencies;
import org.jboss.forge.arquillian.archive.ForgeArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.jboss.forge.addon.angularjs.TestHelpers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests to verify that Freemarker templates that generate JavaScript work. Verifies that the templates dont error out during
 * processing. Functional tests verify whether the generated JavaScript actually work.
 */
@RunWith(Arquillian.class)
public class FreemarkerClientTest {

    @Inject
    private ResourceFactory resourceFactory;

    @Inject
    private TemplateFactory processorFactory;

    @Deployment
    @Dependencies({
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
    
    @Test
    public void testGenerateNewEntityController() throws Exception {
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(ENTITY_ID_PROP);
        entityAttributeProperties.add(ENTITY_VERSION_PROP);
        entityAttributeProperties.add(BASIC_STRING_PROP);
        Map<String, Object> root = createEntityRootmap(entityAttributeProperties);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.NEW_ENTITY_CONTROLLER_JS));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        assertThat(output, IsNull.notNullValue());
    }
    
    @Test
    public void testGenerateEditEntityController() throws Exception {
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(ENTITY_ID_PROP);
        entityAttributeProperties.add(ENTITY_VERSION_PROP);
        entityAttributeProperties.add(BASIC_STRING_PROP);
        Map<String, Object> root = createEntityRootmap(entityAttributeProperties);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.EDIT_ENTITY_CONTROLLER_JS));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        assertThat(output, IsNull.notNullValue());
    }
    
    @Test
    public void testGenerateSearchEntityController() throws Exception {
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(ENTITY_ID_PROP);
        entityAttributeProperties.add(ENTITY_VERSION_PROP);
        entityAttributeProperties.add(BASIC_STRING_PROP);
        Map<String, Object> root = createEntityRootmap(entityAttributeProperties);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_ENTITY_CONTROLLER_JS));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        assertThat(output, IsNull.notNullValue());
    }
    
    @Test
    public void testGenerateEntityFactory() throws Exception {
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(ENTITY_ID_PROP);
        entityAttributeProperties.add(ENTITY_VERSION_PROP);
        entityAttributeProperties.add(BASIC_STRING_PROP);
        Map<String, Object> root = createEntityRootmap(entityAttributeProperties);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.ENTITY_FACTORY));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        assertThat(output, IsNull.notNullValue());
    }

    @Test
    public void testGenerateDetailPartial() throws Exception {
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(ENTITY_ID_PROP);
        entityAttributeProperties.add(ENTITY_VERSION_PROP);
        entityAttributeProperties.add(BASIC_STRING_PROP);
        Map<String, Object> root = createEntityRootmap(entityAttributeProperties);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.DETAIL_VIEW));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        assertThat(output, IsNull.notNullValue());
    }
    
    @Test
    public void testGenerateSearchPartial() throws Exception {
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(ENTITY_ID_PROP);
        entityAttributeProperties.add(ENTITY_VERSION_PROP);
        entityAttributeProperties.add(BASIC_STRING_PROP);
        Map<String, Object> root = createEntityRootmap(entityAttributeProperties);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_VIEW));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        assertThat(output, IsNull.notNullValue());
    }
    
    @Test
    public void testGenerateIndex() throws Exception {
        Map<String, Object> root = createGlobalRootmap();

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.INDEX_PAGE));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        assertThat(output, IsNull.notNullValue());
    }

    @Test
    public void testGenerateAngularApplication() throws Exception {
        Map<String, Object> root = createGlobalRootmap();

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.APP_JS));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        assertThat(output, IsNull.notNullValue());
    }
    
}
