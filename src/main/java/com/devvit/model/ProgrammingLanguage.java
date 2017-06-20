package com.devvit.model;

import com.devvit.jsonsearcher.controller.MainController;
import org.springframework.util.StringUtils;

import java.util.logging.Logger;

/**
 * Created by Vlad on 12.06.2017.
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
        return getName().toLowerCase().contains(str.toLowerCase())
                ||getType().toLowerCase().contains(str.toLowerCase())
                ||getDesignedBy().toLowerCase().contains(str.toLowerCase());
    }

    public boolean equals(ProgrammingLanguage plObj) {
        return getName().toLowerCase().contains(plObj.toString().toLowerCase())
                &&getType().toLowerCase().contains(plObj.toString().toLowerCase())
                &&getDesignedBy().toLowerCase().contains(plObj.toString().toLowerCase());
    }

    public int countWordOccurences(String str) {
        String fullPL = this.toString().toLowerCase();
//        log.info("fullPL="+fullPL);
        int occur = 0;
        String[] s = str.split(" ");
        for (String word: s) {
//            log.info("word="+word);
            occur = occur + StringUtils.countOccurrencesOf(fullPL,word.toLowerCase());
//            log.info("StringUtils.countOccurrencesOf(fullPL,"+word+")="+occur);
        }
        log.info("for "+ getName() + " occur ="+occur);
        return occur;
    }
}
