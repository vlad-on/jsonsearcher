package com.devvit.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Vlad on 19.07.2017.
 */
public class PrLangByNameComparerTest {
    @Before
    public void setUp() throws Exception {
        ProgrammingLanguage pl1 = new ProgrammingLanguage("Java", "Compiled, Curly-bracket, Imperative, Object-oriented class-based, Procedural, Reflective", "James Gosling, Sun Microsystems");
        ProgrammingLanguage pl2 = new ProgrammingLanguage("Lasso", "Procedural, Script, Object-oriented class-based", "Kyle Jessup");
        ProgrammingLanguage pl3 = new ProgrammingLanguage("JavaScript", "Curly-bracket, Interpreted, Reflective, Procedural, Scripting, Interactive mode", "Brendan Eich");
        ProgrammingLanguage pl4 = new ProgrammingLanguage("Factor", "Compiled", "Slava Pestov");
        ProgrammingLanguage pl5 = new ProgrammingLanguage("F#", "Interactive mode", "Microsoft Research, Don Syme");
        ProgrammingLanguage pl6 = new ProgrammingLanguage("C#", "Compiled, Curly-bracket, Iterative, Object-oriented class-based, Reflective, Procedural", "Microsoft");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void compareByNameTest() throws Exception {
         PrLangByNameComparer comp = new PrLangByNameComparer();
         //comp.compare();
    }

}