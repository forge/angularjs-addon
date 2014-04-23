/**
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
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
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertThat;

/**
 * Tests to verify that the generated HTML the paginator in the search page is generated correctly.
 */
@RunWith(Arquillian.class)
public class FreemarkerClientPartialsSearchResultsPaginatorTest {

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
    
    private static String PAGINATOR_OUTPUT = "    <ul class=\"pagination pagination-centered\">\n" + 
    	    "        <li ng-class=\"{disabled:currentPage == 0}\">\n" + 
    		"            <a id=\"prev\" href ng-click=\"previous()\">«</a>\n" + 
    		"        </li>\n" + 
    		"        <li ng-repeat=\"n in pageRange\" ng-class=\"{active:currentPage == n}\" ng-click=\"setPage(n)\">\n" + 
    		"            <a href ng-bind=\"n + 1\">1</a>\n" + 
    		"        </li>\n" + 
    		"        <li ng-class=\"{disabled: currentPage == (numberOfPages() - 1)}\">\n" + 
    		"            <a id=\"next\" href ng-click=\"next()\">»</a>\n" + 
    		"        </li>\n" + 
    		"    </ul>\n";
    
    @Test
    public void testGenerateSearchResultsPaginator() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("type", "number");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", idProperties);
        Resource<URL> templateResource = resourceFactory.create(getClass().getResource(Deployments.BASE_PACKAGE_PATH + Deployments.SEARCH_RESULTS_PAGINATOR_INCLUDE));
        Template processor = processorFactory.create(templateResource, FreemarkerTemplate.class);
        String output = processor.process(root);
        assertThat(output, IsEqual.equalTo(PAGINATOR_OUTPUT));
    }

}
