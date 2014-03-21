    <ul class="pagination pagination-centered">
        <li ng-class="{disabled:currentPage == 0}">
            <a id="prev" href ng-click="previous()">«</a>
        </li>
        <li ng-repeat="n in pageRange" ng-class="{active:currentPage == n}" ng-click="setPage(n)">
            <a href ng-bind="n + 1">1</a>
        </li>
        <li ng-class="{disabled: currentPage == (numberOfPages() - 1)}">
            <a id="next" href ng-click="next()">»</a>
        </li>
    </ul>
