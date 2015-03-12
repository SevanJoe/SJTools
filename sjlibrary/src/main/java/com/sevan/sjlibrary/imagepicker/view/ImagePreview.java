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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sevan.sjlibrary.R;
import com.sevan.sjlibrary.imagepicker.model.ImageModel;
import com.sevan.sjlibrary.polites.GestureImageView;

/**
 * Created by Sevan Joe on 2015/3/12.
 */
public class ImagePreview extends LinearLayout implements OnClickListener {

	private ProgressBar loadingProgressBar;
	private GestureImageView gestureImageView;

	private OnClickListener onClickListener;

	public ImagePreview(Context context) {
		this(context, null);
	}

	public ImagePreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.view_imagepreview, this, true);

		loadingProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
		gestureImageView = (GestureImageView) findViewById(R.id.iv_content);
		gestureImageView.setOnClickListener(this);
	}

	public void loadImage(ImageModel imageModel) {
		loadImage("file://" + imageModel.getOriginalPath());
	}

	private void loadImage(String path) {
		ImageLoader.getInstance().loadImage(path, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				gestureImageView.setImageBitmap(loadedImage);
				loadingProgressBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				gestureImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_picture_loadfailed));
				loadingProgressBar.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	@Override
	public void onClick(View v) {
		if (null != onClickListener && v.getId() == R.id.iv_content)
			onClickListener.onClick(gestureImageView);
	}
}
