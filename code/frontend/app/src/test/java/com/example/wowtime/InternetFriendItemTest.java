package com.example.wowtime;

import com.example.wowtime.adapter.InternetFriendItemAdapter;
import com.example.wowtime.dto.InternetFriendItem;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class InternetFriendItemTest {

    private static InternetFriendItem item;

    @BeforeClass
    public static void setBeforeClass(){
        item = new InternetFriendItem(1,"icon","name");
    }

    @Test
    public void testGetUserId(){
        assertEquals(Integer.valueOf(1),item.getUserId());
    }

    @Test
    public void testGetUserIcon(){
        assertEquals("icon",item.getUserIcon());
    }

    @Test
    public void testGetUsername(){
        assertEquals("name",item.getUsername());
    }
}
