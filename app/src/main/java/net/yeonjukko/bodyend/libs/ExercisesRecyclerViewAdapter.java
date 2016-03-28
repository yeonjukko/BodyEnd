package net.yeonjukko.bodyend.libs;

/**
 * Created by yeonjukko on 16. 3. 16..
 */


import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.activity.settings.ExerciseManagerActivity;
import net.yeonjukko.bodyend.model.ExerciseSortInfoModel;

import java.util.List;


public class ExercisesRecyclerViewAdapter extends RecyclerView.Adapter<ExercisesRecyclerViewAdapter.ExerciseViewHolder> {

    private final List<ExerciseSortInfoModel> data;
    private Context context;
    DBmanager dBmanager;

    public ExercisesRecyclerViewAdapter(final List<ExerciseSortInfoModel> data) {
        this.data = data;

    }

    @Override
    public ExercisesRecyclerViewAdapter.ExerciseViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        this.context = parent.getContext();
        dBmanager = ((ExerciseManagerActivity)context).dBmanager;
        return new ExerciseViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recycler_view_exercise_sort, parent, false));

    }

    @Override
    public void onBindViewHolder(final ExercisesRecyclerViewAdapter.ExerciseViewHolder holder, final int position) {

        final ExerciseSortInfoModel model = data.get(position);
        holder.tvExerciseName.setText(model.getExerciseName());
        holder.tvExerciseName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("mox", model.getExerciseName() + model.getExerciseDay());

                Snackbar.make(v, holder.tvExerciseName.getText() + "을 삭제하시겠습니까?", Snackbar.LENGTH_SHORT)
                        .setAction("삭제", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dBmanager.deleteSort(model.getSortId());
                                notifyDataSetChanged();
                                //((ExerciseManagerActivity) context).setRecyclerLayout();

                            }
                        })
                        .show();
                return false;
            }
        });

        if ((model.getExerciseDay() & ExerciseManagerActivity.FLAG_MONDAY) != 0) {
            holder.cbMonday.setChecked(true);
        }
        if ((model.getExerciseDay() & ExerciseManagerActivity.FLAG_TUESDAY) != 0) {
            holder.cbTuesday.setChecked(true);
        }
        if ((model.getExerciseDay() & ExerciseManagerActivity.FLAG_WEDNESDAY) != 0) {
            holder.cbWednesday.setChecked(true);
        }
        if ((model.getExerciseDay() & ExerciseManagerActivity.FLAG_THURSDAY) != 0) {
            holder.cbThursday.setChecked(true);
        }
        if ((model.getExerciseDay() & ExerciseManagerActivity.FLAG_FRIDAY) != 0) {
            holder.cbFriday.setChecked(true);
        }
        if ((model.getExerciseDay() & ExerciseManagerActivity.FLAG_SATURDAY) != 0) {
            holder.cbSaturday.setChecked(true);
        }
        if ((model.getExerciseDay() & ExerciseManagerActivity.FLAG_SUNDAY) != 0) {
            holder.cbSunday.setChecked(true);
        }


    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout sortLayout;
        public TextView tvExerciseName;
        public CheckableButton cbMonday;
        public CheckableButton cbTuesday;
        public CheckableButton cbWednesday;
        public CheckableButton cbThursday;
        public CheckableButton cbFriday;
        public CheckableButton cbSaturday;
        public CheckableButton cbSunday;


        public ExerciseViewHolder(View v) {
            super(v);
            sortLayout = (LinearLayout) v.findViewById(R.id.layout_sort);
            tvExerciseName = (TextView) v.findViewById(R.id.tv_exercise_sort);
            cbMonday = (CheckableButton) v.findViewById(R.id.set_cb_monday);
            cbTuesday = (CheckableButton) v.findViewById(R.id.set_cb_tuesday);
            cbWednesday = (CheckableButton) v.findViewById(R.id.set_cb_wednesday);
            cbThursday = (CheckableButton) v.findViewById(R.id.set_cb_thursday);
            cbFriday = (CheckableButton) v.findViewById(R.id.set_cb_friday);
            cbSaturday = (CheckableButton) v.findViewById(R.id.set_cb_saturday);
            cbSunday = (CheckableButton) v.findViewById(R.id.set_cb_sunday);


        }
    }


}
