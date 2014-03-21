<#assign propertyLabel = "${property.label}"
    collectionVar = "${property.name?substring(0, 1)}"
    collection = "${property.identifier}List">
<#-- Display only non-hidden singular properties as search criteria. Omit collections and date/time properties -->
<#if (property.hidden!"false") != "true" && (property["n-to-many"]!"false") != "true" && (property["temporal"]!"false") != "true">
    <div class="form-group">
        <label for="${property.name}" class="col-sm-2 control-label">${propertyLabel}</label>
        <div class="col-sm-10">
            <#if (property["many-to-one"]!"false") == "true" || (property["one-to-one"]!"false") == "true">
            <select id="${property.name}" name="${property.name}" class="form-control" ng-model="search.${property.name}" ng-options="${collectionVar} as ${collectionVar}.${property.optionLabel} for ${collectionVar} in ${collection}">
                <option value="">Choose a ${propertyLabel}</option>
            </select>
            <#elseif property["lookup"]??>
            <select id="${property.name}" name="${property.name}" class="form-control" ng-model="search.${property.name}" ng-options="${collectionVar} as ${collectionVar} for ${collectionVar} in ${collection}">
                <option value="">Choose a ${propertyLabel}</option>
            </select>
            <#else>
            <input id="${property.name}" name="${property.name}" class="form-control" type="text" ng-model="search.${property.name}" placeholder="Enter the ${entityName} ${propertyLabel}"></input>
            </#if>
        </div>
    </div>
</#if>