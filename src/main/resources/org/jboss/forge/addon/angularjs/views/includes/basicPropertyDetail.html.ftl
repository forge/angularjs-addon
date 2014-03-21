<#assign formName="${entityName}Form"
        formProperty = "${formName}.${property.name}"
        modelProperty = "${entityName?uncap_first}.${property.name}"
        propertyLabel = "${property.label}" />
<#if (property.hidden!"false") == "false">
    <div class="form-group" ng-class="{'has-error': ${formProperty}.$invalid}">
        <label for="${property.name}" class="col-sm-2 control-label">${propertyLabel}</label>
        <div id="${property.name}Controls" class="col-sm-10">
            <#if (property["datetime-type"]!"") == "both">
            <datetime id="${property.name}" name="${property.name}"<#rt/>
                <#if (property.required!"false") == "true"> required</#if><#t/>
                <#lt/> ng-model="${modelProperty}" placeholder="Enter the ${entityName} ${propertyLabel}"></datetime>
            <#elseif (property["datetime-type"]!"") == "date">
            <date id="${property.name}" name="${property.name}"<#rt/>
                <#if (property.required!"false") == "true"> required</#if><#t/>
                <#lt/> ng-model="${modelProperty}" placeholder="Enter the ${entityName} ${propertyLabel}"></date>
            <#elseif (property["datetime-type"]!"") == "time">
            <time id="${property.name}" name="${property.name}"<#rt/>
                <#if (property.required!"false") == "true"> required</#if><#t/>
                <#lt/> ng-model="${modelProperty}" placeholder="Enter the ${entityName} ${propertyLabel}"></time>
            <#else>
            <input id="${property.name}" name="${property.name}"<#rt/>
                <#if property.type == "number"> type="number"<#t/>
                    <#if property["minimum-value"]??> min="${property["minimum-value"]}"</#if><#t/>
                    <#if property["maximum-value"]??> max="${property["maximum-value"]}"</#if><#t/>
                <#elseif property.type == "boolean"> type="checkbox"<#t/>
                <#else> type="text"</#if><#t/>
                <#if (property.required!"false") == "true"> required</#if><#t/>
                <#if property["maximum-length"]??> ng-maxlength="${property["maximum-length"]}"</#if><#if property["minimum-length"]??> ng-minlength="${property["minimum-length"]}"</#if><#lt/> class="form-control" ng-model="${modelProperty}" placeholder="Enter the ${entityName} ${propertyLabel}"></input>
            </#if>
            <#if (property.required!) == "true">
            <span class="help-block error" ng-show="${formProperty}.$error.required">required</span> 
            </#if>
            <#if property.type == "number">
            <span class="help-block error" ng-show="${formProperty}.$error.number">not a number</span>
            <#if property["minimum-value"]??>
            <span class="help-block error" ng-show="${formProperty}.$error.min">minimum allowed is ${property["minimum-value"]}</span>
            </#if>
            <#if property["maximum-value"]??>
            <span class="help-block error" ng-show="${formProperty}.$error.max">maximum allowed is ${property["maximum-value"]}</span>
            </#if>
            </#if>
            <#if property["minimum-length"]??>
            <span class="help-block error" ng-show="${formProperty}.$error.minlength">minimum length is ${property["minimum-length"]}</span>
            </#if>
            <#if property["maximum-length"]??>
            <span class="help-block error" ng-show="${formProperty}.$error.maxlength">maximum length is ${property["maximum-length"]}</span>
            </#if>
            <#if (property["datetime-type"]!"") == "both">
            <span class="help-block error" ng-show="${formProperty}.$error.datetimeFormat">does not match format "yyyy-MM-dd hh:mm:ss" (e.g. 2013-12-01 22:00:00)</span>
            </#if>
            <#if (property["datetime-type"]!"") == "date">
            <span class="help-block error" ng-show="${formProperty}.$error.dateFormat">does not match format "yyyy-MM-dd" (e.g. 2013-12-01)</span>
            </#if>
            <#if (property["datetime-type"]!"") == "time">
            <span class="help-block error" ng-show="${formProperty}.$error.timeFormat">does not match format "hh:mm" or "hh:mm:ss" (e.g. 05:00 or 22:00:00)</span>
            </#if>
        </div>
    </div>
</#if>