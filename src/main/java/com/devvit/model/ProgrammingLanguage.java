package com.devvit.model;

/**
 * Created by Vlad on 12.06.2017.
 */
public class ProgrammingLanguage {
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

    public boolean contains(Object obj) {
        return getName().toLowerCase().contains(obj.toString().toLowerCase())
                ||getType().toLowerCase().contains(obj.toString().toLowerCase())
                ||getDesignedBy().toLowerCase().contains(obj.toString().toLowerCase());
    }

    public boolean equals(ProgrammingLanguage plObj) {
        return getName().equals(plObj.getName())
                &&getType().equals(plObj.getType())
                &&getDesignedBy().equals(plObj.getDesignedBy());
    }
}
