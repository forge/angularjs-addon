<!DOCTYPE html>
<html lang="en" ng-app="${projectId}">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>${projectTitle}</title>
    <link href="styles/bootstrap.css" rel="stylesheet" media="screen">
    <link href="styles/bootstrap-theme.css" rel="stylesheet" media="screen">
    <link href="styles/main.css" rel="stylesheet" media="screen">
</head>
<body>
    <div id="wrap">
    	
    	<div class="navbar navbar-inverse navbar-fixed-top">
			<div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="offcanvas">
                      <span class="sr-only">Toggle navigation</span>
                      <span class="icon-bar"></span>
                      <span class="icon-bar"></span>
                      <span class="icon-bar"></span>
                    </button>
				    <a class="navbar-brand" href="#">${projectTitle}</a>
			    </div>
			</div>
    	</div>
        
        <div class="container">
            <div class="row row-offcanvas row-offcanvas-left">
                <!-- sidebar -->
                <div class="col-xs-6 col-sm-3 well sidebar-offcanvas">
                    <a href="app.html">
                        <img class="hidden-xs img-responsive" src="img/forge-logo.png" alt="JBoss Forge"></img>
                    </a>
                    <nav class="sidebar-nav" ng-controller="NavController" role="navigation">
                        <div id="sidebar-entries" class="list-group">
                            <#list entityNames as entityName>
                        	<a class="list-group-item" ng-class="{active: matchesRoute('${entityName}s')}" href="#/${entityName}s" data-toggle="offcanvas">${entityName}s</a>
                        	</#list>
                        </div>
                    </nav>
                </div>
                <!-- main area-->
                <div class="col-sm-offset-1 col-xs-12 col-sm-8 well mainarea">
                    <div id="main" ng-view>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <div id="footer">
        <div class="container">
            <p>Powered by <a href="http://jboss.org/forge">Forge</a></p>
            <p><a href="http://glyphicons.com">Glyphicons Free</a> licensed under <a href="http://creativecommons.org/licenses/by/3.0/">CC BY 3.0</a>.</p>
        </div>
    </div>
    
    <script src="scripts/vendor/modernizr-2.6.2.min.js"></script>
    <script src="scripts/vendor/jquery-1.9.1.js"></script>
    <script src="scripts/vendor/bootstrap.js"></script>
    <script src="scripts/vendor/angular.js"></script>
    <script src="scripts/vendor/angular-route.js"></script>
    <script src="scripts/vendor/angular-resource.js"></script>
    <script src="scripts/app.js"></script>
    <script src="scripts/offcanvas.js"></script>
    <script src="scripts/directives/datepicker.js"></script>
    <script src="scripts/directives/timepicker.js"></script>
    <script src="scripts/directives/datetimepicker.js"></script>
    <script src="scripts/filters/startFromFilter.js"></script>
    <script src="scripts/filters/genericSearchFilter.js"></script>
    <script src="scripts/services/locationParser.js"></script>
    <#list entityNames as entityName>
    <script src="scripts/services/${entityName}Factory.js"></script>
    <script src="scripts/controllers/new${entityName}Controller.js"></script>
    <script src="scripts/controllers/search${entityName}Controller.js"></script>
    <script src="scripts/controllers/edit${entityName}Controller.js"></script>
    </#list>
</body>
</html>