package com.example.wowtime;

import com.example.wowtime.dto.FriendsListItem;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

public class FriendsListItemTest {

    private static FriendsListItem item;


    @BeforeClass
    public static void setBeforeTest(){
        item = new FriendsListItem(1,"1","name","13300000001","test@163.com");
    }

    @Test
    public void testGetUserId(){
        assertEquals(Integer.valueOf(1),item.getUserId());
    }

    @Test
    public void testGetUserIcon(){
        assertEquals("1",item.getUserIcon());
    }

    @Test
    public void testGetUsername(){
        assertEquals("name",item.getUsername());
    }

    @Test
    public void testGetPhone(){
        assertEquals("13300000001",item.getPhone());
    }

    @Test
    public void testGetEmail(){
        assertEquals("test@163.com",item.getEmail());
    }
}
