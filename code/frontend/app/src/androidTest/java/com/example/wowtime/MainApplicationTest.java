package com.example.wowtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainApplicationTest {
    @Test
    public void testOnCreate(){
        MainApplication mainApplication=new MainApplication();
        System.out.println(mainApplication.getClass().toString());
        assertEquals(1,0);
        assertNotNull(MainApplication.getThemeNumber());
    }
}
