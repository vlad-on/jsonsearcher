package com.devvit.jsonsearcher.controller;

import com.devvit.jsonsearcher.service.SearchEngineService;
import com.devvit.model.ProgrammingLanguage;
import com.devvit.model.UIModel;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by Vlad on 12.06.2017.
 */
@RestController
@RequestMapping("/")
@PropertySource("application.properties")
public class MainController {

    private static Logger log = Logger.getLogger(MainController.class.getName());

    private SearchEngineService searchEngineService = new SearchEngineService();

    @RequestMapping
    public ModelAndView homePage() {
        log.info("entered homePage() by / mapping");
        SearchEngineService.initializeData();
        return new ModelAndView("ajax", "message", "Spring MVC with Ajax and JQuery ");
    }

    @RequestMapping(value = "/ajax/{inputToSearch}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView getProgLangSearch(@PathVariable String inputToSearch,
                                   @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                                   @RequestParam(required = false, defaultValue = "5") Integer pageSize,
                                   @RequestParam(required = false) String sortBy) {
        log.info("entered getProgLangSearch() by /ajax/" + inputToSearch + " mapping");

        //return the list:
        boolean isSortByRelevance = true; //TODO implement search filter
        //sort by relevance
        if (isSortByRelevance) {
            Set<ProgrammingLanguage> resultSet = searchEngineService.getResultSet(inputToSearch, 1);
            int pageCount = (int) Math.ceil(1.0 * resultSet.size() / pageSize);
            UIModel uiModel = new UIModel();
            uiModel.setPageNumber(pageNumber);
            uiModel.setPagesTotal(pageCount);
            uiModel.setResultCollection(resultSet);
            return new ModelAndView("searchresult", "prLanguages", uiModel);
        } else {
            //sort by PL name (as it was in the file)
            Set<ProgrammingLanguage> resultSet = searchEngineService.getResultSet(inputToSearch, 0);
            return new ModelAndView("searchresult", "prLanguages", resultSet);
        }
    }
}
