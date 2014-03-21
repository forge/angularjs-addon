<#assign formName="${entityName}Form"
        formProperty = "${formName}.${property.name}"
        modelProperty = "${entityName?uncap_first}.${property.name}"
        propertyLabel = "${property.label}"
        collectionVar = "${property.name?substring(0, 1)}"
        collection = "${property.identifier}List">
<#if (property.hidden!"false") == "false">
    <div class="form-group" ng-class="{'has-error': ${formProperty}.$invalid}">
        <label for="${property.name}" class="col-sm-2 control-label">${propertyLabel}</label>
        <div id="${property.name}Controls" class="col-sm-10">
        <select id="${property.name}" name="${property.name}" class="form-control" ng-model="${modelProperty}" ng-options="${collectionVar} for ${collectionVar} in ${collection}" <#if (property.required!) == "true">required</#if> >
            <option value="">Choose a ${propertyLabel}</option>
        </select>
        <#if (property.required!) == "true">
        <span class="help-block error" ng-show="${formProperty}.$error.required">required</span> 
        </#if>
        </div>
    </div>
</#if>