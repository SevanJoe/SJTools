/*
 * Copyright (c) 2015. Sevan Joe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sevan.sjlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.DisplayMetrics;

import com.sevan.sjlibrary.imagepicker.ui.ImagePickerActivity;

/**
 * Created by Sevan Joe on 3/11/2015.
 */
public class CommonUtil {

    public static void launchActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    public static void launchActivity(Context context, Class<?> activity, Bundle bundle) {
        Intent intent = new Intent(context, activity);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void launchActivity(Context context, Class<?> activity, String key, int value) {
        Bundle bundle = new Bundle();
        bundle.putInt(key, value);
        launchActivity(context, activity, bundle);
    }

    public static void launchActivity(Context context, Class<?> activity, String key, String value) {
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        launchActivity(context, activity, bundle);
    }

    public static void launchActivityForResult(Activity context, Class<?> activity, int requestCode) {
        Intent intent = new Intent(context, activity);
        context.startActivityForResult(intent, requestCode);
    }

    public static void launchActivityForResult(Activity context, Class<?> activity, int requestCode, int maxImage) {
        Intent intent = new Intent(context, activity);
        intent.putExtra(ImagePickerActivity.COUNT_MAX, maxImage);
        context.startActivityForResult(intent, requestCode);
    }

    public static void launchActivityForResult(Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
    }

    public static void launchService(Context context, Class<?> service) {
        Intent intent = new Intent(context, service);
        context.startService(intent);
    }

    public static void stopService(Context context, Class<?> service) {
        Intent intent = new Intent(context, service);
        context.stopService(intent);
    }

    /**
     * check the string is null or not
     *
     * @param text text to check
     * @return true null false !null
     */
    public static boolean isNull(CharSequence text) {
	    if (text == null || "".equals(text.toString().trim()) || "null".equals(text)) {
		    return true;
	    }
	    return false;
    }

    /**
     * get screen width
     *
     * @param activity activity
     * @return screen width
     */
    public static int getWidthPixels(Activity activity) {
	    DisplayMetrics displayMetrics = new DisplayMetrics();
	    activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
	    return displayMetrics.widthPixels;
    }

    /**
     * get screen height
     *
     * @param activity activity
     * @return screen height
     */
    public static int getHeightPixels(Activity activity) {
	    DisplayMetrics displayMetrics = new DisplayMetrics();
	    activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
	    return displayMetrics.heightPixels;
    }

    /**
     * get image path by uri
     *
     * @param context context
     * @param uri     uri
     * @return image path
     */
    public static String query(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, new String[]{ImageColumns.DATA}, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex(ImageColumns.DATA));
        cursor.close();
        return path;
    }
}
