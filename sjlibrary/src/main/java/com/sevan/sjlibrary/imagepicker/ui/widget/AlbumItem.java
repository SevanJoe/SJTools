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

package com.sevan.sjlibrary.imagepicker.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sevan.sjlibrary.R;
import com.sevan.sjlibrary.imagepicker.model.AlbumModel;

/**
 * Created by Sevan Joe on 3/11/2015.
 */
public class AlbumItem extends LinearLayout {

	private ImageView coverImageView, checkImageView;
	private TextView nameTextView, countTextView;

	public AlbumItem(Context context) {
		this(context, null);
	}

	public AlbumItem(Context context, AttributeSet attrs) {
		super(context, attrs);
        init(context);
	}

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_album, this, true);

        coverImageView = (ImageView) findViewById(R.id.iv_album_cover);
        checkImageView = (ImageView) findViewById(R.id.iv_album_check);
        nameTextView = (TextView) findViewById(R.id.tv_album_name);
        countTextView = (TextView) findViewById(R.id.tv_album_count);
    }

    /**
     * set album cover image
     * @param path cover image path
     */
	public void setAlbumImage(String path) {
		ImageLoader.getInstance().displayImage("file://" + path, coverImageView);
	}

	public void update(AlbumModel albumModel) {
		setAlbumImage(albumModel.getRecent());
		setName(albumModel.getName());
		setCount(albumModel.getCount());
		isCheck(albumModel.isCheck());
	}

	public void setName(CharSequence title) {
		nameTextView.setText(title);
	}

	public void setCount(int count) {
		countTextView.setHint(count + getContext().getString(R.string.image_count));
	}

	public void isCheck(boolean isCheck) {
		if (isCheck) {
            checkImageView.setVisibility(View.VISIBLE);
        } else {
            checkImageView.setVisibility(View.GONE);
        }
	}
}
