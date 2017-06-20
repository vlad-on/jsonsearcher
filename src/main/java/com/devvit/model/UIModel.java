package com.devvit.model;

import java.util.Collection;

/**
 * Created by vaveryanov on 19.06.2017.
 */
public class UIModel {
    private Collection<ProgrammingLanguage> resultCollection;
    private int pageCount;
    private int pageNumber;

    public Collection<ProgrammingLanguage> getResultCollection() {
        return resultCollection;
    }

    public void setResultCollection(Collection<ProgrammingLanguage> resultCollection) {
        this.resultCollection = resultCollection;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
