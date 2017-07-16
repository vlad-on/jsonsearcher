package com.devvit.jsonsearcher.repository;

import com.devvit.jsonsearcher.controller.MainController;
import com.devvit.model.ProgrammingLanguage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Vlad on 16.07.2017.
 */
@Repository
//@PropertySource("application.properties")
public class MainRepository {

    private static Logger log = Logger.getLogger(MainController.class.getName());

    private static File dataFile = new File("src/main/resources/data/data.json"); //may use application.properties file
    private static boolean isDataInitialized;
    private static Set<ProgrammingLanguage> prLangSet;

    public static Set<ProgrammingLanguage> getPrLangSet() {
        initializeData();
        return prLangSet;
    }

    private static void initializeData() {
        if (!isDataInitialized) {
            log.info("Started data initialization");
            prLangSet = new LinkedHashSet<>();
            JsonParser parser = new JsonParser();
            JsonArray jsonPrLangList;
            try {
                jsonPrLangList = parser.parse(new FileReader(dataFile)).getAsJsonArray();

                for (JsonElement jsonPrLang : jsonPrLangList) {
                    JsonObject j = parser.parse(jsonPrLang.toString()).getAsJsonObject();
                    String name = j.get("Name").getAsString();
                    String type = j.get("Type").getAsString();
                    String designedBy = j.get("Designed by").getAsString();
                    ProgrammingLanguage prLang = new ProgrammingLanguage(name, type, designedBy);
                    prLangSet.add(prLang);
                }
            } catch (FileNotFoundException e) {
                log.log(Level.SEVERE, "Exception: ", e);
            }
            isDataInitialized = true;
            log.fine("Data successfully initialized");
        } else {
            log.info("Data already initialized");
        }
    }
}
