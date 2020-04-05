package com.example.todoapplication;

import com.example.daysvalues.Days;
import com.example.model.Converter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {

    @Test
    public void should_convert_string_to_localdatetime() {
        LocalDateTime localDateTime = Converter.fromStringToLocalDateTime("2020-01-01T12:00");
        assertEquals(LocalDateTime.of(2020, 1, 1, 12, 0), localDateTime);
    }

    @Test
    public void should_convert_localdatetime_to_string(){
        LocalDateTime currentTime = LocalDateTime.now();
        assertEquals(currentTime.toString(), Converter.fromLocalDateTimeToString(currentTime));
    }

    @Test
    public void should_get_enum(){
        String text = "do trzech dni";
        Days day = Days.fromString(text);
        assertEquals(Days.THREE_DAYS, day);
    }


}