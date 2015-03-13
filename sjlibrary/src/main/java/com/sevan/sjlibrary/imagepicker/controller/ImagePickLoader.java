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

package com.sevan.sjlibrary.imagepicker.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.sevan.sjlibrary.imagepicker.model.AlbumModel;
import com.sevan.sjlibrary.imagepicker.model.ImageModel;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Sevan Joe on 3/11/2015.
 */
public class ImagePickLoader {

	private AlbumController albumController;

	public ImagePickLoader(Context context) {
		albumController = new AlbumController(context);
	}

	public void loadRecentImageList(OnImageLoadListener onImageLoadListener) {
		final ImageHandler imageHandler = new ImageHandler(onImageLoadListener);
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<ImageModel> imageModelList = albumController.getRecentImageList();
				Message msg = new Message();
				msg.obj = imageModelList;
				imageHandler.sendMessage(msg);
			}
		}).start();
	}

	public void loadAlbumList(OnAlbumLoadListener onAlbumLoadListener) {
		final AlbumHandler albumHandler = new AlbumHandler(onAlbumLoadListener);
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<AlbumModel> albumModelList = albumController.getAlbumList();
				Message msg = new Message();
				msg.obj = albumModelList;
				albumHandler.sendMessage(msg);
			}
		}).start();
	}

	public void loadAlbumImageList(final String name, OnImageLoadListener onImageLoadListener) {
		final ImageHandler imageHandler = new ImageHandler(onImageLoadListener);
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<ImageModel> imageModelList = albumController.getImageListByAlbum(name);
				Message msg = new Message();
				msg.obj = imageModelList;
				imageHandler.sendMessage(msg);
			}
		}).start();
	}

	private static class AlbumHandler extends Handler {
		private final WeakReference<OnAlbumLoadListener> onAlbumLoadListener;

		public AlbumHandler(OnAlbumLoadListener albumLoadListener) {
			this.onAlbumLoadListener = new WeakReference<>(albumLoadListener);
		}

        @SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			OnAlbumLoadListener albumLoadListener = onAlbumLoadListener.get();
			if (null != albumLoadListener) {
                albumLoadListener.onAlbumLoaded((List<AlbumModel>) msg.obj);
			}
		}
	}

	private static class ImageHandler extends Handler {
		private final WeakReference<OnImageLoadListener> onImageLoadListener;

		public ImageHandler(OnImageLoadListener imageLoadListener) {
			this.onImageLoadListener = new WeakReference<>(imageLoadListener);
		}

        @SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			OnImageLoadListener imageLoadListener = onImageLoadListener.get();
			if (null != imageLoadListener) {
				imageLoadListener.onImageLoaded((List<ImageModel>) msg.obj);
			}
		}
	}
}
