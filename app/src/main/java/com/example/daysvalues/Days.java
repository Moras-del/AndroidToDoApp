package com.example.daysvalues;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@RequiresApi(api = Build.VERSION_CODES.N)
public enum Days {

    ALL(0, "wszystko"),
    DAY(1, "do 24 godzin"),
    THREE_DAYS(3, "do trzech dni"),
    WEEK(7, "do tygodnia"),
    TWO_WEEKS(14, "do dwóch tygodni"),
    MONTH(30,"do końca miesiąca");

    private final int numOfDays;
    private final String text;

    Days(int numOfDays, String text) {
        this.numOfDays = numOfDays;
        this.text = text;
    }

    public int getNumOfDays() {
        return numOfDays;
    }

    @Override
    public String toString() {
        return text;
    }

    private static final Map<String, Days> stringToEnum =
            Stream.of(values()).collect(toMap(Object::toString, e->e));

    public static Days fromString(String day){
        return stringToEnum.get(day);
    }

}