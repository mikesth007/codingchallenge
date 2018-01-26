package com.bridj.codingchallenge;

import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@RunWith(JUnit4.class)
@SmallTest
public class DataUnitTest {
    List<Event> events;

    @Before
    public void createEvents() {
        events = new ArrayList<>();
    }

    @Test
    public void checkDataFilterAndSort() {
        for (int i = 5; i > 0; i--) {
            Event event = new Event();
            event.setName("Event" + i);
            event.setDate(new Date());
            event.setAvailableSeats(new Random().nextBoolean() ? 1 : 0);
            events.add(event);
        }

        int prevTotal = events.size();
        Iterator<Event> itr = events.iterator();
        int removedCount = 0;
        while (itr.hasNext()) {
            Event ev = itr.next();
            if (ev.getAvailableSeats() <= 0) {
                removedCount++;
                itr.remove();
            }
        }

        Assert.assertEquals(events.size(), prevTotal - removedCount);
        Assert.assertTrue("Filtered data with 0 values failure.", prevTotal - removedCount == events.size());
    }
}