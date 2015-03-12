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

package com.sevan.sjlibrary.imagepicker.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.sevan.sjlibrary.imagepicker.model.AlbumModel;

import java.util.ArrayList;

/**
 * Created by Sevan Joe on 3/11/2015.
 */
public class AlbumAdapter extends ImageBaseAdapter<AlbumModel> {

	public AlbumAdapter(Context context, ArrayList<AlbumModel> albumModels) {
		super(context, albumModels);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AlbumItem albumItem;
		if (convertView == null) {
			albumItem = new AlbumItem(context);
			convertView = albumItem;
		} else {
			albumItem = (AlbumItem) convertView;
		}
		albumItem.update(models.get(position));
		return convertView;
	}
}
