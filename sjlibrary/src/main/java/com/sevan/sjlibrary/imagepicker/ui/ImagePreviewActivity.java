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

package com.sevan.sjlibrary.imagepicker.ui;

import android.os.Bundle;

import com.sevan.sjlibrary.Constants;
import com.sevan.sjlibrary.R;
import com.sevan.sjlibrary.imagepicker.controller.ImagePickLoader;
import com.sevan.sjlibrary.imagepicker.controller.OnImageLoadListener;
import com.sevan.sjlibrary.imagepicker.model.ImageModel;
import com.sevan.sjlibrary.utils.CommonUtil;

import java.util.List;

/**
 * Created by Sevan Joe on 2015/3/12.
 */
public class ImagePreviewActivity extends BaseImagePreviewActivity implements OnImageLoadListener {

	private ImagePickLoader imagePickLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		imagePickLoader = new ImagePickLoader(getApplicationContext());

		init(getIntent().getExtras());
	}

    @SuppressWarnings("unchecked")
	protected void init(Bundle extras) {
		if (null == extras) {
            return;
        }

		if (extras.containsKey(Constants.IMAGE_LIST)) {
			imageModelList = (List<ImageModel>) extras.getSerializable(Constants.IMAGE_LIST);
			currentIndex = extras.getInt(Constants.IMAGE_POSITION, 0);
			updatePercent();
			bindData();
		} else if (extras.containsKey(Constants.ALBUM)) {
			String albumName = extras.getString(Constants.ALBUM);
			this.currentIndex = extras.getInt(Constants.IMAGE_POSITION);
			if (!CommonUtil.isNull(albumName) && albumName.equals(getString(R.string.recent_photos))) {
				imagePickLoader.loadRecentImageList(this);
			} else {
				imagePickLoader.loadAlbumImageList(albumName, this);
			}
		}
	}

	@Override
	public void onImageLoaded(List<ImageModel> photos) {
		this.imageModelList = photos;
		updatePercent();
		bindData();
	}
}
