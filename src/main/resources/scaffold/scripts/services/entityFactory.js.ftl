<#assign
    angularApp = "${projectId}"
    entityModule = "${entityName?uncap_first}Module"
    entityResource = "${entityName}Resource"
    entityIdJsVar = "${entityName}Id"
>
angular.module('${angularApp}').factory('${entityResource}', function($resource){
    var resource = $resource('${parentDirectories}${resourceRootPath}/${resourcePath}/:${entityIdJsVar}',{${entityIdJsVar}:'@${entityId}'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});