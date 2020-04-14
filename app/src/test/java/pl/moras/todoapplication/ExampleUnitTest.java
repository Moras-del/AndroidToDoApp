package pl.moras.todoapplication;

import android.content.Context;

import pl.moras.daysvalues.Days;
import pl.moras.model.Converter;

import pl.moras.todoapplication.R;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {

    private static String[] filters = {"Wszystko", "Do 24 godzin", "Do trzech dni", "Do tygodnia", "Do dwóch tygodni", "Do miesiąca"};

    @Mock
    Context context;

    @Before
    public void setup(){
        initMocks(this);
        when(context.getString(anyInt())).thenAnswer(arg->{
            switch ((int)arg.getArgument(0)){
                case R.string.days_one:
                    return filters[1];
                case R.string.days_three:
                    return filters[2];
                case R.string.days_week:
                    return filters[3];
                case R.string.days_two_weeks:
                    return filters[4];
                case R.string.days_month:
                    return filters[5];
                default:
                    return filters[0];
            }
        });
    }

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
    public void should_get_filter_names(){
        String[] days = Days.getDays(context);
        assertArrayEquals(filters, days);
    }


}