<#assign formName = "${entityName}Form"
        model = "${entityName?uncap_first}">
<header ng-switch on="$location.path().indexOf('/${entityName}s/new') > -1">
    <h3 ng-switch-when="true">Create a new ${entityName}</h3>
    <h3 ng-switch-when="false">View or edit ${entityName}</h3>
</header>
<form id="${formName}" name="${formName}" class="form-horizontal" role="form">
    <div ng-show="displayError" class="alert alert-danger">
        <strong>Error!</strong> Something broke. Retry, or cancel and start afresh.
    </div>
    <#list properties as property>
        <#if (property["many-to-one"]!) == "true" || (property["one-to-one"]!) == "true">
        <#include "includes/nToOnePropertyDetail.html.ftl" >
        <#elseif (property["n-to-many"]!) == "true">
        <#include "includes/nToManyPropertyDetail.html.ftl" >
        <#elseif property["lookup"]??>
        <#include "includes/lookupPropertyDetail.html.ftl" >
        <#else>
        <#include "includes/basicPropertyDetail.html.ftl" >
        </#if>
    </#list>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button id="save${entityName}" name="save${entityName}" class="btn btn-primary" ng-disabled="isClean() || ${formName}.$invalid" ng-click="save()"><span class="glyphicon glyphicon-ok-sign"></span> Save</button>
            <button id="cancel" name="cancel" class="btn btn-default" ng-click="cancel()"><span class="glyphicon glyphicon-remove-sign"></span> Cancel</button>
            <button id="delete${entityName}" name="delete${entityName}" class="btn btn-danger" ng-show="${model}.${entityId}" ng-click="remove()"><span class="glyphicon glyphicon-trash"></span> Delete</button>
        </div>
    <div>
</form>