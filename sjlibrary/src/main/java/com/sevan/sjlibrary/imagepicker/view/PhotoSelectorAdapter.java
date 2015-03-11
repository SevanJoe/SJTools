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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;

import com.sevan.sjlibrary.R;
import com.sevan.sjlibrary.imagepicker.model.ImageModel;

import java.util.ArrayList;


/**
 * 
 * @author Aizaz AZ
 *
 */


public class PhotoSelectorAdapter extends MBaseAdapter<ImageModel> {

	private int itemWidth;
	private int horizentalNum = 3;
	private PhotoItem.onPhotoItemCheckedListener listener;
	private LayoutParams itemLayoutParams;
	private PhotoItem.onItemClickListener mCallback;
	private OnClickListener cameraListener;

	private PhotoSelectorAdapter(Context context, ArrayList<ImageModel> models) {
		super(context, models);
	}

	public PhotoSelectorAdapter(Context context, ArrayList<ImageModel> models, int screenWidth, PhotoItem.onPhotoItemCheckedListener listener, PhotoItem.onItemClickListener mCallback,
			OnClickListener cameraListener) {
		this(context, models);
		setItemWidth(screenWidth);
		this.listener = listener;
		this.mCallback = mCallback;
		this.cameraListener = cameraListener;
	}

	/** ����ÿһ��Item�Ŀ�� */
	public void setItemWidth(int screenWidth) {
		int horizentalSpace = context.getResources().getDimensionPixelSize(R.dimen.sticky_item_horizontalSpacing);
		this.itemWidth = (screenWidth - (horizentalSpace * (horizentalNum - 1))) / horizentalNum;
		this.itemLayoutParams = new LayoutParams(itemWidth, itemWidth);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PhotoItem item = null;
		if (convertView == null || !(convertView instanceof PhotoItem)) {
			item = new PhotoItem(context, listener);
			item.setLayoutParams(itemLayoutParams);
			convertView = item;
		} else {
			item = (PhotoItem) convertView;
		}
		item.setImageDrawable(models.get(position));
		item.setSelected(models.get(position).isChecked());
		item.setOnClickListener(mCallback, position);
		return convertView;
	}
}
