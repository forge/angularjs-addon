/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.angularjs.tests.freemarker;

import org.hamcrest.core.IsEqual;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.net.URL;
import java.util.Map;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.jboss.forge.addon.angularjs.TestHelpers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests to verify that the generated HTML for 1:M and M:M associations of JPA entities are generated correctly.
 */
@RunWith(Arquillian.class)
public class FreemarkerClientPartialsNToManyPropertyTest {

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
    public void testGenerateHiddenProperty() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, ENTITY_VERSION_PROP);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.N_TO_MANY_PROPERTY_DETAIL_INCLUDE));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        assertThat(output.trim(), IsEqual.equalTo(""));
    }
    
    @Test
    public void testGenerateHiddenAndRequiredProperty() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, ENTITY_ID_PROP);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.N_TO_MANY_PROPERTY_DETAIL_INCLUDE));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        assertThat(output.trim(), IsEqual.equalTo(""));
    }
    
    @Test
    public void testGenerateOneToManyProperty() throws Exception {
        String oneToManyProperty = "orders";
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, ONE_TO_MANY_PROP);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.N_TO_MANY_PROPERTY_DETAIL_INCLUDE));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements nToManyWidgetElement = html.select("div.form-group > div.col-sm-10");
        assertThat(nToManyWidgetElement, notNullValue());

        Elements selectElement = nToManyWidgetElement.select(" > select");
        assertThat(selectElement.attr("id"), equalTo(oneToManyProperty));
        assertThat(selectElement.attr("multiple"), notNullValue());
        assertThat(selectElement.attr("ng-model"), equalTo(oneToManyProperty+"Selection"));
        String collectionElement = oneToManyProperty.substring(0, 1);
        String optionsExpression = collectionElement +".text for "+ collectionElement +" in " + oneToManyProperty + "SelectionList";
        assertThat(selectElement.attr("ng-options"), equalTo(optionsExpression));
    }
    
    @Test
    public void testGenerateManyToManyProperty() throws Exception {
        String manyToManyProperty = "users";
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, MANY_TO_MANY_PROP);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.N_TO_MANY_PROPERTY_DETAIL_INCLUDE));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements nToManyWidgetElement = html.select("div.form-group > div.col-sm-10");
        assertThat(nToManyWidgetElement, notNullValue());

        Elements selectElement = nToManyWidgetElement.select(" > select");
        assertThat(selectElement.attr("id"), equalTo(manyToManyProperty));
        assertThat(selectElement.attr("multiple"), notNullValue());
        assertThat(selectElement.attr("ng-model"), equalTo(manyToManyProperty+"Selection"));
        String collectionElement = manyToManyProperty.substring(0, 1);
        String optionsExpression = collectionElement +".text for "+ collectionElement +" in " + manyToManyProperty + "SelectionList";
        assertThat(selectElement.attr("ng-options"), equalTo(optionsExpression));
    }

}
