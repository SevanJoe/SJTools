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
import android.widget.AbsListView.LayoutParams;

import com.sevan.sjlibrary.R;
import com.sevan.sjlibrary.imagepicker.model.ImageModel;
import com.sevan.sjlibrary.imagepicker.ui.widget.ImageItem;

import java.util.ArrayList;

/**
 * Created by Sevan Joe on 2015/3/12.
 */
public class ImagePickerAdapter extends ImageBaseAdapter<ImageModel> {

	private static final int HORIZONTAL_NUM = 3;

    private LayoutParams itemLayoutParams;
    private ImageItem.OnImageClickListener onImageClickListener;
	private ImageItem.OnImageLongClickListener onImageLongClickListener;
    private ImageItem.OnImageCheckChangedListener onImageCheckChangedListener;

	private ImagePickerAdapter(Context context, ArrayList<ImageModel> models) {
		super(context, models);
	}

	public ImagePickerAdapter(Context context, ArrayList<ImageModel> imageModels, int screenWidth,
                              ImageItem.OnImageClickListener onImageClickListener,
                              ImageItem.OnImageLongClickListener onImageLongClickListener,
                              ImageItem.OnImageCheckChangedListener onImageCheckChangedListener) {
		this(context, imageModels);
		this.onImageClickListener = onImageClickListener;
		this.onImageLongClickListener = onImageLongClickListener;
        this.onImageCheckChangedListener = onImageCheckChangedListener;
        setItemWidth(screenWidth);
	}

	public void setItemWidth(int screenWidth) {
		int horizontalSpace = context.getResources().getDimensionPixelSize(R.dimen.sticky_item_horizontal_spacing);
		int itemWidth = (screenWidth - (horizontalSpace * (HORIZONTAL_NUM - 1))) / HORIZONTAL_NUM;
		this.itemLayoutParams = new LayoutParams(itemWidth, itemWidth);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageItem imageItem;
		if (null == convertView || !(convertView instanceof ImageItem)) {
			imageItem = new ImageItem(context, position, onImageClickListener, onImageLongClickListener,
                    onImageCheckChangedListener);
			imageItem.setLayoutParams(itemLayoutParams);
			convertView = imageItem;
		} else {
			imageItem = (ImageItem) convertView;
		}
		imageItem.setImageDrawable(models.get(position));
		imageItem.setSelected(models.get(position).isChecked());
        imageItem.setPosition(position);
		return convertView;
	}
}
