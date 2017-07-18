package com.devvit.jsonsearcher.repository;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.times;

public class MainRepositoryTest {

    private MainRepository mr = new MainRepository();

    @Test
    public void getPrLangSetSetIsFilledTest() throws Exception {
        Set prLangSet = MainRepository.getPrLangSet();
        assertFalse(prLangSet.isEmpty());
    }

    @Test
    public void getPrLangSetInitCallTest() throws Exception {
        MainRepository spy = PowerMockito.spy(new MainRepository());
        MainRepository.getPrLangSet();
        PowerMockito.verifyPrivate(spy, times(1)).invoke("initializeData");
    }
}