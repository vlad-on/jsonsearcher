package com.devvit.jsonsearcher.service;

import com.devvit.jsonsearcher.repository.MainRepository;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

public class MainServiceTest {

    private MainService ms = new MainService();
    private String userInput;
    private int sortBy;

    @Before
    public void setUp() throws Exception {
        userInput = "Java  -Script";
        sortBy = 1;
    }

    @Test
    public void setSearchAndIgnoreValuesCheckToSearchTest() throws Exception {
        ms.setSearchAndIgnoreValues(userInput);
        String toSearchMock = Whitebox.getInternalState(ms, "toSearch");
        assertTrue(toSearchMock.equals("Java"));
    }

    @Test
    public void setSearchAndIgnoreValuesCheckToSearch2Test() throws Exception {
        userInput = "Java Compile set - -Script";
        ms.setSearchAndIgnoreValues(userInput);
        String toSearchMock = Whitebox.getInternalState(ms, "toSearch");
        assertTrue(toSearchMock.equals("Java Compile set"));
    }

    @Test
    public void setSearchAndIgnoreValuesCheckToIgnoreTest() throws Exception {
        ms.setSearchAndIgnoreValues(userInput);
        String toIgnoreMock = Whitebox.getInternalState(ms, "toIgnore");
        assertTrue(toIgnoreMock.equals("Script"));
    }

    @Test
    public void setSearchAndIgnoreValuesCheckToIgnore2Test() throws Exception {
        userInput = "Java Compile set - -Script";
        ms.setSearchAndIgnoreValues(userInput);
        String toIgnoreMock = Whitebox.getInternalState(ms, "toIgnore");
        assertTrue(toIgnoreMock.equals("Script"));
    }

    @Test
    public void getResultSetNoMatchesTest() throws Exception {
        userInput = "";
        Set rs = ms.getResultSet(userInput, sortBy);
        assertTrue(rs.isEmpty());
        userInput = "fdjkfdjfkd --";
        rs = ms.getResultSet(userInput, sortBy);
        assertTrue(rs.isEmpty());
    }

    @Test
    public void getResultSet1MatchTest() throws Exception {
        Set rs = ms.getResultSet(userInput, sortBy);
        assertTrue(rs.size()==1);
    }

    @Test
    public void getFullPrLangSetRepositoryCallTest() throws Exception {
        MainRepository msMock = mock(MainRepository.class);
        ms.getFullPrLangSet();
        verify(msMock, times(1)).getPrLangSet();
    }
}