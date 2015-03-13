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

package com.sevan.sjtools;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sevan.sjlibrary.Constants;
import com.sevan.sjlibrary.tools.CrashHandler;

/**
 * Created by Sevan Joe on 3/10/2015.
 */
public class SJToolsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        registerCrashHandler(getApplicationContext());

        initImageLoader(getApplicationContext());
    }

    private void registerCrashHandler(Context context) {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(context);
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(Constants.IMAGE_WIDTH, Constants.IMAGE_HEIGHT)
                .diskCacheExtraOptions(Constants.IMAGE_WIDTH, Constants.IMAGE_HEIGHT, null).build();
        ImageLoader.getInstance().init(config);
    }
}
