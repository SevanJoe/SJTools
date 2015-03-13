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

package com.sevan.sjlibrary.imagepicker.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sevan Joe on 3/11/2015.
 */
public class ImageBaseAdapter<T> extends BaseAdapter {

	protected Context context;
	protected ArrayList<T> models;

	public ImageBaseAdapter(Context context, ArrayList<T> models) {
		this.context = context;
		if (models == null) {
            this.models = new ArrayList<>();
        } else {
            this.models = models;
        }
	}

	@Override
	public int getCount() {
		if (models != null) {
			return models.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return models.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

    /**
     * update data
     * @param models models
     */
	public void update(List<T> models) {
		if (models == null) {
			return;
		}
		this.models.clear();
		for (T t : models) {
			this.models.add(t);
		}
		notifyDataSetChanged();
	}
}
