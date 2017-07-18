package com.devvit.model;

import java.util.Collection;

/**
 * Helper class for implementing pagination
 */
public class UIModel {
    private Collection<ProgrammingLanguage> resultCollection;
    private int pagesTotal;
    private int pageNumber;
    private int elementsPerPage;
    private String previousUrl;

    public Collection<ProgrammingLanguage> getResultCollection() {
        return resultCollection;
    }

    public void setResultCollection(Collection<ProgrammingLanguage> resultCollection) {
        this.resultCollection = resultCollection;
    }

    public int getPagesTotal() {
        return pagesTotal;
    }

    public void setPagesTotal(int pagesTotal) {
        this.pagesTotal = pagesTotal;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPreviousUrl() {
        return previousUrl;
    }

    public void setPreviousUrl(String previousUrl) {
        this.previousUrl = previousUrl;
    }

    public int getElementsPerPage() {
        return elementsPerPage;
    }

    public void setElementsPerPage(int elementsPerPage) {
        this.elementsPerPage = elementsPerPage;
    }
}
