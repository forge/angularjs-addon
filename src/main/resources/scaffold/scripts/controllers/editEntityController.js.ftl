<#assign
    angularApp = "${projectId}"
    angularController = "Edit${entityName}Controller"
    angularResource = "${entityName}Resource"
    entityIdJsVar = "${entityName}Id"
    model = "$scope.${entityName?uncap_first}"
    entityRoute = "/${entityName}s"
>

<#assign relatedResources>
<#list properties as property>
<#if (property["many-to-one"]!) == "true" || (property["one-to-one"]!) == "true" || (property["n-to-many"]!) == "true">
, ${property.simpleType}Resource<#t>
</#if>
</#list>
</#assign>

angular.module('${angularApp}').controller('${angularController}', function($scope, $routeParams, $location, ${angularResource} ${relatedResources}) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            ${model} = new ${angularResource}(self.original);
            <#list properties as property>
            <#assign
                relatedResource = "${property.simpleType!}Resource"
                relatedCollection ="$scope.${property.identifier}SelectionList"
                modelProperty = "${model}.${property.name}"
                originalProperty = "self.original.${property.name}"
                reverseId = property["reverse-primary-key"]!>
            <#if (property["many-to-one"]!) == "true" || (property["one-to-one"]!) == "true">
            ${relatedResource}.queryAll(function(items) {
                ${relatedCollection} = $.map(items, function(item) {
                    var wrappedObject = {
                        ${reverseId} : item.${reverseId}
                    };
                    var labelObject = {
                        value : item.${reverseId},
                        text : item.${property.optionLabel}
                    };
                    if(${modelProperty} && item.${reverseId} == ${modelProperty}.${reverseId}) {
                        $scope.${property.identifier}Selection = labelObject;
                        ${modelProperty} = wrappedObject;
                        ${originalProperty} = ${modelProperty};
                    }
                    return labelObject;
                });
            });
            <#elseif (property["n-to-many"]!) == "true">
            ${relatedResource}.queryAll(function(items) {
                ${relatedCollection} = $.map(items, function(item) {
                    var wrappedObject = {
                        ${reverseId} : item.${reverseId}
                    };
                    var labelObject = {
                        value : item.${reverseId},
                        text : item.${property.optionLabel}
                    };
                    if(${modelProperty}){
                        $.each(${modelProperty}, function(idx, element) {
                            if(item.${reverseId} == element.${reverseId}) {
                                $scope.${property.name}Selection.push(labelObject);
                                ${modelProperty}.push(wrappedObject);
                            }
                        });
                        ${originalProperty} = ${modelProperty};
                    }
                    return labelObject;
                });
            });
            </#if>
            </#list>
        };
        var errorCallback = function() {
            $location.path("${entityRoute}");
        };
        ${angularResource}.get({${entityIdJsVar}:$routeParams.${entityIdJsVar}}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, ${model});
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        ${model}.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("${entityRoute}");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("${entityRoute}");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        ${model}.$remove(successCallback, errorCallback);
    };
    
    <#list properties as property>
    <#if (property["many-to-one"]!) == "true" || (property["one-to-one"]!) == "true">
    <#assign
            modelProperty = "${model}.${property.name}"
            selectedItem="${property.identifier}Selection"
            reverseId = property["reverse-primary-key"]!>
    $scope.$watch("${selectedItem}", function(selection) {
        if (typeof selection != 'undefined') {
            ${modelProperty} = {};
            ${modelProperty}.${reverseId} = selection.value;
        }
    });
    <#elseif (property["n-to-many"]!"false") == "true">
    <#assign
            modelProperty = "${model}.${property.name}"
            selectedItem="${property.identifier}Selection"
            reverseId = property["reverse-primary-key"]!>
    $scope.${selectedItem} = $scope.${selectedItem} || [];
    $scope.$watch("${selectedItem}", function(selection) {
        if (typeof selection != 'undefined' && ${model}) {
            ${modelProperty} = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.${reverseId} = selectedItem.value;
                ${modelProperty}.push(collectionItem);
            });
        }
    });
    <#elseif property["lookup"]??>
    <#assign
            lookupCollection = "$scope.${property.identifier}List">
    ${lookupCollection} = [
    <#list property["lookup"]?split(",") as option>
        "${option}"<#if option_has_next>,</#if>  
    </#list>
    ];
    </#if>
    </#list>
    
    $scope.get();
});