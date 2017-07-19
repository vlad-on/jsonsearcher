package com.devvit.jsonsearcher.controller;

import com.devvit.jsonsearcher.service.MainService;
import com.devvit.model.ProgrammingLanguage;
import com.devvit.model.UIModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Contains entry points implementation for home page "/" and ajax calls "/ajax/..." when search is used
 */
@RestController
@RequestMapping("/")
public class MainController {

    private static Logger log = Logger.getLogger(MainController.class.getName());

    private MainService mainService = new MainService();

    @RequestMapping
    public ModelAndView homePage() {
        log.info("Entered homePage() by / mapping");
        mainService.getFullPrLangSet(); //for initialization only :)
        return new ModelAndView("ajax", "message", "Spring MVC with Ajax and JQuery ");
    }

    @RequestMapping(value = "/ajax/{inputToSearch}", method = RequestMethod.GET)
    public @ResponseBody
    ModelAndView getProgLangSearch(@PathVariable String inputToSearch,
                                   @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                                   @RequestParam(required = false, defaultValue = "0") Integer pageSize,
                                   @RequestParam(required = false, defaultValue = "0") Integer sortBy) {
        log.info("Entered getProgLangSearch() by /ajax/" + inputToSearch + " mapping");
        Set<ProgrammingLanguage> resultSet = mainService.getResultSet(inputToSearch, sortBy);
        int pageCount = 1;
        if (pageSize > 0) {
            pageCount = (int) Math.ceil(1.0 * resultSet.size() / pageSize);
        } else {
            pageSize = resultSet.size();
        }
        System.out.println("pageCount="+pageCount+" pageSize="+pageSize+" pageNumber="+pageNumber+" resultSet.size()="+resultSet.size());
        UIModel uiModel = new UIModel();
        uiModel.setPageNumber(pageNumber);
        uiModel.setPagesTotal(pageCount);
        Set limitedRS = limitResultSet(resultSet, pageNumber, pageSize);
        uiModel.setResultCollection(limitedRS);
        return new ModelAndView("searchresult", "prLanguages", uiModel);
    }

    private Set<ProgrammingLanguage> limitResultSet(Set<ProgrammingLanguage> resultSet, Integer pageNumber, Integer pageSize) {
        Set<ProgrammingLanguage> limRS = new LinkedHashSet<>();
        int i=0;
        int firstIndex = (pageNumber-1)*pageSize;
        int lastIndex = (pageNumber)*pageSize-1;
        for (ProgrammingLanguage pl : resultSet){
            if (i>=firstIndex && i<=lastIndex){
                limRS.add(pl);
            }
            i++;
        }
        return limRS;
    }
}