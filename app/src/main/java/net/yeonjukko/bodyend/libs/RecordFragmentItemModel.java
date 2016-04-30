package net.yeonjukko.bodyend.libs;

/**
 * Created by yeonjukko on 16. 3. 16..
 */

import android.animation.TimeInterpolator;


public class RecordFragmentItemModel {
    public final String description;
    public final int colorId1;
    public final int colorId2;
    public final TimeInterpolator interpolator;

    public RecordFragmentItemModel(String description, int colorId1, int colorId2, TimeInterpolator interpolator) {
        this.description = description;
        this.colorId1 = colorId1;
        this.colorId2 = colorId2;
        this.interpolator = interpolator;
    }
}
