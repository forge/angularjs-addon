package org.jboss.forge.addon.angularjs.tests.freemarker;

import java.io.File;

import org.jboss.forge.addon.angularjs.AngularScaffoldProvider;
import org.jboss.forge.addon.angularjs.matchers.InspectionResultMatcher;
import org.jboss.forge.arquillian.archive.ForgeArchive;
import org.jboss.forge.furnace.repositories.AddonDependencyEntry;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class Deployments {

	public static ForgeArchive getDeployment() {
		return ShrinkWrap
				.create(ForgeArchive.class)
				.addClass(Deployments.class)
                .addClass(InspectionResultMatcher.class)
				.addAsLibrary(Maven.resolver().resolve("org.jsoup:jsoup:1.7.1").withTransitivity().asSingleFile())
				.addPackage(AngularScaffoldProvider.class.getPackage())
				.addAsResources(Deployments.BASE_PACKAGE,
						Deployments.NEW_ENTITY_CONTROLLER_JS,
						Deployments.EDIT_ENTITY_CONTROLLER_JS,
						Deployments.SEARCH_ENTITY_CONTROLLER_JS,
						Deployments.ENTITY_FACTORY, Deployments.DETAIL_VIEW,
						Deployments.SEARCH_VIEW,
						Deployments.SEARCH_RESULTS_PAGINATOR_INCLUDE,
						Deployments.SEARCH_RESULTS,
						Deployments.SEARCH_FORM_INPUT,
						Deployments.BASIC_PROPERTY_DETAIL_INCLUDE,
						Deployments.LOOKUP_PROPERTY_DETAIL_INCLUDE,
						Deployments.N_TO_ONE_PROPERTY_DETAIL_INCLUDE,
						Deployments.N_TO_MANY_PROPERTY_DETAIL_INCLUDE,
						Deployments.INDEX_PAGE,
						Deployments.APP_JS)
				.addBeansXML()
				.addAsAddonDependencies(
						AddonDependencyEntry
								.create("org.jboss.forge.furnace.container:cdi"),
						AddonDependencyEntry
								.create("org.jboss.forge.addon:scaffold-spi"),
						AddonDependencyEntry
								.create("org.jboss.forge.addon:javaee"),
						AddonDependencyEntry
								.create("org.jboss.forge.addon:templates"),
						AddonDependencyEntry
								.create("org.jboss.forge.addon:text"),
						AddonDependencyEntry
								.create("org.jboss.forge.addon:convert"),
						AddonDependencyEntry
								.create("org.jboss.forge.addon:parser-java"));
	}

	public static final Package BASE_PACKAGE = AngularScaffoldProvider.class
			.getPackage();
	public static final String BASE_PACKAGE_PATH = File.separator
			+ BASE_PACKAGE.getName().replace('.', File.separatorChar)
			+ File.separator;
	public static final String NEW_ENTITY_CONTROLLER_JS = "scripts/controllers/newEntityController.js.ftl";
	public static final String EDIT_ENTITY_CONTROLLER_JS = "scripts/controllers/editEntityController.js.ftl";
	public static final String SEARCH_ENTITY_CONTROLLER_JS = "scripts/controllers/searchEntityController.js.ftl";
	public static final String ENTITY_FACTORY = "scripts/services/entityFactory.js.ftl";
	public static final String DETAIL_VIEW = "views/detail.html.ftl";
	public static final String SEARCH_VIEW = "views/search.html.ftl";
	public static final String SEARCH_RESULTS_PAGINATOR_INCLUDE = "views/includes/searchResultsPaginator.html.ftl";
	public static final String SEARCH_RESULTS = "views/includes/searchResults.html.ftl";
	public static final String SEARCH_FORM_INPUT = "views/includes/searchFormInput.html.ftl";
	public static final String BASIC_PROPERTY_DETAIL_INCLUDE = "views/includes/basicPropertyDetail.html.ftl";
	public static final String LOOKUP_PROPERTY_DETAIL_INCLUDE = "views/includes/lookupPropertyDetail.html.ftl";
	public static final String N_TO_ONE_PROPERTY_DETAIL_INCLUDE = "views/includes/nToOnePropertyDetail.html.ftl";
	public static final String N_TO_MANY_PROPERTY_DETAIL_INCLUDE = "views/includes/nToManyPropertyDetail.html.ftl";
	public static final String INDEX_PAGE = "index.html.ftl";
	public static final String APP_JS = "scripts/app.js.ftl";

}
