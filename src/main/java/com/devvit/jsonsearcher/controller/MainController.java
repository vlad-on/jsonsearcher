package com.devvit.jsonsearcher.controller;

import com.devvit.jsonsearcher.service.MainService;
import com.devvit.model.ProgrammingLanguage;
import com.devvit.model.UIModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
                                   @RequestParam(required = false, defaultValue = "5") Integer pageSize,
                                   @RequestParam(required = false, defaultValue = "1") Integer sortBy) {
        log.info("Entered getProgLangSearch() by /ajax/" + inputToSearch + " mapping");
        Set<ProgrammingLanguage> resultSet = mainService.getResultSet(inputToSearch, sortBy);
        int pageCount = (int) Math.ceil(1.0 * resultSet.size() / pageSize);
        UIModel uiModel = new UIModel();
//        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequestUri();
//        builder.scheme("https");
//        URI newUri = builder.build().toUri();
        String uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().toString().replaceAll("%2520"," ");
        System.out.println(uri);
        uiModel.setPreviousUrl(uri);
        uiModel.setPageNumber(pageNumber);
        uiModel.setPagesTotal(pageCount);
        uiModel.setResultCollection(resultSet);
        return new ModelAndView("searchresult", "prLanguages", uiModel);
    }
}