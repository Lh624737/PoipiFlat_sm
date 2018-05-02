package com.pospi.util;

import android.content.Context;

/**
 * Created by Qiyan on 2016/6/24.
 * 改变尺寸的大小
 */
public class TurnSize {
    public static int dip2px(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    
}
