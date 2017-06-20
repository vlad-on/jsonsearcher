package com.devvit.jsonsearcher.service;

import com.devvit.jsonsearcher.controller.MainController;
import com.devvit.model.ProgrammingLanguage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vaveryanov on 20.06.2017.
 */
public class SearchEngineService {

    private static Logger log = Logger.getLogger(MainController.class.getName());

    private static Set<ProgrammingLanguage> prLangSet;
    private static boolean isDataInitialized;

    public static void initializeData() {
        if (!isDataInitialized) {
            log.info("Started data initialization");
            prLangSet = new LinkedHashSet<>();
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
            isDataInitialized = true;
            log.fine("Data successfully initialized");
        } else {
            log.info("Data already initialized");
        }
    }

    private void sortByRelevance(String toSearch, List<ProgrammingLanguage> sortedResultList) {
        log.info("Entered sortByRelevance with for word(s):" + toSearch);
        //might use tree set
        Collections.sort(sortedResultList, (a, b) -> a.countWordOccurrences(toSearch) > b.countWordOccurrences(toSearch) ? -1 :
                a.countWordOccurrences(toSearch) == b.countWordOccurrences(toSearch) ? 0 : 1);
        log.fine("List sorted by word relevance");
    }

    //toSearch - value to be match with,
    //resultSet - Set where matched values to be added
    private void addExactAndPartialMatch(@PathVariable String toSearch, Set<ProgrammingLanguage> resultSet) {
        log.info("Entered addExactAndPartialMatch() for word(s): " + toSearch);
        for (ProgrammingLanguage elem : prLangSet) {
            if (elem.contains(toSearch)) {
                resultSet.add(elem);
                log.fine("  added " + elem.getName());
            }
        }
        log.info(" In general there are " + resultSet.size() + " elements in the set");
        log.fine("All ExactAndPartial matched results added");
    }

    //slice toSearch on separate words and find matches in resultSet with each of the words,
    //then filter for matches with all words from toSearch
    //works only if toSearch contains more than 1 word
    private void addWordSwappedMatch(String toSearch, Set<ProgrammingLanguage> resultSet) {
        log.info("Entered addWordSwappedMatch for word(s): " + toSearch);
        //slice to separate words
        String[] wordsToSearch = toSearch.split(" ");
        //find matches with each word
        if (wordsToSearch.length > 1) {
            //result set for all matches for each word
            Set<ProgrammingLanguage> resultMatchByWordsSet = new LinkedHashSet<>();
            //initialize 2 new sets - we need them to compare results for each word and find only common for both sets
            Set<ProgrammingLanguage> matchByWordsSet = new LinkedHashSet<>();
            //adding matches set for first word
            addExactAndPartialMatch(wordsToSearch[0], resultMatchByWordsSet);
            //searches all matches for each word from toSearch string
            for (int i = 1; i < wordsToSearch.length; i++) {
                //pass to existing match method each word and adds to this new set
                addExactAndPartialMatch(wordsToSearch[i], matchByWordsSet);
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
            String replacedSearch = toSearch.replace("scripting", "script");
            log.info("Replaced scripting to script");
            addWordSwappedMatch(replacedSearch, resultSet);
        } else {
            log.info("Word scripting not found, no new results added");
        }
    }

    //if toSearch contains words staring with "-" exclude them from resultSet
    //might be not optimal because of forming of another Set for ignored values
    private void excludeValuesWithMinus(String toIgnore, Set<ProgrammingLanguage> resultSet) {
        log.info("Entered excludeValuesWithMinus for word(s): " + toIgnore);
        if (toIgnore.length() > 0) {
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

    public Set<ProgrammingLanguage> getResultSet(String inputToSearch, int sortBy) {
        initializeData();
        String toSearch = "";
        String toIgnore = "";
        String[] s = inputToSearch.split(" ");
        for (String word : s) {
            if (word.length() > 0) {
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
        log.info("Values to search = " + toSearch);
        log.info("Values to ignore = " + toIgnore);

        //new list
        Set<ProgrammingLanguage> resultSet = new LinkedHashSet<>();
        //find exact (and partial) values and add to the list
        addExactAndPartialMatch(toSearch, resultSet);
        //find values with words in toSearch (swapped)
        addWordSwappedMatch(toSearch, resultSet);
        //find related like Scripting=script
        addRelatedScriptingLanguages(toSearch, resultSet);
        //exclude values with "-..."
        excludeValuesWithMinus(toIgnore, resultSet);
        //sort by relevance if sortBy=1
        if (sortBy==1) {
            getRelevanceSortedResultList(toSearch, resultSet);
        }
        return resultSet;
    }

    //sort by relevance (number of occurrences of each word)
    private void getRelevanceSortedResultList(String toSearch, Set<ProgrammingLanguage> resultSet) {
        List<ProgrammingLanguage> sortedResultList = new ArrayList<>(resultSet);
        sortByRelevance(toSearch, sortedResultList);
        resultSet.clear();
        resultSet.addAll(sortedResultList);
    }
}