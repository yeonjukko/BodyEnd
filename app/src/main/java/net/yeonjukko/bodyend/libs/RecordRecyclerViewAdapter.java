package net.yeonjukko.bodyend.libs;

/**
 * Created by yeonjukko on 16. 3. 16..
 */


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import net.yeonjukko.bodyend.RecordActivity;

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
                    .inflate(R.layout.recycler_view_list_row_water, parent, false));
        } else if (viewType == VIEW_TYPE_MEAL) {
            return new ViewHolderMeal(LayoutInflater.from(context)
                    .inflate(R.layout.recycler_view_list_row_water, parent, false));
        } else if (viewType == VIEW_TYPE_PICTURE) {
            return new ViewHolderPicture(LayoutInflater.from(context)
                    .inflate(R.layout.recycler_view_list_row_water, parent, false));
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
            float user_weight = dBmanager.selectUserInfoDB().getUserCurrWeight();
            final float waterVolume = dBmanager.selectUserRecordDB(((RecordActivity) context).getToday()).getWaterVolume();
            final int water_cup_count = (int) waterVolume;
            final int water_left_count = (int) ((waterVolume - water_cup_count) * 10);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.height = 80;
            params.width = 80;
            ImageView fullWater;
            ImageView fullWater2;
            ImageView leftWater;

            //<--물 기록 UI 설정 부분

            //<--물컵아이콘설정 1. 10잔이하일때 2. 10잔 초과 20장 미만일때 3. 20잔 이상 시

            if (water_cup_count <= 10) {
                for (int i = 0; i < water_cup_count; i++) {
                    //waterLayout1 물의 잔
                    fullWater = new ImageView(context);
                    fullWater.setBackgroundResource(R.drawable.icon_bottle_gray_10);
                    fullWater.setLayoutParams(params);
                    holderWater.water1Layout1.addView(fullWater);
                    holderWater.water1Layout2.setVisibility(View.GONE);
                }

                //10잔일때 waterLayout2 잔여량

                if (water_left_count > 0) {
                    if (water_cup_count == 10) {
                        holderWater.water1Layout2.setVisibility(View.VISIBLE);
                        leftWater = new ImageView(context);
                        leftWater.setBackgroundResource(calWaterIcon(water_left_count));
                        leftWater.setLayoutParams(params);
                        holderWater.water1Layout2.addView(leftWater);
                    }
                    leftWater = new ImageView(context);
                    leftWater.setBackgroundResource(calWaterIcon(water_left_count));
                    leftWater.setLayoutParams(params);
                    holderWater.water1Layout1.addView(leftWater);
                }


            } else if (water_cup_count + 1 < 20) {
                //waterLayout1 10잔 full
                for (int i = 0; i < water_cup_count; i++) {
                    fullWater = new ImageView(context);
                    fullWater.setBackgroundResource(R.drawable.icon_bottle_gray_10);
                    fullWater.setLayoutParams(params);
                    holderWater.water1Layout1.addView(fullWater);
                }
                //waterLayout2 물의잔
                for (int i = 0; i < water_cup_count - 10; i++) {
                    fullWater = new ImageView(context);
                    fullWater.setBackgroundResource(R.drawable.icon_bottle_gray_10);
                    fullWater.setLayoutParams(params);
                    holderWater.water1Layout2.addView(fullWater);
                }
                //waterLayout2 잔여량
                if (water_cup_count == 20 && water_left_count > 0) {
                    holderWater.water1Layout2.setVisibility(View.VISIBLE);
                    leftWater = new ImageView(context);
                    leftWater.setBackgroundResource(calWaterIcon(water_left_count));
                    leftWater.setLayoutParams(params);
                    holderWater.water1Layout2.addView(leftWater);
                }


            } else if (water_cup_count + 1 >= 20) {
                for (int i = 0; i < 10; i++) {
                    fullWater = new ImageView(context);
                    fullWater2 = new ImageView(context);
                    fullWater.setBackgroundResource(R.drawable.icon_bottle_gray_10);
                    fullWater.setLayoutParams(params);
                    fullWater2.setBackgroundResource(R.drawable.icon_bottle_gray_10);
                    fullWater2.setLayoutParams(params);
                    holderWater.water1Layout1.addView(fullWater);
                    holderWater.water1Layout2.addView(fullWater2);

                }
            }

            //<--마신 잔 수 나타내기

            float waterRecord = dBmanager.selectUserRecordDB(((RecordActivity) context).getToday()).getWaterRecord();
            if (waterRecord == waterVolume) {
                holderWater.water1Layout1.set
            }


            holderWater.btWaterPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float waterRecord = dBmanager.selectUserRecordDB(((RecordActivity) context).getToday()).getWaterRecord();
                    if (waterRecord >= waterVolume) {
                        Toast.makeText(context, "이미 목표를 완료하였습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.d("mox", "record:" + waterRecord);
                    if (water_cup_count < 10) {
                        for (int i = 0; i < 10; i++) {
                            if (holderWater.water1Layout1.getChildAt(i).getTag() != "checked") {
                                holderWater.water1Layout1.getChildAt(i).setBackgroundResource(R.drawable.icon_bottle_checked);
                                holderWater.water1Layout1.getChildAt(i).setTag("checked");
                                waterRecord = i + 1;
                                break;
                            }
                        }
                    } else if (water_cup_count == 10) {
                        if (water_left_count > 0) {
                            holderWater.water1Layout2.getChildAt(0).setBackgroundResource(R.drawable.icon_bottle_checked);
                            holderWater.water1Layout1.getChildAt(0).setTag("checked");
                            waterRecord = waterVolume;
                            Toast.makeText(context, "목표를 완료하였습니다!.", Toast.LENGTH_SHORT).show();
                        } else {
                            waterRecord = 10;
                            Toast.makeText(context, "이미 목표를 완료하였습니다.", Toast.LENGTH_SHORT).show();
                        }

                    } else if (water_cup_count < 20)

                    {
                        for (int i = 0; i < 10; i++) {
                            if (holderWater.water1Layout2.getChildAt(i).getTag() != "checked") {
                                holderWater.water1Layout2.getChildAt(i).setBackgroundResource(R.drawable.icon_bottle_checked);
                                break;
                            }
                        }

                    } else if (water_cup_count >= 20)

                    {
                        Toast.makeText(context, "오늘 목표를 완료하였습니다.", Toast.LENGTH_SHORT).show();

                    }

                    holderWater.water1Layout1.invalidate();
                    holderWater.water1Layout2.invalidate();
                    dBmanager.updateWaterRecord(waterRecord);
                }
            });


        }

    }

    private int calWaterIcon(int water_left_count) {

        switch (water_left_count) {
            case 1:
                return R.drawable.icon_bottle_gray_1;
            case 2:
                return R.drawable.icon_bottle_gray_2;
            case 3:
                return R.drawable.icon_bottle_gray_3;
            case 4:
                return R.drawable.icon_bottle_gray_4;
            case 5:
                return R.drawable.icon_bottle_gray_5;
            case 6:
                return R.drawable.icon_bottle_gray_6;
            case 7:
                return R.drawable.icon_bottle_gray_7;
            case 8:
                return R.drawable.icon_bottle_gray_8;
            case 9:
                return R.drawable.icon_bottle_gray_9;

        }
        throw new IndexOutOfBoundsException();
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
        public LinearLayout water1Layout1;
        public LinearLayout water1Layout2;
        public Button btWaterPlus;
        public Button btWaterMinus;


        public ViewHolderWater(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button);
            expandableLayout = (ExpandableRelativeLayout) v.findViewById(R.id.layout_water);
            water1Layout1 = (LinearLayout) v.findViewById(R.id.layout_water_count_1);
            water1Layout2 = (LinearLayout) v.findViewById(R.id.layout_water_count_2);
            btWaterPlus = (Button) v.findViewById(R.id.bt_water_plus);
            btWaterMinus = (Button) v.findViewById(R.id.bt_water_minus);

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

        public ViewHolderWeight(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button);
            expandableLayout = (ExpandableRelativeLayout) v.findViewById(R.id.layout_weight);
        }
    }

    public static class ViewHolderMeal extends RecyclerView.ViewHolder {
        public TextView textView;
        public RelativeLayout buttonLayout;
        public ExpandableRelativeLayout expandableLayout;

        public ViewHolderMeal(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button);
            expandableLayout = (ExpandableRelativeLayout) v.findViewById(R.id.layout_meal);
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
