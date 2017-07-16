package com.devvit.model;

import com.devvit.jsonsearcher.controller.MainController;
import org.springframework.util.StringUtils;

import java.util.logging.Logger;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

/**
 * Stores info about one Programming Language from the file
 */
public class ProgrammingLanguage {

    private static Logger log = Logger.getLogger(MainController.class.getName());

    private String name;
    private String type;
    private String designedBy;

    public ProgrammingLanguage() {
    }

    public ProgrammingLanguage(String name, String type, String designedBy) {
        this.name = name;
        this.type = type;
        this.designedBy = designedBy;
    }

    public String getName() {
        return name;
    }

    public String getDesignedBy() {
        return designedBy;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "{\n" +
                "    \"Name\":" + getName() + ",\n" +
                "    \"Type\":" + getType() + ",\n" +
                "    \"Designed by\":" + getDesignedBy() + "\n" +
                "}";
    }

    public boolean contains(String str) {
//        return getName().toLowerCase().contains(str.toLowerCase())
//                || getType().toLowerCase().contains(str.toLowerCase())
//                || getDesignedBy().toLowerCase().contains(str.toLowerCase());
        return containsIgnoreCase(getName(), str)
                || containsIgnoreCase(getType(), str)
                || containsIgnoreCase(getDesignedBy(), str);
    }

    public int countWordOccurrences(String str) {
        String fullPL = this.toString().toLowerCase();
//        log.info("fullPL="+fullPL);
        int occur = 0;
        String[] s = str.split(" ");
        for (String word: s) {
//            log.info("word="+word);
            occur = occur + StringUtils.countOccurrencesOf(fullPL,word.toLowerCase());
//            log.info("StringUtils.countOccurrencesOf(fullPL,"+word+")="+occur);
        }
        log.info("for lang "+ getName() + " number occurrences = "+occur);
        return occur;
    }
}
