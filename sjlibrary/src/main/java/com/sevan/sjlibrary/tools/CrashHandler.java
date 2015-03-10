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

package com.sevan.sjlibrary.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.sevan.sjlibrary.R;
import com.sevan.sjlibrary.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Sevan Joe on 3/10/2015.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private String TAG = CrashHandler.class.getSimpleName();

    private Thread.UncaughtExceptionHandler defaultHandler;
    private Context context;
    private Map<String, String> infoMap = new HashMap<>();

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.getDefault());

    private static CrashHandler INSTANCE = new CrashHandler();

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * register CrashHandler with context
     * @param context context
     */
    public void init(Context context) {
        this.context = context;
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && defaultHandler != null) {
            defaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error: ", e);
            }

            android.os.Process.killProcess(Process.myPid());
            System.exit(1);
        }
    }

    private boolean handleException(Throwable ex) {
        if (null == ex) {
            return  false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                // display exception
                Toast.makeText(context, context.getString(R.string.crash_tip), Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        collectCrashInfo();
        showCrashInfo(ex);
        return true;
    }

    private void collectCrashInfo() {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (null != packageInfo) {
                String versionName = packageInfo.versionName == null ? "null" : packageInfo.versionName;
                String versionCode = String.valueOf(packageInfo.versionCode);
                infoMap.put("versionName", versionName);
                infoMap.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "error occurred during collect package info");
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                infoMap.put(field.getName(), field.get(null).toString());
            } catch (IllegalAccessException e) {
                Log.e(TAG, "error occurred during collect crash info");
            }
        }
    }

    private void showCrashInfo(Throwable ex) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : infoMap.entrySet()) {
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (null != cause) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        stringBuilder.append(writer.toString());

        // save crash info
        saveToFile(stringBuilder.toString());
    }

    private void saveToFile(String string) {
        String time = dateFormat.format(new Date());
        String fileName = "crash-" + time + ".log";
        String filePath = FileUtils.getExternalFileDir(context);
        if (null != filePath) {
            filePath += "/log/";
            File dir = new File(filePath);
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    return;
                }
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(filePath + fileName);
                fileOutputStream.write(string.getBytes());
                fileOutputStream.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
