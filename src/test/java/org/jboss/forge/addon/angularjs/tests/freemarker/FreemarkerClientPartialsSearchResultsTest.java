/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.angularjs.tests.freemarker;

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
import org.metawidget.util.simple.StringUtils;

import javax.inject.Inject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

/**
 * Tests to verify that the generated HTML for the search results table in the search page is generated correctly.
 */
@RunWith(Arquillian.class)
public class FreemarkerClientPartialsSearchResultsTest {

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
        idProperties.put("hidden", "true");
        idProperties.put("type", "number");
        
        List<Map<String,? extends Object>> properties = new ArrayList<Map<String,? extends Object>>();
        properties.add(idProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("properties", properties);
        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_RESULTS));
        TemplateProcessor processor = processorFactory.fromTemplate(new FreemarkerTemplate(templateResource));
        String output = processor.process(root);
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements headers = html.select("table > thead > tr > th");
        assertThat(0, equalTo(headers.size()));
    }
    
    @Test
    public void testGenerateHiddenAndRequiredProperty() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("required", "true");
        idProperties.put("type", "number");
        
        List<Map<String,? extends Object>> properties = new ArrayList<Map<String,? extends Object>>();
        properties.add(idProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("properties", properties);
        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_RESULTS));
        TemplateProcessor processor = processorFactory.fromTemplate(new FreemarkerTemplate(templateResource));
        String output = processor.process(root);
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements headers = html.select("table > thead > tr > th");
        assertThat(0, equalTo(headers.size()));
    }
    
    @Test
    public void testGenerateOneToManyProperty() throws Exception {
        Map<String, String> ordersProperties = new HashMap<String, String>();
        String oneToManyProperty = "orders";
        ordersProperties.put("name", oneToManyProperty);
        ordersProperties.put("type", "java.lang.String");
        ordersProperties.put("n-to-many", "true");
        ordersProperties.put("parameterized-type", "com.example.scaffoldtester.model.StoreOrder");
        ordersProperties.put("type", "java.util.Set");
        ordersProperties.put("simpleType", "StoreOrder");
        
        List<Map<String,? extends Object>> properties = new ArrayList<Map<String,? extends Object>>();
        properties.add(ordersProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("properties", properties);
        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_RESULTS));
        TemplateProcessor processor = processorFactory.fromTemplate(new FreemarkerTemplate(templateResource));
        String output = processor.process(root);
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements headers = html.select("table > thead > tr > th");
        assertThat(0, equalTo(headers.size()));
    }
    
    @Test
    public void testGenerateManyToManyProperty() throws Exception {
        Map<String, String> usersProperties = new HashMap<String, String>();
        String manyToManyProperty = "users";
        usersProperties.put("name", manyToManyProperty);
        usersProperties.put("type", "java.lang.String");
        usersProperties.put("n-to-many", "true");
        usersProperties.put("parameterized-type", "com.example.scaffoldtester.model.UserIdentity");
        usersProperties.put("type", "java.util.Set");
        usersProperties.put("simpleType", "UserIdentity");
        
        List<Map<String,? extends Object>> properties = new ArrayList<Map<String,? extends Object>>();
        properties.add(usersProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("properties", properties);
        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_RESULTS));
        TemplateProcessor processor = processorFactory.fromTemplate(new FreemarkerTemplate(templateResource));
        String output = processor.process(root);
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements headers = html.select("table > thead > tr > th");
        assertThat(0, equalTo(headers.size()));
    }
    
    @Test
    public void testGenerateBasicStringProperty() throws Exception {
        Map<String, String> nameProperties = new HashMap<String, String>();
        String basicStringProperty = "fullName";
        nameProperties.put("name", basicStringProperty);
        nameProperties.put("label", StringUtils.uncamelCase(basicStringProperty));
        nameProperties.put("type", "java.lang.String");
        
        List<Map<String,? extends Object>> properties = new ArrayList<Map<String,? extends Object>>();
        properties.add(nameProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("pluralizedEntityName", "SampleEntities");
        root.put("entityId", basicStringProperty);
        root.put("properties", properties);
        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_RESULTS));
        TemplateProcessor processor = processorFactory.fromTemplate(new FreemarkerTemplate(templateResource));
        String output = processor.process(root);
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements headers = html.select("table > thead > tr > th");
        assertThat(headers.size(), equalTo(1));
        assertThat(headers.text(), equalTo("Full Name"));
        
        Elements resultRows = html.select("table > tbody > tr");
        assertThat(resultRows.attr("ng-repeat"), containsString("result in searchResults"));
        
        Elements resultCells = resultRows.select(" > td");
        assertThat(resultCells.size(), equalTo(1));
        assertThat(resultCells.select("a").attr("href"), equalTo("#/"+"SampleEntities"+ "/edit/{{result.fullName}}"));
        assertThat(resultCells.select("a").text(), equalTo("{{result.fullName}}"));
    }
    
    @Test
    public void testGenerateBasicNumberProperty() throws Exception {
        Map<String, String> ageProperties = new HashMap<String, String>();
        String basicNumberProperty = "age";
        ageProperties.put("name", basicNumberProperty);
        ageProperties.put("label", StringUtils.uncamelCase(basicNumberProperty));
        ageProperties.put("type", "number");
        
        List<Map<String,? extends Object>> properties = new ArrayList<Map<String,? extends Object>>();
        properties.add(ageProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("pluralizedEntityName", "SampleEntities");
        root.put("entityId", basicNumberProperty);
        root.put("properties", properties);
        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_RESULTS));
        TemplateProcessor processor = processorFactory.fromTemplate(new FreemarkerTemplate(templateResource));
        String output = processor.process(root);
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements headers = html.select("table > thead > tr > th");
        assertThat(headers.size(), equalTo(1));
        assertThat(headers.text(), equalTo("Age"));
        
        Elements resultRows = html.select("table > tbody > tr");
        assertThat(resultRows.attr("ng-repeat"), containsString("result in searchResults"));
        
        Elements resultCells = resultRows.select(" > td");
        assertThat(resultCells.size(), equalTo(1));
        assertThat(resultCells.select("a").attr("href"), equalTo("#/"+"SampleEntities"+ "/edit/{{result.age}}"));
        assertThat(resultCells.select("a").text(), equalTo("{{result.age}}"));
    }
    
    @Test
    public void testGenerateBasicDateProperty() throws Exception {
        Map<String, String> dateOfBirthProperties = new HashMap<String, String>();
        String basicDateProperty = "dateOfBirth";
        dateOfBirthProperties.put("name", basicDateProperty);
        dateOfBirthProperties.put("label", StringUtils.uncamelCase(basicDateProperty));
        dateOfBirthProperties.put("type","java.util.Date");
        dateOfBirthProperties.put("datetime-type", "date");
        
        List<Map<String,? extends Object>> properties = new ArrayList<Map<String,? extends Object>>();
        properties.add(dateOfBirthProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("pluralizedEntityName", "SampleEntities");
        root.put("entityId", basicDateProperty);
        root.put("properties", properties);
        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_RESULTS));
        TemplateProcessor processor = processorFactory.fromTemplate(new FreemarkerTemplate(templateResource));
        String output = processor.process(root);
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements headers = html.select("table > thead > tr > th");
        assertThat(headers.size(), equalTo(1));
        assertThat(headers.text(), equalTo("Date Of Birth"));
        
        Elements resultRows = html.select("table > tbody > tr");
        assertThat(resultRows.attr("ng-repeat"), containsString("result in searchResults"));
        
        Elements resultCells = resultRows.select(" > td");
        assertThat(resultCells.size(), equalTo(1));
        assertThat(resultCells.select("a").attr("href"), equalTo("#/"+"SampleEntities"+ "/edit/{{result.dateOfBirth}}"));
        assertThat(resultCells.select("a").text(), equalTo("{{result.dateOfBirth| date:'mediumDate'}}"));
    }
    
    @Test
    public void testGenerateOneToOneProperty() throws Exception {
        Map<String, String> voucherProperties = new HashMap<String, String>();
        String oneToOneProperty = "voucher";
        voucherProperties.put("name", oneToOneProperty);
        voucherProperties.put("label", StringUtils.uncamelCase(oneToOneProperty));
        voucherProperties.put("type", "com.example.scaffoldtester.model.DiscountVoucher");
        voucherProperties.put("one-to-one", "true");
        voucherProperties.put("simpleType", "DiscountVoucher");
        voucherProperties.put("optionLabel", "id");
        
        List<Map<String,? extends Object>> properties = new ArrayList<Map<String,? extends Object>>();
        properties.add(voucherProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("pluralizedEntityName", "SampleEntities");
        root.put("entityId", oneToOneProperty);
        root.put("properties", properties);
        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_RESULTS));
        TemplateProcessor processor = processorFactory.fromTemplate(new FreemarkerTemplate(templateResource));
        String output = processor.process(root);
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements headers = html.select("table > thead > tr > th");
        assertThat(headers.size(), equalTo(1));
        assertThat(headers.text(), equalTo("Voucher"));
        
        Elements resultRows = html.select("table > tbody > tr");
        assertThat(resultRows.attr("ng-repeat"), containsString("result in searchResults"));
        
        Elements resultCells = resultRows.select(" > td");
        assertThat(resultCells.size(), equalTo(1));
        assertThat(resultCells.select("a").attr("href"), equalTo("#/"+"SampleEntities"+ "/edit/{{result.voucher}}"));
        assertThat(resultCells.select("a").text(), equalTo("{{result.voucher.id}}"));
    }
    
    @Test
    public void testGenerateManyToOneProperty() throws Exception {
        Map<String, String> customerProperties = new HashMap<String, String>();
        String manyToOneProperty = "customer";
        customerProperties.put("name", manyToOneProperty);
        customerProperties.put("label", StringUtils.uncamelCase(manyToOneProperty));
        customerProperties.put("type", "com.example.scaffoldtester.model.Customer");
        customerProperties.put("many-to-one", "true");
        customerProperties.put("simpleType", "Customer");
        customerProperties.put("optionLabel", "id");
        
        List<Map<String,? extends Object>> properties = new ArrayList<Map<String,? extends Object>>();
        properties.add(customerProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("pluralizedEntityName", "SampleEntities");
        root.put("entityId", manyToOneProperty);
        root.put("properties", properties);
        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_RESULTS));
        TemplateProcessor processor = processorFactory.fromTemplate(new FreemarkerTemplate(templateResource));
        String output = processor.process(root);
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements headers = html.select("table > thead > tr > th");
        assertThat(headers.size(), equalTo(1));
        assertThat(headers.text(), equalTo("Customer"));
        
        Elements resultRows = html.select("table > tbody > tr");
        assertThat(resultRows.attr("ng-repeat"), containsString("result in searchResults"));
        
        Elements resultCells = resultRows.select(" > td");
        assertThat(resultCells.size(), equalTo(1));
        assertThat(resultCells.select("a").attr("href"), equalTo("#/"+"SampleEntities"+ "/edit/{{result.customer}}"));
        assertThat(resultCells.select("a").text(), equalTo("{{result.customer.id}}"));
    }

}
