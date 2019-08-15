package com.arx.pagingrecyclerview;

import android.content.Context;

import androidx.annotation.Dimension;

/**
 * @author Zeng Derong (derong218@gmail.com)
 * on  2019-8-4 17:08
 */
public class DensityUtil {
    private DensityUtil() {
    }

    /**
     * 将dp转换成px
     *
     * @param context {@link Context}
     * @param dp      {@link Dimension.DP}
     * @return px
     */
    public static int dp2px(Context context, @Dimension(unit = Dimension.DP) int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 将像素转换成dp
     *
     * @param context {@link Context}
     * @param pxValue {@link Dimension.PX}
     * @return dp
     */
    public static int px2dp(Context context, @Dimension(unit = Dimension.PX) int pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
