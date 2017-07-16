package com.devvit.jsonsearcher.service;

import com.devvit.jsonsearcher.controller.MainController;
import com.devvit.jsonsearcher.repository.MainRepository;
import com.devvit.model.ProgrammingLanguage;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by Vlad on 16.07.2017.
 */
@Service
public class MainService {

    private static Logger log = Logger.getLogger(MainController.class.getName());

    private SearchEngineService searchEngineService = new SearchEngineService();

    public Set<ProgrammingLanguage> getResultSet(String inputToSearch, int sortBy) {
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

        Set<ProgrammingLanguage> fullPrLangSet = getFullPrLangSet();

        //new list
        Set<ProgrammingLanguage> resultSet = new LinkedHashSet<>();
        //find exact (and partial) values and add to the list
        searchEngineService.addExactAndPartialMatch(toSearch, resultSet, fullPrLangSet);
        //find values with words in toSearch (swapped)
        searchEngineService.addWordSwappedMatch(toSearch, resultSet, fullPrLangSet);
        //find related like Scripting=script
        searchEngineService.addRelatedScriptingLanguages(toSearch, resultSet, fullPrLangSet);
        //exclude values with "-..."
        searchEngineService.excludeValuesWithMinus(toIgnore, resultSet, fullPrLangSet);
        //sort by relevance if sortBy=1
        if (sortBy==1) {
            searchEngineService.getRelevanceSortedResultList(toSearch, resultSet);
        }
        return resultSet;
    }

    public Set<ProgrammingLanguage> getFullPrLangSet() {
        return MainRepository.getPrLangSet();
    }
}
