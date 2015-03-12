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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sevan.sjlibrary.R;
import com.sevan.sjlibrary.imagepicker.model.ImageModel;

import java.util.Random;

/**
 * Created by Sevan Joe on 3/12/2015.
 */
public class ImageItem extends LinearLayout implements OnCheckedChangeListener, OnLongClickListener {

	private ImageView imageView;
	private CheckBox imageCheckBox;

	private OnImageCheckChangedListener onImageCheckChangedListener;
	private OnImageClickListener onImageClickListener;

	private ImageModel imageModel;

	private boolean isCheckAll;
	private int position;

	private DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_picture_loading)
			.showImageOnFail(R.drawable.ic_picture_loadfailed)
			.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565).build();

	private ImageItem(Context context) {
		super(context);
	}

	public ImageItem(Context context, OnImageCheckChangedListener listener) {
		this(context);
		LayoutInflater.from(context).inflate(R.layout.layout_image, this, true);
		this.onImageCheckChangedListener = listener;

		setOnLongClickListener(this);

		imageView = (ImageView) findViewById(R.id.iv_image);
		imageCheckBox = (CheckBox) findViewById(R.id.cb_image);

		imageCheckBox.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
		if (!isCheckAll) {
			onImageCheckChangedListener.onImageCheckChanged(imageModel, compoundButton, isChecked);
		}
		if (isChecked) {
			imageView.setDrawingCacheEnabled(true);
			imageView.buildDrawingCache();
			imageView.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
		} else {
			imageView.clearColorFilter();
		}
		imageModel.setChecked(isChecked);
	}

	public void setImageDrawable(final ImageModel imageModel) {
		this.imageModel = imageModel;
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				ImageLoader.getInstance().displayImage("file://" + imageModel.getOriginalPath(),
						imageView, imageOptions);
			}
		}, new Random().nextInt(10));
	}

	@Override
	public void setSelected(boolean selected) {
		if (imageModel == null) {
			return;
		}
		isCheckAll = true;
		imageCheckBox.setChecked(selected);
		isCheckAll = false;
	}

	public void setOnClickListener(OnImageClickListener onImageClickListener, int position) {
		this.onImageClickListener = onImageClickListener;
		this.position = position;
	}

	public static interface OnImageCheckChangedListener {
		public void onImageCheckChanged(ImageModel imageModel, CompoundButton compoundButton, boolean isChecked);
	}

	public interface OnImageClickListener {
		public void onImageClick(int position);
	}

	@Override
	public boolean onLongClick(View view) {
		if (null != onImageClickListener) {
			onImageClickListener.onImageClick(position);
		}
		return true;
	}
}
