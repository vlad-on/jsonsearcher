package com.devvit.model;

import java.util.Comparator;

public class PrLangByNameComparer implements Comparator<ProgrammingLanguage> {

    @Override
    public int compare(ProgrammingLanguage x, ProgrammingLanguage y) {
        if (x != null && y != null) {
            return x.getName().compareTo(y.getName());
        } else {
            return 0;
        }
    }
}
