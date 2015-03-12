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

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;

import com.sevan.sjlibrary.R;
import com.sevan.sjlibrary.imagepicker.model.AlbumModel;
import com.sevan.sjlibrary.imagepicker.model.ImageModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sevan Joe on 3/11/2015.
 */
public class AlbumController {

	private Context context;
	private ContentResolver contentResolver;

	public AlbumController(Context context) {
		this.context = context;
		contentResolver = context.getContentResolver();
	}

	public List<ImageModel> getRecentImageList() {
		Cursor cursor = contentResolver.query(Media.EXTERNAL_CONTENT_URI,
				new String[]{ImageColumns.DATA, ImageColumns.DATE_ADDED, ImageColumns.SIZE},
				null, null, ImageColumns.DATE_ADDED);
		if (null == cursor || !cursor.moveToNext()) {
			return new ArrayList<>();
		}
		List<ImageModel> imageModelList = new ArrayList<>();
		cursor.moveToLast();
		do {
			if (cursor.getLong(cursor.getColumnIndex(ImageColumns.SIZE)) > 1024 * 10) {
				ImageModel imageModel = new ImageModel();
				imageModel.setOriginalPath(cursor.getString(cursor.getColumnIndex(ImageColumns.DATA)));
				imageModelList.add(imageModel);
			}
		} while (cursor.moveToPrevious());
		cursor.close();
		return imageModelList;
	}

	public List<AlbumModel> getAlbumList() {
		List<AlbumModel> albumModelList = new ArrayList<>();
		Map<String, AlbumModel> albumModelMap = new HashMap<>();
		Cursor cursor = contentResolver.query(Media.EXTERNAL_CONTENT_URI,
				new String[]{ImageColumns.DATA, ImageColumns.BUCKET_DISPLAY_NAME, ImageColumns.SIZE},
				null, null, null);
		if (null == cursor || !cursor.moveToNext()) {
			return new ArrayList<>();
		}
		cursor.moveToLast();
		AlbumModel currentAlbum = new AlbumModel(context.getString(R.string.recent_photos), 0, cursor.getString(cursor.getColumnIndex(ImageColumns.DATA)), true);
		albumModelList.add(currentAlbum);
		do {
			if (cursor.getInt(cursor.getColumnIndex(ImageColumns.SIZE)) < 1024 * 10) {
				continue;
			}
			currentAlbum.increaseCount();
			String name = cursor.getString(cursor.getColumnIndex(ImageColumns.BUCKET_DISPLAY_NAME));
			if (albumModelMap.keySet().contains(name)) {
				albumModelMap.get(name).increaseCount();
			} else {
				AlbumModel albumModel = new AlbumModel(name, 1, cursor.getString(cursor.getColumnIndex(ImageColumns.DATA)));
				albumModelMap.put(name, albumModel);
				albumModelList.add(albumModel);
			}
		} while (cursor.moveToPrevious());
		cursor.close();
		return albumModelList;
	}

	public List<ImageModel> getImageListByAlbum(String name) {
		Cursor cursor = contentResolver.query(Media.EXTERNAL_CONTENT_URI,
				new String[]{ImageColumns.BUCKET_DISPLAY_NAME, ImageColumns.DATA, ImageColumns.DATE_ADDED, ImageColumns.SIZE},
				"bucket_display_name = ?", new String[]{name}, ImageColumns.DATE_ADDED);
		if (null == cursor || !cursor.moveToNext()) {
			return new ArrayList<>();
		}
		List<ImageModel> imageModelList = new ArrayList<>();
		cursor.moveToLast();
		do {
			if (cursor.getLong(cursor.getColumnIndex(ImageColumns.SIZE)) > 1024 * 10) {
				ImageModel imageModel = new ImageModel();
				imageModel.setOriginalPath(cursor.getString(cursor.getColumnIndex(ImageColumns.DATA)));
				imageModelList.add(imageModel);
			}
		} while (cursor.moveToPrevious());
		cursor.close();
		return imageModelList;
	}
}
