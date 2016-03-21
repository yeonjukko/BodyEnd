package net.yeonjukko.bodyend.libs;

/**
 * Created by yeonjukko on 16. 3. 16..
 */


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.aakira.expandablelayout.Utils;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.activity.RecordActivity;
import net.yeonjukko.bodyend.activity.settings.WaterSettingActivity;
import net.yeonjukko.bodyend.model.UserInfoModel;
import net.yeonjukko.bodyend.model.UserRecordModel;

import java.util.List;


public class RecordRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<RecordItemModel> data;
    private Context context;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private int VIEW_TYPE_WATER = 100;
    private int VIEW_TYPE_EXERCISE = 101;
    private int VIEW_TYPE_WEIGHT = 102;
    private int VIEW_TYPE_MEAL = 103;
    private int VIEW_TYPE_PICTURE = 104;
    private int VIEW_TYPE_ERROR = 105;


    DBmanager dBmanager;
    LayoutInflater inflater;

    public RecordRecyclerViewAdapter(final List<RecordItemModel> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            expandState.append(0, true);
            expandState.append(1, true);
            expandState.append(2, true);
            expandState.append(3, false);
            expandState.append(4, false);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        this.context = parent.getContext();
        dBmanager = ((RecordActivity) context).dBmanager;
        inflater = ((RecordActivity) context).getLayoutInflater();

        if (viewType == VIEW_TYPE_WATER) {
            return new ViewHolderWater(LayoutInflater.from(context)
                    .inflate(R.layout.recycler_view_list_row_water, parent, false));
        } else if (viewType == VIEW_TYPE_EXERCISE) {
            return new ViewHolderExercise(LayoutInflater.from(context)
                    .inflate(R.layout.recycler_view_list_row_exercise, parent, false));
        } else if (viewType == VIEW_TYPE_WEIGHT) {
            return new ViewHolderWeight(LayoutInflater.from(context)
                    .inflate(R.layout.recycler_view_list_row_weight, parent, false));
        } else if (viewType == VIEW_TYPE_MEAL) {
            return new ViewHolderMeal(LayoutInflater.from(context)
                    .inflate(R.layout.recycler_view_list_row_meal, parent, false));
        } else if (viewType == VIEW_TYPE_PICTURE) {
            return new ViewHolderPicture(LayoutInflater.from(context)
                    .inflate(R.layout.recycler_view_list_row_picture, parent, false));
        }

        throw new IndexOutOfBoundsException("viewHolderTypeError: 내부 에러가 발생하였습니다. ");

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_WATER;
        } else if (position == 1) {
            return VIEW_TYPE_EXERCISE;
        } else if (position == 2) {
            return VIEW_TYPE_WEIGHT;
        } else if (position == 3) {
            return VIEW_TYPE_MEAL;
        } else if (position == 4) {
            return VIEW_TYPE_PICTURE;
        }
        return VIEW_TYPE_ERROR;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final RecordItemModel item = data.get(position);
        final Resources resource = context.getResources();

        if (position == 0) {
            // holderWater 설정
            final ViewHolderWater holderWater = (ViewHolderWater) holder;
            holderWater.textView.setText(item.description);
            holderWater.textView.setTextSize(19);
            holderWater.textView.setTextColor(context.getResources().getColor(android.R.color.white));
            holderWater.textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holderWater.itemView.setBackground(resource.getDrawable(R.drawable.layout_round_all));
            holderWater.expandableLayout.setBackground(resource.getDrawable(R.drawable.layout_round_bottom));
            holderWater.expandableLayout.setInterpolator(item.interpolator);
            holderWater.expandableLayout.setExpanded(expandState.get(position));
            holderWater.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
                @Override
                public void onPreOpen() {
                    createRotateAnimator(holderWater.buttonLayout, 0f, 180f).start();
                    holderWater.itemView.setBackground(resource.getDrawable(R.drawable.layout_round_all));
                    holderWater.expandableLayout.setBackground(resource.getDrawable(R.drawable.layout_round_bottom));
                    expandState.put(position, true);
                }

                @Override
                public void onPreClose() {
                    createRotateAnimator(holderWater.buttonLayout, 180f, 0f).start();
                    holderWater.itemView.setBackground(resource.getDrawable(R.drawable.layout_round_all));
                    expandState.put(position, false);
                }
            });
            holderWater.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickButton(holderWater.expandableLayout);
                }
            });

            holderWater.buttonLayout.setRotation(expandState.get(position) ? 180f : 0f);

            //<--물 ui 설정
            final float waterVolume = dBmanager.selectUserRecordDB(((RecordActivity) context).getToday()).getWaterVolume();
            int water_cup_count = (int) waterVolume;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.height = 80;
            params.width = 80;
            ImageView fullWater;

            //<--물 기록 UI 설정 부분

            if (waterVolume >= 20) water_cup_count = 20;
            for (int i = 0; i < water_cup_count; i++) {
                fullWater = new ImageView(context);
                fullWater.setBackgroundResource(R.drawable.icon_bottle_gray_10);
                fullWater.setLayoutParams(params);
                holderWater.gridWaterLayout.addView(fullWater);
            }

            //<--마신 잔 수 나타내기
            final int waterRecord = dBmanager.selectUserRecordDB(((RecordActivity) context).getToday()).getWaterRecord();
            for (int i = 0; i < waterRecord; i++) {
                holderWater.gridWaterLayout.getChildAt(i).setBackgroundResource(R.drawable.icon_bottle_checked);
                holderWater.gridWaterLayout.getChildAt(i).setTag("checked");
            }

            holderWater.btWaterPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int waterRecord = dBmanager.selectUserRecordDB(((RecordActivity) context).getToday()).getWaterRecord();
                    if (waterRecord >= waterVolume) {
                        Toast.makeText(context, "이미 목표를 완료하였습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    holderWater.gridWaterLayout.getChildAt(waterRecord).setBackgroundResource(R.drawable.icon_bottle_checked);
                    holderWater.gridWaterLayout.getChildAt(waterRecord).setTag("checked");
                    waterRecord++;

                    dBmanager.updateWaterRecord(waterRecord);
                    holderWater.gridWaterLayout.invalidate();
                }
            });

            holderWater.btWaterMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int waterRecord = dBmanager.selectUserRecordDB(((RecordActivity) context).getToday()).getWaterRecord();
                    if (waterRecord == 0) {
                        Toast.makeText(context, "물 좀 드새오!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    holderWater.gridWaterLayout.getChildAt(waterRecord - 1).setBackgroundResource(R.drawable.icon_bottle_gray_10);
                    holderWater.gridWaterLayout.getChildAt(waterRecord - 1).setTag(null);
                    waterRecord--;

                    dBmanager.updateWaterRecord(waterRecord);
                    holderWater.gridWaterLayout.invalidate();
                }
            });

            holderWater.btWaterSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WaterSettingActivity.class);
                    context.startActivity(intent);
                }
            });


        } else if (position == 2) {
            final ViewHolderWeight holderWeight = (ViewHolderWeight) holder;
            holderWeight.textView.setText(item.description);
            holderWeight.textView.setTextSize(19);
            holderWeight.textView.setTextColor(context.getResources().getColor(android.R.color.white));
            holderWeight.textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holderWeight.itemView.setBackground(resource.getDrawable(R.drawable.layout_round_all));
            holderWeight.expandableLayout.setBackground(resource.getDrawable(R.drawable.layout_round_bottom));
            holderWeight.expandableLayout.setInterpolator(item.interpolator);
            holderWeight.expandableLayout.setExpanded(expandState.get(position));
            holderWeight.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
                @Override
                public void onPreOpen() {
                    createRotateAnimator(holderWeight.buttonLayout, 0f, 180f).start();
                    holderWeight.itemView.setBackground(resource.getDrawable(R.drawable.layout_round_all));
                    holderWeight.expandableLayout.setBackground(resource.getDrawable(R.drawable.layout_round_bottom));
                    expandState.put(position, true);
                }

                @Override
                public void onPreClose() {
                    createRotateAnimator(holderWeight.buttonLayout, 180f, 0f).start();
                    holderWeight.itemView.setBackground(resource.getDrawable(R.drawable.layout_round_all));
                    expandState.put(position, false);
                }
            });
            holderWeight.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickButton(holderWeight.expandableLayout);
                }
            });

            holderWeight.buttonLayout.setRotation(expandState.get(position) ? 180f : 0f);
            holderWeight.tvWeight.setText((dBmanager.selectUserInfoDB().getUserCurrWeight()) + "kg");
            holderWeight.tvWeight.setTextColor(resource.getColor(R.color.Primary_text));
            holderWeight.btWeightPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    float weight = dBmanager.selectUserInfoDB().getUserCurrWeight() + 0.1f;
                    Log.d("mox", weight + "");
                    String strNumber = String.format("%.1f", weight);
                    dBmanager.updateCurrWeight(Float.parseFloat(strNumber));
                    holderWeight.tvWeight.setText(strNumber + "kg");
                    holderWeight.expandableLayout.invalidate();
                }
            });
            holderWeight.btWeightMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float weight = (float) (dBmanager.selectUserInfoDB().getUserCurrWeight() - 0.1);
                    String strNumber = String.format("%.1f", weight);
                    dBmanager.updateCurrWeight(Float.parseFloat(strNumber));
                    holderWeight.tvWeight.setText(strNumber + "kg");
                    holderWeight.expandableLayout.invalidate();
                }
            });


        } else if (position == 3) {
            final ViewHolderMeal holderMeal = (ViewHolderMeal) holder;
            holderMeal.textView.setText(item.description);
            holderMeal.textView.setTextSize(19);
            holderMeal.textView.setTextColor(context.getResources().getColor(android.R.color.white));
            holderMeal.textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holderMeal.itemView.setBackground(resource.getDrawable(R.drawable.layout_round_all));
            holderMeal.expandableLayout.setBackground(resource.getDrawable(R.drawable.layout_round_bottom));
            holderMeal.expandableLayout.setInterpolator(item.interpolator);
            holderMeal.expandableLayout.setExpanded(expandState.get(position));
            holderMeal.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
                @Override
                public void onPreOpen() {
                    createRotateAnimator(holderMeal.buttonLayout, 0f, 180f).start();
                    holderMeal.itemView.setBackground(resource.getDrawable(R.drawable.layout_round_all));
                    holderMeal.expandableLayout.setBackground(resource.getDrawable(R.drawable.layout_round_bottom));
                    expandState.put(position, true);
                }

                @Override
                public void onPreClose() {
                    createRotateAnimator(holderMeal.buttonLayout, 180f, 0f).start();
                    holderMeal.itemView.setBackground(resource.getDrawable(R.drawable.layout_round_all));
                    expandState.put(position, false);
                }
            });
            holderMeal.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickButton(holderMeal.expandableLayout);
                    holderMeal.itemView.setClickable(false);
                }
            });

            holderMeal.buttonLayout.setRotation(expandState.get(position) ? 180f : 0f);
            holderMeal.tvBreakfast.setTextColor(resource.getColor(R.color.Primary_text));
            holderMeal.tvLunch.setTextColor(resource.getColor(R.color.Primary_text));
            holderMeal.tvDinner.setTextColor(resource.getColor(R.color.Primary_text));
            holderMeal.tvRefreshment.setTextColor(resource.getColor(R.color.Primary_text));
            holderMeal.etBreakfast.setTextColor(resource.getColor(R.color.Primary_text));
            holderMeal.etLunch.setTextColor(resource.getColor(R.color.Primary_text));
            holderMeal.etDinner.setTextColor(resource.getColor(R.color.Primary_text));
            holderMeal.etRefreshment.setTextColor(resource.getColor(R.color.Primary_text));


        }

    }


    private void onClickButton(final ExpandableLayout expandableLayout) {
        expandableLayout.toggle();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolderWater extends RecyclerView.ViewHolder {
        public TextView textView;
        public RelativeLayout buttonLayout;
        public ExpandableRelativeLayout expandableLayout;
        public GridLayout gridWaterLayout;
        public ImageView btWaterSetting;
        public Button btWaterPlus;
        public Button btWaterMinus;


        public ViewHolderWater(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button);
            expandableLayout = (ExpandableRelativeLayout) v.findViewById(R.id.layout_water);
            gridWaterLayout = (GridLayout) v.findViewById(R.id.layout_water_grid);
            btWaterPlus = (Button) v.findViewById(R.id.bt_water_plus);
            btWaterMinus = (Button) v.findViewById(R.id.bt_water_minus);
            btWaterSetting = (ImageView) v.findViewById(R.id.bt_water_setting);

        }
    }

    public static class ViewHolderExercise extends RecyclerView.ViewHolder {
        public TextView textView;
        public RelativeLayout buttonLayout;
        public ExpandableRelativeLayout expandableLayout;

        public ViewHolderExercise(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button);
            expandableLayout = (ExpandableRelativeLayout) v.findViewById(R.id.layout_exercise);
        }
    }

    public static class ViewHolderWeight extends RecyclerView.ViewHolder {
        public TextView textView;
        public RelativeLayout buttonLayout;
        public ExpandableRelativeLayout expandableLayout;
        public TextView tvWeight;
        public Button btWeightPlus;
        public Button btWeightMinus;

        public ViewHolderWeight(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button2);
            expandableLayout = (ExpandableRelativeLayout) v.findViewById(R.id.layout_weight);
            tvWeight = (TextView) v.findViewById(R.id.tv_weight);
            btWeightPlus = (Button) v.findViewById(R.id.bt_weight_plus);
            btWeightMinus = (Button) v.findViewById(R.id.bt_weight_minus);

        }
    }

    public static class ViewHolderMeal extends RecyclerView.ViewHolder {
        public TextView textView;
        public RelativeLayout buttonLayout;
        public ExpandableRelativeLayout expandableLayout;
        public TextView tvBreakfast;
        public TextView tvLunch;
        public TextView tvDinner;
        public TextView tvRefreshment;
        public EditText etBreakfast;
        public EditText etLunch;
        public EditText etDinner;
        public EditText etRefreshment;

        public ViewHolderMeal(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button3);
            expandableLayout = (ExpandableRelativeLayout) v.findViewById(R.id.layout_meal);
            tvBreakfast = (TextView) v.findViewById(R.id.tv_breakfast);
            tvLunch = (TextView) v.findViewById(R.id.tv_lunch);
            tvDinner = (TextView) v.findViewById(R.id.tv_dinner);
            tvRefreshment = (TextView) v.findViewById(R.id.tv_refreshment);
            etBreakfast = (EditText)v.findViewById(R.id.et_breakfast);
            etLunch = (EditText) v.findViewById(R.id.et_lunch);
            etDinner = (EditText) v.findViewById(R.id.et_dinner);
            etRefreshment = (EditText) v.findViewById(R.id.et_refreshment);
        }
    }

    public static class ViewHolderPicture extends RecyclerView.ViewHolder {
        public TextView textView;
        public RelativeLayout buttonLayout;
        public ExpandableRelativeLayout expandableLayout;

        public ViewHolderPicture(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button);
            expandableLayout = (ExpandableRelativeLayout) v.findViewById(R.id.layout_picture);
        }

    }


    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }
}
