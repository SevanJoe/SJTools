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

import android.util.Log;

import com.sevan.sjlibrary.BuildConfig;

/**
 * Created by Sevan Joe on 3/11/2015.
 */
public class LogUtil {

    private static final String TAG = "SJ Library";

    public static void d(String debugInfo) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, debugInfo);
        }
    }

    public static void e(String errorInfo) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, errorInfo);
        }
    }

    public static void e(String errorInfo, Throwable throwable) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, errorInfo, throwable);
        }
    }
}
