<div class="form-horizontal">
    <h3>Create a new ${entityName}</h3>
    <div class="form-group">
        <div class="col-md-offset-2 col-sm-2">
            <a id="Create" name="Create" class="btn btn-primary" href="#/${entityName}s/new"><span class="glyphicon glyphicon-plus-sign"></span> Create</a>
        </div>
    </div>
</div>
<hr/>
<div>
    <h3>Search for ${entityName}s</h3>
    <form id="${entityName}Search" class="form-horizontal">
        <#list properties as property>
        <#include "includes/searchFormInput.html.ftl">
        </#list>
        <div class="form-group">
            <div class="col-md-offset-2 col-sm-10">
                <a id="Search" name="Search" class="btn btn-primary" ng-click="performSearch()"><span class="glyphicon glyphicon-search"></span> Search</a>
            </div>
        </div>
    </form>
</div>
<div id="search-results">
    <#include "includes/searchResults.html.ftl">
    <#include "includes/searchResultsPaginator.html.ftl">
</div>