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
import org.metawidget.util.simple.StringUtils;

import javax.inject.Inject;

import java.net.URL;
import java.util.Map;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.jboss.forge.addon.angularjs.TestHelpers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests to verify that the generated HTML for Enum properties of JPA entities are generated correctly.
 */
@RunWith(Arquillian.class)
public class FreemarkerClientPartialsLookupPropertyTest {

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

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.LOOKUP_PROPERTY_DETAIL_INCLUDE));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        assertThat(output.trim(), IsEqual.equalTo(""));
    }
    
    @Test
    public void testGenerateHiddenAndRequiredProperty() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, ENTITY_ID_PROP);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.LOOKUP_PROPERTY_DETAIL_INCLUDE));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        assertThat(output.trim(), IsEqual.equalTo(""));
    }
    
    @Test
    public void testGenerateEnumProperty() throws Exception {
        String enumProperty = "paymentType";
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, ENUM_PROP);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.LOOKUP_PROPERTY_DETAIL_INCLUDE));
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
        assertThat(selectElement.attr("id"), equalTo(enumProperty));
        assertThat(selectElement.attr("ng-model"), equalTo(StringUtils.camelCase(ENTITY_NAME) + "." + enumProperty));
        String collectionElement = enumProperty.substring(0, 1);
        String optionsExpression = collectionElement +" for "+ collectionElement +" in " + enumProperty + "List";
        assertThat(selectElement.attr("ng-options"), equalTo(optionsExpression));
    }

}
