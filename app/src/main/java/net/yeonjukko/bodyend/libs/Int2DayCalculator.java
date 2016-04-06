package net.yeonjukko.bodyend.libs;

import net.yeonjukko.bodyend.activity.settings.ExerciseManagerActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by yeonjukko on 16. 3. 30..
 */
public class Int2DayCalculator {
    public int getDay(int date){
        int day=0;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            day = format.parse(date + "").getDay()+1;
            if (day == Calendar.MONDAY)
                day = ExerciseManagerActivity.FLAG_MONDAY;
            if (day == Calendar.TUESDAY)
                day = ExerciseManagerActivity.FLAG_TUESDAY;
            if (day == Calendar.WEDNESDAY)
                day = ExerciseManagerActivity.FLAG_WEDNESDAY;
            if (day == Calendar.THURSDAY)
                day = ExerciseManagerActivity.FLAG_THURSDAY;
            if (day == Calendar.FRIDAY)
                day = ExerciseManagerActivity.FLAG_FRIDAY;
            if (day == Calendar.SATURDAY)
                day = ExerciseManagerActivity.FLAG_SATURDAY;
            if (day == Calendar.SUNDAY) {
                day = ExerciseManagerActivity.FLAG_SUNDAY;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;

    }
}
