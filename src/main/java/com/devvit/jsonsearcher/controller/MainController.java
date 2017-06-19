package com.devvit.jsonsearcher.controller;

import com.devvit.model.ProgrammingLanguage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Vlad on 12.06.2017.
 */
@RestController
@RequestMapping("/")
@PropertySource("application.properties")
public class MainController {

    private static Logger log = Logger.getLogger(MainController.class.getName());

    private static Set<ProgrammingLanguage> prLangSet;

    private static void initializeData() {
        log.info("Started data initialization");
        prLangSet = new LinkedHashSet <>();
        JsonParser parser = new JsonParser();
        JsonArray jsonPrLangList;
        try {
            File file = new File("src/main/resources/data/data.json");
            jsonPrLangList = parser.parse(new FileReader(file)).getAsJsonArray();

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
        log.fine("Data successfully initialized");
    }

    @RequestMapping
    public ModelAndView homePage() {
        log.info("entered homePage() by / mapping");
        initializeData();
        return new ModelAndView("ajax", "message", "Spring MVC with Ajax and JQuery ");
    }

    @RequestMapping(value = "/ajax/{inputToSearch}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView getProgLangSearch(@PathVariable String inputToSearch) {
        log.info("entered getProgLangSearch() by /ajax/" + inputToSearch + " mapping");
        //initializeData(); // should be at the start of program
        String toSearch = "";
        String toIgnore = "";
        String[] s = inputToSearch.split(" ");
        for (String word: s) {
            if (word.length()>1) {
                if (word.substring(0, 1).equals("-")) {
                    toIgnore = toIgnore + word.substring(1, word.length()) + " ";
                } else {
                    toSearch = toSearch + word + " ";
                }
            }
        }
        //remove last " "
        toSearch = toSearch.trim();
        toIgnore = toIgnore.trim();

        log.info("Values to search = "+toSearch);
        log.info("Values to ignore = "+toIgnore);
        //new list
        Set<ProgrammingLanguage> resultSet = new LinkedHashSet <>();
        //find exact (and partial) values and add to the list
        addExactAndPartialMatch(toSearch, resultSet);
        //find values with words in toSearch (swapped)
        addWordSwappedMatch(toSearch, resultSet);
        //find related like Scripting=script
        addRelatedScriptingLanguages(toSearch, resultSet);
        //exclude values with "-..."
        excludeValuesWithMinus(toIgnore, resultSet);
        //return the list (prettify)
        //sort by relevance

        return new ModelAndView("searchresult", "prLanguages", resultSet);
    }

    //toSearch - value to be match with,
    //resultSet - Set where matched values to be added
    private void addExactAndPartialMatch(@PathVariable String toSearch, Set<ProgrammingLanguage> resultSet) {
        log.info("Entered addExactAndPartialMatch() for word(s): " + toSearch);
        for (ProgrammingLanguage elem : prLangSet){
//            System.out.println("elem.getName()="+elem.getName()+".contains(toSearch)="+toSearch+" is "+elem.contains(toSearch));
//            System.out.println("elem.getType()="+elem.getType()+".contains(toSearch)="+toSearch+" is "+elem.contains(toSearch));
            if (elem.contains(toSearch)){
                resultSet.add(elem);
                log.fine("  added " + elem.getName());
            }
        }
        log.info(" In general there are "+resultSet.size()+" elements in the set");
        log.fine("All ExactAndPartial matched results added");
//        System.out.println("after addExactAndPartialMatch");
//        for (ProgrammingLanguage pl: resultSet) {
//            System.out.println(pl.getName());
//        }
    }

    //slice toSearch on separate words and find matches in resultSet with each of the words,
    //then filter for matches with all words from toSearch
    //works only if toSearch contains more than 1 word
    private void addWordSwappedMatch(String toSearch, Set<ProgrammingLanguage> resultSet) {
        log.info("Entered addWordSwappedMatch for word(s): " + toSearch);
        //slice to separate words
        String[] wordsToSearch = toSearch.split(" ");
        //find matches with each word
        //check if words >1
        if (wordsToSearch.length > 1) {
            //result set for all matches for each word
            Set<ProgrammingLanguage> resultMatchByWordsSet = new LinkedHashSet<>();
            //initialize 2 new sets - we need them to compare results for each word and find only common for both sets
            Set<ProgrammingLanguage> matchByWordsSet = new LinkedHashSet<>();
            //adding matches set for first word
            addExactAndPartialMatch(wordsToSearch[0],resultMatchByWordsSet);
            //searches all matches for each word from toSearch string
            for (int i=1; i<wordsToSearch.length; i++){
                //pass to existing match method each word and adds to this new set
                addExactAndPartialMatch(wordsToSearch[i],matchByWordsSet);
                //this method removes all objects from resultMatchByWordsSet that are not in matchByWordsSet
                //so it leaves in resultMatchByWordsSet only common objects with matchByWordsSet
                resultMatchByWordsSet.retainAll(matchByWordsSet);
            }
            //adding results of above search to general result set
            //as the result set is Set - it won't add same elements and as it it is Linked - results with swapped words
            // added in the end - you might consider them as related
            resultSet.addAll(resultMatchByWordsSet);
            log.fine("All WordSwapped matched results added");
        } else {
            log.info("There is only one word to search, no new results added");
        }
    }

    //if toSearch contains word scriprting - change it to script and use addWordSwappedMatch(...)
    private void addRelatedScriptingLanguages(String toSearch, Set<ProgrammingLanguage> resultSet) {
        log.info("Entered addRelatedScriptingLanguages for word(s): " + toSearch);
        if (toSearch.toLowerCase().contains("scripting")) {
            String replacedSearch = toSearch.replace("scripting","script");
            log.info("Replaced Scripting to script");
            addWordSwappedMatch(replacedSearch, resultSet);
        } else {
            log.info("No word Scripting found, no new results added");
        }
    }

    //if toSearch contains words staring with "-" exclude them from resultSet
    //might be not optimal
    private void excludeValuesWithMinus(String toIgnore, Set<ProgrammingLanguage> resultSet) {
        log.info("Entered excludeValuesWithMinus for word(s): " + toIgnore);
        if (toIgnore.length()>0) {
            log.info("there are results to exclude");
            //get all results with such words to new HashSet
            Set<ProgrammingLanguage> plSetToIgnore = new HashSet<>();
            addExactAndPartialMatch(toIgnore, plSetToIgnore);
            addWordSwappedMatch(toIgnore, plSetToIgnore);
            //delete results with such words from resultSet
            resultSet.removeAll(plSetToIgnore);
            log.fine("Excluded all results containing " + toIgnore);
        } else {
            log.info("There are no values to ignore, no results excluded");
        }
    }

//json prettify
//        String result = "";
//        for (ProgrammingLanguage elem:
//                resultSet) {
//            result = result + elem.toString() + ",\n";
//        }
//        result = "[" + result.substring(0, result.length()-2) + "]";
}
