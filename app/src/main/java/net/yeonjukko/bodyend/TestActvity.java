package net.yeonjukko.bodyend;

/**
 * Created by yeonjukko on 16. 3. 11..
 */
public class TestActvity extends RecordActivity {

}
/*
        holder.textView.setText(item.description);
        holder.textView.setTextSize(19);
        holder.textView.setTextColor(context.getResources().getColor(android.R.color.white));
        holder.textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        holder.itemView.setBackground(resource.getDrawable(R.drawable.layout_round_all));
        holder.expandableLayout.setBackground(resource.getDrawable(R.drawable.layout_round_bottom));
        holder.expandableLayout.setInterpolator(item.interpolator);
        holder.expandableLayout.setExpanded(expandState.get(position));
        holder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
@Override
public void onPreOpen() {
        createRotateAnimator(holder.buttonLayout, 0f, 180f).start();
        holder.itemView.setBackground(resource.getDrawable(R.drawable.layout_round_all));
        holder.expandableLayout.setBackground(resource.getDrawable(R.drawable.layout_round_bottom));
        expandState.put(position, true);
        }

@Override
public void onPreClose() {
        createRotateAnimator(holder.buttonLayout, 180f, 0f).start();
        holder.itemView.setBackground(resource.getDrawable(R.drawable.layout_round_all));
        expandState.put(position, false);
        }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        onClickButton(holder.expandableLayout);
        }
        });

        holder.buttonLayout.setRotation(expandState.get(position) ? 180f : 0f);
//        holder.buttonLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                onClickButton(holder.expandableLayout);
//            }
//        });

        if (position == 0) {
        float user_wegith = dBmanager.selectUserInfoDB().getUserCurrWeight();

        float water_volume = user_wegith * 33 / 300;
        int water_cup_count = (int) water_volume;
        int water_left_count = (int) ((water_volume - water_cup_count) * 10);

        LayoutInflater inflater =

        }


*/