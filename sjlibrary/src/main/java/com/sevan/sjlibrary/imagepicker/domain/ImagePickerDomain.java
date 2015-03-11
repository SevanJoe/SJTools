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

package com.sevan.sjlibrary.imagepicker.domain;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.sevan.sjlibrary.imagepicker.controller.AlbumController;
import com.sevan.sjlibrary.imagepicker.model.AlbumModel;
import com.sevan.sjlibrary.imagepicker.model.ImageModel;
import com.sevan.sjlibrary.imagepicker.view.PhotoSelectorActivity;

import java.util.List;

/**
 * Created by Sevan Joe on 3/11/2015.
 */
public class ImagePickerDomain {

    private AlbumController albumController;

    public ImagePickerDomain(Context context) {
        albumController = new AlbumController(context);
    }

    public void getReccent(final PhotoSelectorActivity.OnLocalReccentListener listener) {
        final Handler handler = new Handler() {
            @SuppressWarnings("unchecked")
            @Override
            public void handleMessage(Message msg) {
                listener.onPhotoLoaded((List<ImageModel>) msg.obj);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ImageModel> photos = albumController.getRecentImageList();
                Message msg = new Message();
                msg.obj = photos;
                handler.sendMessage(msg);
            }
        }).start();
    }

    /** 获取相册列表 */
    public void updateAlbum(final PhotoSelectorActivity.OnLocalAlbumListener listener) {
        final Handler handler = new Handler() {
            @SuppressWarnings("unchecked")
            @Override
            public void handleMessage(Message msg) {
                listener.onAlbumLoaded((List<AlbumModel>) msg.obj);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<AlbumModel> albums = albumController.getAlbumList();
                Message msg = new Message();
                msg.obj = albums;
                handler.sendMessage(msg);
            }
        }).start();
    }

    /** 获取单个相册下的所有照片信息 */
    public void getAlbum(final String name, final PhotoSelectorActivity.OnLocalReccentListener listener) {
        final Handler handler = new Handler() {
            @SuppressWarnings("unchecked")
            @Override
            public void handleMessage(Message msg) {
                listener.onPhotoLoaded((List<ImageModel>) msg.obj);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ImageModel> photos = albumController.getImageListByAlbum(name);
                Message msg = new Message();
                msg.obj = photos;
                handler.sendMessage(msg);
            }
        }).start();
    }
}
