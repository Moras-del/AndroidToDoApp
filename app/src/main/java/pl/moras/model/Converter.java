package pl.moras.model;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.time.LocalDateTime;

public class Converter {

    @TypeConverter
    public static LocalDateTime fromStringToLocalDateTime(String value){
        return value==null? null : LocalDateTime.parse(value);
    }

    @TypeConverter
    public static String fromLocalDateTimeToString(LocalDateTime localDateTime){
        return localDateTime.toString();
    }
}
