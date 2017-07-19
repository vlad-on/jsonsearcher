var pageNumber = 1;
var pageSize = 0;
var sortBy = 0;
var value = '';
$(document).keypress(function (e) {
    if (e.which == 13) {
        e.preventDefault();
        searchClick();
    }
});

function searchClick() {
    pageNumber = 1;
    retrievePrLanguages();
}

function retrievePrLanguages() {
    var url = '/ajax';
    value = $('#searchValue').val().trim();
    if (value != '') {
        value = encodeURIComponent(value);
        var uriParams = '';
        if (pageNumber != '') {
            uriParams = appendUriParam(uriParams, 'pageNumber=' + pageNumber);
        }
        if (pageSize != '') {
            uriParams = appendUriParam(uriParams, 'pageSize=' + pageSize);
        }
        if (sortBy != '') {
            uriParams = appendUriParam(uriParams, 'sortBy=' + sortBy);
        }
        url = url + '/' + value + '?' + uriParams;
    }
    $("#resultsBlock").load(url);
}

function appendUriParam(uriParams, param) {
    if (uriParams != ''){
        return uriParams = uriParams + '\u0026' + param;
    } else {
        return uriParams = uriParams + param;
    }
}

function getPreviousPage(pNum) {
    pNum = pNum - 1;
    if (pNum >= 1) {
        selectPage(pNum);
    }
}

function selectPage(pNum) {
    pageNumber = pNum;
    retrievePrLanguages();
}

function getNextPage(pNum, pTotal) {
    pNum = pNum + 1;
    if (!(pNum > parseInt(pTotal))) {
        selectPage(pNum);
    }
}

function setSortBy(){
    var select = document.getElementById("sortBy");
    sortBy = select.selectedIndex;
    searchClick();
}

function resPerPage() {
    var select = document.getElementById("resPerPage");
    pageSize = select.options[select.selectedIndex].value;
    if (pageSize == "All"){
        pageSize = 0;
    }
    searchClick();
}

function onChangeInput() {
    searchClick();
}