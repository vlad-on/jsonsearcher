package com.devvit.jsonsearcher.service;

import com.devvit.model.ProgrammingLanguage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class SearchEngineServiceTest {
    private SearchEngineService ses = new SearchEngineService();

    private String toSearch;
    private String toIgnore;
    private Set<ProgrammingLanguage> resultSet;
    private Set<ProgrammingLanguage> fullPrLangSet;
    @Before
    public void setUp() throws Exception {
        resultSet = new HashSet<>();
        fullPrLangSet = new HashSet<>();
        toSearch = "Java";
        toIgnore = "Script";

        ProgrammingLanguage pl1 = new ProgrammingLanguage("Java", "Compiled, Curly-bracket, Imperative, Object-oriented class-based, Procedural, Reflective", "James Gosling, Sun Microsystems");
        ProgrammingLanguage pl2 = new ProgrammingLanguage("Lasso", "Procedural, Script, Object-oriented class-based", "Kyle Jessup");
        ProgrammingLanguage pl3 = new ProgrammingLanguage("JavaScript", "Curly-bracket, Interpreted, Reflective, Procedural, Scripting, Interactive mode", "Brendan Eich");
        ProgrammingLanguage pl4 = new ProgrammingLanguage("Factor", "Compiled", "Slava Pestov");
        ProgrammingLanguage pl5 = new ProgrammingLanguage("F#", "Interactive mode", "Microsoft Research, Don Syme");
        ProgrammingLanguage pl6 = new ProgrammingLanguage("C#", "Compiled, Curly-bracket, Iterative, Object-oriented class-based, Reflective, Procedural", "Microsoft");
        fullPrLangSet.add(pl1);
        fullPrLangSet.add(pl2);
        fullPrLangSet.add(pl3);
        fullPrLangSet.add(pl4);
        fullPrLangSet.add(pl5);
        fullPrLangSet.add(pl6);
    }

    @Test
    public void addExactMatchEmptySearchTest() throws Exception {
        toSearch = "";
        ses.addExactMatch(toSearch, resultSet, fullPrLangSet);
        assertTrue(resultSet.isEmpty());
    }

    @Test
    public void addExactMatchNoResultsTest() throws Exception {
        toSearch = "Gosling James";
        ses.addExactMatch(toSearch, resultSet, fullPrLangSet);
        assertTrue(resultSet.isEmpty());
    }

    @Test
    public void addExactMatch2ResultsTest() throws Exception {
        ses.addExactMatch(toSearch, resultSet, fullPrLangSet);
        assertTrue("Wrong number of results returned", resultSet.size()==2);
    }

    @Test
    public void addExactMatchScriptingResultsTest() throws Exception {
        toSearch = "Scripting";
        ses.addExactMatch(toSearch, resultSet, fullPrLangSet);
        assertTrue("Wrong number of results returned", resultSet.size()==1);
    }

    @Test
    public void addWordSwappedMatchNameSwappedTest() throws Exception {
        toSearch = "C# Compiled";
        ses.addWordSwappedMatch(toSearch, resultSet, fullPrLangSet);
        assertTrue(resultSet.size()==1);
    }

    @Test
    public void addWordSwappedMatchNameSwapped2Test() throws Exception {
        toSearch = "C Compiled";
        ses.addWordSwappedMatch(toSearch, resultSet, fullPrLangSet);
        assertTrue(resultSet.size()==3);
    }

    @Test
    public void addWordSwappedMatchNoMatchTest() throws Exception {
        toSearch = "C# Gosling James";
        ses.addWordSwappedMatch(toSearch, resultSet, fullPrLangSet);
        assertTrue(resultSet.isEmpty());
    }

    @Test
    public void addWordSwappedMatchNameAndTypeTest() throws Exception {
        toSearch = "Gosling James";
        ses.addWordSwappedMatch(toSearch, resultSet, fullPrLangSet);
        assertTrue(resultSet.size()==1);
    }

    @Test
    public void addRelatedScriptingLanguagesTest() throws Exception {
        toSearch = "Scripting";
        ses.addRelatedScriptingLanguages(toSearch, resultSet, fullPrLangSet);
        assertTrue("Wrong number of results returned", resultSet.size()==2);
    }

    @Test
    public void excludeValuesWithMinus() throws Exception {
    }

    @Test
    public void getSortedResultList() throws Exception {
    }

    @Test
    public void getResultSet() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        resultSet.clear();
        fullPrLangSet.clear();
    }
}