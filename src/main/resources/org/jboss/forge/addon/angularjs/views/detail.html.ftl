<#assign formName = "${entityName}Form"
        model = "${entityName?uncap_first}">
<header ng-switch on="$location.path().indexOf('/${pluralizedEntityName}/new') > -1">
    <h3 ng-switch-when="true">Create a new ${entityName}</h3>
    <h3 ng-switch-when="false">View or edit ${entityName}</h3>
</header>
<form id="${formName}" name="${formName}" class="form-horizontal" role="form">
    ${formProperties}
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button id="save${entityName}" name="save${entityName}" class="btn btn-primary" ng-disabled="isClean() || ${formName}.$invalid" ng-click="save()"><span class="glyphicon glyphicon-ok-sign"></span> Save</button>
            <button id="cancel" name="cancel" class="btn btn-default" ng-click="cancel()"><span class="glyphicon glyphicon-remove-sign"></span> Cancel</button>
            <button id="delete${entityName}" name="delete${entityName}" class="btn btn-danger" ng-show="${model}.${entityId}" ng-click="remove()"><span class="glyphicon glyphicon-trash"></span> Delete</button>
        </div>
    </div>
</form>