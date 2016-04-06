package net.yeonjukko.bodyend.libs;

import android.util.Log;

import net.yeonjukko.bodyend.activity.settings.ExerciseManagerActivity;
import net.yeonjukko.bodyend.model.ExerciseSortInfoModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by yeonjukko on 16. 3. 28..
 */
public class ExerciseTodayList {
    public ArrayList<ExerciseSortInfoModel> getTodayList(int today, ArrayList<ExerciseSortInfoModel> model) {
        ArrayList<ExerciseSortInfoModel> newModel = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            int day = format.parse(today + "").getDay()+1;

            Log.d("mox4", day + "day");
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

            Log.d("mox4", day + "day");

            for (int i = 0; i < model.size(); i++) {

                if ((day & model.get(i).getExerciseDay()) != 0) {
                    newModel.add(model.get(i));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d("mox4", newModel.size() + ":size");
        return newModel;
    }
}
