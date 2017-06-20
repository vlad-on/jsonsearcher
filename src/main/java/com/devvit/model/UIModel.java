package com.devvit.model;

import java.util.Collection;

/**
 * Created by vaveryanov on 19.06.2017.
 */
public class UIModel {
    private Collection<ProgrammingLanguage> resultCollection;
    private int pagesTotal;
    private int pageNumber;

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
}
