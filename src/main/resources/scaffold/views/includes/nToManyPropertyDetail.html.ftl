<#assign formName="${entityName}Form"
        formProperty = "${formName}.${property.name}"
        modelProperty = "${property.identifier}Selection"
        propertyLabel = "${property.label}"
        collectionVar = "${property.name?substring(0, 1)}"
        collection = "${property.identifier}SelectionList">
<#if (property.hidden!"false") == "false">
    <div class="form-group" ng-class="{'has-error': ${formProperty}.$invalid}">
        <label for="${property.name}" class="col-sm-2 control-label">${propertyLabel}</label>
        <div id="${property.name}Controls" class="col-sm-10">
            <select id="${property.name}" name="${property.name}" multiple class="form-control" ng-model="${property.identifier}Selection" ng-options="${collectionVar}.text for ${collectionVar} in ${collection}">
                <option value="">Choose a ${propertyLabel}</option>
            </select>
        </div>
    </div>
</#if>