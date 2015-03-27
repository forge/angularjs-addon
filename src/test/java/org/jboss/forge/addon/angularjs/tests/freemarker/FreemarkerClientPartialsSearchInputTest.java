/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.angularjs.tests.freemarker;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.jboss.forge.addon.angularjs.TestHelpers.BASIC_STRING_PROP;
import static org.jboss.forge.addon.angularjs.TestHelpers.BOOLEAN_PROP;
import static org.jboss.forge.addon.angularjs.TestHelpers.DATE_PROP;
import static org.jboss.forge.addon.angularjs.TestHelpers.ENTITY_ID_PROP;
import static org.jboss.forge.addon.angularjs.TestHelpers.ENTITY_NAME;
import static org.jboss.forge.addon.angularjs.TestHelpers.ENTITY_VERSION_PROP;
import static org.jboss.forge.addon.angularjs.TestHelpers.MANY_TO_MANY_PROP;
import static org.jboss.forge.addon.angularjs.TestHelpers.MANY_TO_ONE_PROP;
import static org.jboss.forge.addon.angularjs.TestHelpers.NUMBER_PROP;
import static org.jboss.forge.addon.angularjs.TestHelpers.ONE_TO_MANY_PROP;
import static org.jboss.forge.addon.angularjs.TestHelpers.ONE_TO_ONE_PROP;
import static org.jboss.forge.addon.angularjs.TestHelpers.createInspectionResultWrapper;
import static org.junit.Assert.assertThat;

import java.net.URL;
import java.util.Map;

import javax.inject.Inject;

import org.hamcrest.core.IsEqual;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.addon.angularjs.TestHelpers;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.templates.Template;
import org.jboss.forge.addon.templates.TemplateFactory;
import org.jboss.forge.addon.templates.freemarker.FreemarkerTemplate;
import org.jboss.forge.arquillian.AddonDependencies;
import org.jboss.forge.arquillian.AddonDependency;
import org.jboss.forge.arquillian.archive.AddonArchive;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests to verify that the generated HTML of the search page is generated correctly.
 */
@RunWith(Arquillian.class)
public class FreemarkerClientPartialsSearchInputTest {

    @Inject
    private ResourceFactory resourceFactory;

    @Inject
    private TemplateFactory processorFactory;

    @Deployment
    @AddonDependencies({
            @AddonDependency(name = "org.jboss.forge.addon:scaffold-spi"),
            @AddonDependency(name = "org.jboss.forge.addon:javaee"),
            @AddonDependency(name = "org.jboss.forge.addon:templates"),
            @AddonDependency(name = "org.jboss.forge.addon:text"),
            @AddonDependency(name = "org.jboss.forge.addon:convert"),
            @AddonDependency(name = "org.jboss.forge.addon:parser-java"),
            @AddonDependency(name = "org.jboss.forge.furnace.container:cdi")
    })
    public static AddonArchive getDeployment()
    {
        return Deployments.getDeployment();
    }

    @Test
    public void testGenerateHiddenProperty() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, ENTITY_VERSION_PROP);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_FORM_INPUT));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        assertThat(output.trim(), IsEqual.equalTo(""));
    }

    @Test
    public void testGenerateHiddenAndRequiredProperty() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, ENTITY_ID_PROP);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_FORM_INPUT));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        assertThat(output.trim(), IsEqual.equalTo(""));
    }

    @Test
    public void testGenerateOneToManyProperty() throws Exception {
        Map<String, Object> root = TestHelpers.createInspectionResultWrapper(ENTITY_NAME, ONE_TO_MANY_PROP);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_FORM_INPUT));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        assertThat(output.trim(), IsEqual.equalTo(""));
    }

    @Test
    public void testGenerateManyToManyProperty() throws Exception {
        Map<String, Object> root = TestHelpers.createInspectionResultWrapper(ENTITY_NAME, MANY_TO_MANY_PROP);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_FORM_INPUT));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        assertThat(output.trim(), IsEqual.equalTo(""));
    }

    @Test
    public void testGenerateBasicStringProperty() throws Exception {
        Map<String, Object> root = TestHelpers.createInspectionResultWrapper(ENTITY_NAME, BASIC_STRING_PROP);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_FORM_INPUT));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));

        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());

        Elements formInputElement = container.select("div.col-sm-10 > input");
        assertThat(formInputElement.attr("id"), equalTo("fullName"));
        assertThat(formInputElement.attr("type"), equalTo("text"));
        assertThat(formInputElement.attr("ng-model"), equalTo("search" + "." + "fullName"));
    }

    @Test
    public void testGenerateBasicNumberProperty() throws Exception {
        Map<String, Object> root = TestHelpers.createInspectionResultWrapper(ENTITY_NAME, NUMBER_PROP);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_FORM_INPUT));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));

        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());

        Elements formInputElement = container.select("div.col-sm-10 > input");
        assertThat(formInputElement.attr("id"), equalTo("score"));
        assertThat(formInputElement.attr("type"), equalTo("text"));
        assertThat(formInputElement.attr("ng-model"), equalTo("search" + "." + "score"));
    }

    @Test
    public void testGenerateBasicBooleanProperty() throws Exception {
        Map<String, Object> root = TestHelpers.createInspectionResultWrapper(ENTITY_NAME, BOOLEAN_PROP);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_FORM_INPUT));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));

        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());

        Elements formInputElement = container.select("div.col-sm-10 > select");
        assertThat(formInputElement.attr("id"), equalTo("optForMail"));
        assertThat(formInputElement.attr("ng-model"), equalTo("search" + "." + "optForMail"));
    }

    @Test
    public void testGenerateBasicDateProperty() throws Exception {
        Map<String, Object> root = TestHelpers.createInspectionResultWrapper(ENTITY_NAME, DATE_PROP);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_FORM_INPUT));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));

        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());

        Elements formInputElement = container.select("div.col-sm-10 > input");
        assertThat(formInputElement.attr("id"), equalTo("dateOfBirth"));
        assertThat(formInputElement.attr("type"), equalTo("text"));
        assertThat(formInputElement.attr("ng-model"), equalTo("search" + "." + "dateOfBirth"));
    }

    @Test
    public void testGenerateOneToOneProperty() throws Exception {
        Map<String, Object> root = TestHelpers.createInspectionResultWrapper(ENTITY_NAME, ONE_TO_ONE_PROP);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_FORM_INPUT));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));

        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());

        Elements formInputElement = container.select("div.col-sm-10 > select");
        assertThat(formInputElement.attr("id"), equalTo("voucher"));
        assertThat(formInputElement.attr("ng-model"), equalTo("search" + "." + "voucher"));
    }

    @Test
    public void testGenerateManyToOneProperty() throws Exception {
        Map<String, Object> root = TestHelpers.createInspectionResultWrapper(ENTITY_NAME, MANY_TO_ONE_PROP);

        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_FORM_INPUT));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));

        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());

        Elements formInputElement = container.select("div.col-sm-10 > select");
        assertThat(formInputElement.attr("id"), equalTo("customer"));
        assertThat(formInputElement.attr("ng-model"), equalTo("search" + "." + "customer"));
    }

}
