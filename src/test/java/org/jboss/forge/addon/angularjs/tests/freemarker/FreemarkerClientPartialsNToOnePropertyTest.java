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
import org.jboss.forge.addon.templates.TemplateProcessor;
import org.jboss.forge.addon.templates.TemplateProcessorFactory;
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
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests to verify that the generated HTML for 1:1 and M:1 associations of JPA entities are generated correctly.
 */
@RunWith(Arquillian.class)
public class FreemarkerClientPartialsNToOnePropertyTest {

    @Inject
    private ResourceFactory resourceFactory;

    @Inject
    private TemplateProcessorFactory processorFactory;

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
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("identifier", "id");
        idProperties.put("label", "Id");
        idProperties.put("hidden", "true");
        idProperties.put("type", "number");

        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", idProperties);
        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.N_TO_ONE_PROPERTY_DETAIL_INCLUDE));
        TemplateProcessor processor = processorFactory.fromTemplate(new FreemarkerTemplate(templateResource));
        String output = processor.process(root);
        assertThat(output.trim(), IsEqual.equalTo(""));
    }

    @Test
    public void testGenerateHiddenAndRequiredProperty() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("identifier", "id");
        idProperties.put("label", "Id");
        idProperties.put("hidden", "true");
        idProperties.put("required", "true");
        idProperties.put("type", "number");

        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", idProperties);
        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.N_TO_ONE_PROPERTY_DETAIL_INCLUDE));
        TemplateProcessor processor = processorFactory.fromTemplate(new FreemarkerTemplate(templateResource));
        String output = processor.process(root);
        assertThat(output.trim(), IsEqual.equalTo(""));
    }

    @Test
    public void testGenerateOneToOneProperty() throws Exception {
        Map<String, String> voucherProperties = new HashMap<String, String>();
        String oneToOneProperty = "voucher";
        voucherProperties.put("name", oneToOneProperty);
        voucherProperties.put("identifier", oneToOneProperty);
        voucherProperties.put("label", "Voucher");
        voucherProperties.put("type", "com.example.scaffoldtester.model.DiscountVoucher");
        voucherProperties.put("one-to-one", "true");
        voucherProperties.put("simpleType", "DiscountVoucher");
        voucherProperties.put("optionLabel", "id");

        Map<String, Object> root = new HashMap<String, Object>();
        String entityName = "SampleEntity";
        root.put("entityName", entityName);
        root.put("property", voucherProperties);
        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.N_TO_ONE_PROPERTY_DETAIL_INCLUDE));
        TemplateProcessor processor = processorFactory.fromTemplate(new FreemarkerTemplate(templateResource));
        String output = processor.process(root);
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));

        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));

        Elements oneToOneWidgetElement = html.select("div.form-group > div.col-sm-10");
        assertThat(oneToOneWidgetElement, notNullValue());

        Elements selectElement = oneToOneWidgetElement.select(" > select");
        assertThat(selectElement, notNullValue());
        assertThat(selectElement.attr("id"), equalTo(oneToOneProperty));
        String collectionElement = oneToOneProperty.substring(0, 1);
        String optionsExpression = collectionElement + ".text for " + collectionElement + " in "
                + oneToOneProperty + "SelectionList";
        assertThat(selectElement.attr("ng-options"), equalTo(optionsExpression));
        assertThat(selectElement.attr("ng-model"), equalTo(oneToOneProperty + "Selection"));
    }

    @Test
    public void testGenerateManyToOneProperty() throws Exception {
        Map<String, String> customerProperties = new HashMap<String, String>();
        String oneToOneProperty = "customer";
        customerProperties.put("name", oneToOneProperty);
        customerProperties.put("identifier", oneToOneProperty);
        customerProperties.put("label", "Customer");
        customerProperties.put("type", "com.example.scaffoldtester.model.Customer");
        customerProperties.put("many-to-one", "true");
        customerProperties.put("simpleType", "Customer");
        customerProperties.put("optionLabel", "id");

        Map<String, Object> root = new HashMap<String, Object>();
        String entityName = "SampleEntity";
        root.put("entityName", entityName);
        root.put("property", customerProperties);
        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.N_TO_ONE_PROPERTY_DETAIL_INCLUDE));
        TemplateProcessor processor = processorFactory.fromTemplate(new FreemarkerTemplate(templateResource));
        String output = processor.process(root);
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));

        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));

        Elements oneToOneWidgetElement = html.select("div.form-group > div.col-sm-10");
        assertThat(oneToOneWidgetElement, notNullValue());

        Elements selectElement = oneToOneWidgetElement.select(" > select");
        assertThat(selectElement, notNullValue());
        assertThat(selectElement.attr("id"), equalTo(oneToOneProperty));
        String collectionElement = oneToOneProperty.substring(0, 1);
        String optionsExpression = collectionElement + ".text for " + collectionElement + " in "
                + oneToOneProperty + "SelectionList";
        assertThat(selectElement.attr("ng-options"), equalTo(optionsExpression));
        assertThat(selectElement.attr("ng-model"), equalTo(oneToOneProperty + "Selection"));
    }

}
