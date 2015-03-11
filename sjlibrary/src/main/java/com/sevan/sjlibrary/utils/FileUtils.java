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

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Sevan Joe on 3/10/2015.
 */
public class FileUtils {

    private static boolean isExternalMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * get external file directory of the application
     * @param context context
     * @return path of the directory
     */
    public static String getExternalFileDir(Context context) {
        if (!isExternalMounted()) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        File file = context.getExternalCacheDir();
        if (null != file) {
            stringBuilder.append(file.getAbsolutePath()).append(File.separator);
        } else {
            stringBuilder.append(Environment.getExternalStorageDirectory().getParent()).append("/Android/data/")
                    .append(context.getPackageName()).append("/files").append(File.separator);
        }
        return stringBuilder.toString();
    }
}
