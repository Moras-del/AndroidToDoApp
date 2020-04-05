package com.example.daysvalues;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.todoapplication.R;

@RequiresApi(api = Build.VERSION_CODES.N)
public enum Days {

    ALL(0, R.string.days_all),
    DAY(1, R.string.days_one),
    THREE_DAYS(3, R.string.days_three),
    WEEK(7, R.string.days_week),
    TWO_WEEKS(14, R.string.days_two_weeks),
    MONTH(30, R.string.days_month);


    private final int numOfDays;
    private final int textReference;

    Days(int numOfDays, int textReference) {
        this.numOfDays = numOfDays;
        this.textReference = textReference;
    }

    public static String[] getDays(Context context){
        String[] texts = new String[values().length];
        Days[] days = values();
        for(int i = 0; i< days.length; i++)
            texts[i] = context.getString(days[i].textReference);
        return texts;
    }


    public int getNumOfDays() {
        return numOfDays;
    }

}