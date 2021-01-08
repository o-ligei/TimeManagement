package com.example.wowtime.dto;


import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.Test;

public class AlarmListItemTest {

    private AlarmListItem alarmListItem;

    public AlarmListItemTest(){
        alarmListItem=new AlarmListItem();
        List<Boolean> frequency = new ArrayList<>();
        frequency.add(0, true);
        for (int i = 1; i < 8; i++) {
            frequency.add(i, false);
        }
        alarmListItem=new AlarmListItem("test",frequency,"shaking game","radar",0,0);
    }

    @Test
     public void getTag() {
        assertEquals(alarmListItem.getTag(),"test");
    }

    @Test
    public void getFrequency() {
        List<Boolean> frequency =alarmListItem.getFrequency();
        assertEquals(frequency.get(0),true);
        for (int i=1;i<8;i++){
            assertEquals(frequency.get(i),false);
        }

    }

    @Test
    public void getGame() {
        assertEquals(alarmListItem.getGame(),"shaking game");
    }

    @Test
    public void getRing() {
        assertEquals(alarmListItem.getRing(),"radar");
    }

    @Test
    public void getHour() {
        assertEquals(alarmListItem.getHour(),0);
    }

    @Test
    public void getMinute() {
        assertEquals(alarmListItem.getMinute(),0);
    }

    @Test
    public void getSleepFlag() {
        assertFalse(alarmListItem.getSleepFlag());
    }

    @Test
    public void getSleepHour() {
        assertEquals(alarmListItem.getSleepHour(),0);
    }

    @Test
    public void getSleepMinute() {
        assertEquals(alarmListItem.getSleepMinute(),0);
    }

    @Test
    public void getStatus() {
        assertFalse(alarmListItem.getStatus());
    }

    @Test
    public void setTag() {
        alarmListItem.setTag("test1");
        assertEquals(alarmListItem.getTag(),"test1");
    }

    @Test
    public void setFrequency() {
        ArrayList<Boolean> frequency = (ArrayList<Boolean>) alarmListItem.getFrequency();
        frequency.set(1,true);
        alarmListItem.setFrequency(frequency);
        assertEquals(alarmListItem.getFrequency().get(1),true);
    }

    @Test
    public void setGame() {
        alarmListItem.setGame("blowing game");
        assertEquals(alarmListItem.getGame(),"blowing game");
    }

    @Test
    public void setHour() {
        alarmListItem.setHour(1);
        assertEquals(alarmListItem.getHour(),1);
    }

    @Test
    public void setMinute() {
        alarmListItem.setMinute(1);
        assertEquals(alarmListItem.getMinute(),1);
    }

    @Test
    public void setRing() {
        alarmListItem.setRing("classic");
        assertEquals(alarmListItem.getRing(),"classic");
    }

    @Test
    public void setSleepFlag() {
        alarmListItem.setSleepFlag(true);
        assertTrue(alarmListItem.getSleepFlag());
    }

    @Test
    public void setSleepHour() {
        alarmListItem.setSleepHour(1);
        assertEquals(alarmListItem.getSleepHour(),1);
    }

    @Test
    public void setSleepMinute() {
        alarmListItem.setSleepMinute(1);
        assertEquals(alarmListItem.getSleepMinute(),1);
    }

    @Test
    public void setStatus() {
        alarmListItem.setStatus(true);
        assertEquals(alarmListItem.getStatus(),true);
    }
}