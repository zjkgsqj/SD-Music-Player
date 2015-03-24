/**
 * 
 */

package com.example.sdmusicplayer.utils;

import android.content.Context;
import android.content.res.Configuration;

/**
 * @author sdhuang 
 * create date 2015-03-20
 * 工具类
 */
public class Utils {

	/**
     * @param context
     * @return if a Tablet is the device being used
     * 判断是否是平板
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
