    <div class="table-responsive">
        <table class="table table-responsive table-bordered table-striped clearfix">
            <thead>
                <tr>
                <#list properties as property><#t>
                <#if (property.hidden!"false") != "true">
                    <#if (property["n-to-many"]!"false") != "true">
                    <#-- Display only singular properties for now. Exclude collections as they cannot be displayed "meaningfully". -->
                    <th>${property.label}</th>
                    </#if>
                </#if>
                </#list>
                </tr>
            </thead>
            <tbody id="search-results-body">
                <tr ng-repeat="result in searchResults | searchFilter:searchResults | startFrom:currentPage*pageSize | limitTo:pageSize">
                <#list properties as property>
                <#if (property.hidden!"false") != "true">
                    <#if (property["many-to-one"]!"false") == "true" || (property["one-to-one"]!"false") == "true">
                        <#if (property["option-label-temporal-type"]!"") == "date">
                        <td><a href="#/${entityName}s/edit/{{result.${entityId}}}">{{result.${property.name}.${property.optionLabel}| date:'mediumDate'}}</a></td>
                        <#elseif (property["option-label-temporal-type"]!"") == "time">
                        <td><a href="#/${entityName}s/edit/{{result.${entityId}}}">{{result.${property.name}.${property.optionLabel}| date:'mediumTime'}}</a></td>
                        <#elseif (property["option-label-temporal-type"]!"") == "both">
                        <td><a href="#/${entityName}s/edit/{{result.${entityId}}}">{{result.${property.name}.${property.optionLabel}| date:'medium'}}</a></td>
                        <#else>
                        <td><a href="#/${entityName}s/edit/{{result.${entityId}}}">{{result.${property.name}.${property.optionLabel}}}</a></td>
                        </#if>
                    <#elseif (property["n-to-many"]!"false") == "true">
                    <#-- Do nothing. We won't allow for display of collection properties in search results for now. -->
                    <#elseif (property["datetime-type"]!"") == "date">
                    <td><a href="#/${entityName}s/edit/{{result.${entityId}}}">{{result.${property.name}| date:'mediumDate'}}</a></td>
                    <#elseif (property["datetime-type"]!"") == "time">
                    <td><a href="#/${entityName}s/edit/{{result.${entityId}}}">{{result.${property.name}| date:'mediumTime'}}</a></td>
                    <#elseif (property["datetime-type"]!"") == "both">
                    <td><a href="#/${entityName}s/edit/{{result.${entityId}}}">{{result.${property.name}| date:'yyyy-MM-dd HH:mm:ss Z'}}</a></td>
                    <#else>
                    <td><a href="#/${entityName}s/edit/{{result.${entityId}}}">{{result.${property.name}}}</a></td>
                    </#if>
                </#if>
                </#list>
                </tr>
            </tbody>
        </table>
    </div>
