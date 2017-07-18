package com.devvit.jsonsearcher.service;

import com.devvit.jsonsearcher.controller.MainController;
import com.devvit.model.PrLangByNameComparer;
import com.devvit.model.ProgrammingLanguage;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

/**
 * Main search engine methods are stored here
 * Most of the method take toSearch value which is list of words entered by user
 *      and toIgnore - list of words entered by user but with "-" in the beginning
 *      toSearch and toIgnore - Strings of words separated by space
 * Also they require fullPrLangSet - set of all Programming Languages, it is taken from MainRepository through MainService
 */
@Service
public class SearchEngineService {

    private static Logger log = Logger.getLogger(MainController.class.getName());

    private void sortByRelevance(String toSearch, List<ProgrammingLanguage> sortedResultList) {
        log.info("Entered sortByRelevance for word(s):" + toSearch);
        sortedResultList.sort((a, b) -> a.countWordOccurrences(toSearch) > b.countWordOccurrences(toSearch) ? -1 :
                a.countWordOccurrences(toSearch) == b.countWordOccurrences(toSearch) ? 0 : 1);
        log.fine(" List sorted by word relevance");
    }

    private void sortByName(List<ProgrammingLanguage> sortedResultList) {
        log.info("Entered sortByName");
        sortedResultList.sort(new PrLangByNameComparer());
        log.fine(" List sorted by PL names");
    }

    //toSearch - value to be match with,
    //resultSet - Set where matched values to be added
    void addExactMatch(String toSearch, Set<ProgrammingLanguage> resultSet, Set<ProgrammingLanguage> fullPrLangSet) {
        log.info("Entered addExactMatch() for word(s): " + toSearch);
        if (toSearch.equals("")){
            log.info(" There is nothing to match.");
            return; //exit method
        }
        for (ProgrammingLanguage elem : fullPrLangSet) {
            if (elem.contains(toSearch)) {
                resultSet.add(elem);
                log.fine(" 8885 added " + elem.getName());
            }
        }
        log.info(" In general there are " + resultSet.size() + " elements in the set");
        log.fine(" All Exact matched results added");
    }

    //slice toSearch on separate words and find matches in resultSet with each of the words,
    //then filter for matches with all words from toSearch
    //works only if toSearch contains more than 1 word
    void addWordSwappedMatch(String toSearch, Set<ProgrammingLanguage> resultSet, Set<ProgrammingLanguage> fullPrLangSet) {
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
            addExactMatch(wordsToSearch[0], resultMatchByWordsSet, fullPrLangSet);
            //searches all matches for each word from toSearch string
            for (int i = 1; i < wordsToSearch.length; i++) {
                matchByWordsSet.clear();
                //pass to existing match method each word and adds to this new set
                addExactMatch(wordsToSearch[i], matchByWordsSet, fullPrLangSet);
                //this method removes all objects from resultMatchByWordsSet that are not in matchByWordsSet
                //so it leaves in resultMatchByWordsSet only common objects with matchByWordsSet
                resultMatchByWordsSet.retainAll(matchByWordsSet);
            }
            //adding results of above search to general result set
            //as the result set is Set - it won't add same elements and as it is Linked - results with swapped words
            // added in the end - you might consider them as related relatively to user search input (works only if no sorting applied)
            resultSet.addAll(resultMatchByWordsSet);
            log.fine(" All WordSwapped matched results added");
        } else {
            log.info(" There is only one word to search, no new results added");
        }
    }

    //is meant to be used after exact and swapped match methods
    //if toSearch contains word scriprting - change it to script and use addWordSwappedMatch(...)
    void addRelatedScriptingLanguages(String toSearch, Set<ProgrammingLanguage> resultSet, Set<ProgrammingLanguage> fullPrLangSet) {
        log.info("Entered addRelatedScriptingLanguages for word(s): " + toSearch);
        if (toSearch.toLowerCase().contains("scripting")) {
            String replacedSearch = toSearch.toLowerCase().replaceAll("scripting", "script");
            log.info(" Replaced scripting to script");
            addExactMatch(replacedSearch, resultSet, fullPrLangSet);
            addWordSwappedMatch(replacedSearch, resultSet, fullPrLangSet);
        } else {
            log.info(" Word scripting not found, no new results added");
        }
    }

    //if toSearch contains words staring with "-" exclude them from resultSet
    //might be not optimal because of forming of another Set for ignored values
    void excludeValuesWithMinus(String toIgnore, Set<ProgrammingLanguage> resultSet, Set<ProgrammingLanguage> fullPrLangSet) {
        log.info("Entered excludeValuesWithMinus for word(s): " + toIgnore);
        String[] wordsToIgnore = toIgnore.split(" ");
        if (toIgnore.length() > 0) {
            log.info(" there are words to exclude");
            //get all results for each such word to new HashSet
            Set<ProgrammingLanguage> plSetToIgnore = new HashSet<>();
            for (String aWordsToIgnore : wordsToIgnore) {
                addExactMatch(aWordsToIgnore, plSetToIgnore, fullPrLangSet);
            }
            //delete results with such words from resultSet
            resultSet.removeAll(plSetToIgnore);
            log.fine(" Excluded all " + plSetToIgnore.size() + " results containing " + toIgnore);
        } else {
            log.info(" There are no values to ignore, no results excluded");
        }
    }

    //sort by relevance (number of occurrences of each word)
    void getSortedResultList(String toSearch, Set<ProgrammingLanguage> resultSet, int sortBy) {
        List<ProgrammingLanguage> sortedResultList = new ArrayList<>(resultSet);
        switch (sortBy) {
            case 0:
                sortByName(sortedResultList);
                break;
            case 1:
                sortByRelevance(toSearch, sortedResultList);
                break;
        }
        resultSet.clear();
        resultSet.addAll(sortedResultList);
    }
}
