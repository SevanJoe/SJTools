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

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sevan.sjlibrary.R;
import com.sevan.sjlibrary.imagepicker.controller.ImagePickLoader;
import com.sevan.sjlibrary.imagepicker.controller.OnAlbumLoadListener;
import com.sevan.sjlibrary.imagepicker.controller.OnImageLoadListener;
import com.sevan.sjlibrary.imagepicker.model.AlbumModel;
import com.sevan.sjlibrary.imagepicker.model.ImageModel;
import com.sevan.sjlibrary.utils.AnimationUtil;
import com.sevan.sjlibrary.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sevan Joe on 2015/3/12.
 */
public class ImagePickerActivity extends ActionBarActivity implements
		ImageItem.OnImageClickListener, ImageItem.OnImageCheckChangedListener,
        OnItemClickListener, OnClickListener {

	public static final String KEY_MAX = "key_max";
	private static final int DEFAULT_MAX = 9;
	private int maxCount;

	public static final int REQUEST_PHOTO = 0;
	private static final int REQUEST_CAMERA = 1;

    private Toolbar toolbar;
	private GridView imageGridView;
	private TextView albumTextView;
	private TextView previewTextView;
	private ImagePickLoader imagePickLoader;
	private ImagePickerAdapter imagePickerAdapter;
	private AlbumAdapter albumAdapter;
	private RelativeLayout albumLayout;
	private ArrayList<ImageModel> selectedImageModels;
//	private TextView numberTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imagepicker);

		if (getIntent().getExtras() != null) {
			maxCount = getIntent().getIntExtra(KEY_MAX, DEFAULT_MAX);
		}

        initViews();

		imagePickLoader = new ImagePickLoader(getApplicationContext());

		selectedImageModels = new ArrayList<>();

		imageGridView = (GridView) findViewById(R.id.gv_images);
		ListView albumListView = (ListView) findViewById(R.id.lv_album);
//		Button confirmButton = (Button) findViewById(R.id.btn_confirm);
		albumTextView = (TextView) findViewById(R.id.tv_album);
		previewTextView = (TextView) findViewById(R.id.tv_preview);
		albumLayout = (RelativeLayout) findViewById(R.id.rl_album);
//		numberTextView = (TextView) findViewById(R.id.tv_number);

//		confirmButton.setOnClickListener(this);
		albumTextView.setOnClickListener(this);
		previewTextView.setOnClickListener(this);

		imagePickerAdapter = new ImagePickerAdapter(getApplicationContext(),
				new ArrayList<ImageModel>(), CommonUtil.getWidthPixels(this),
				this, this, this);
		imageGridView.setAdapter(imagePickerAdapter);

		albumAdapter = new AlbumAdapter(getApplicationContext(), new ArrayList<AlbumModel>());
		albumListView.setAdapter(albumAdapter);
		albumListView.setOnItemClickListener(this);

//		findViewById(R.id.ll_back).setOnClickListener(this);

		imagePickLoader.loadRecentImageList(recentListener);
		imagePickLoader.loadAlbumList(albumListener);
	}

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

	@Override
	public void onClick(View v) {
//		if (v.getId() == R.id.btn_confirm)
//			ok();
		if (v.getId() == R.id.tv_album)
			album();
		else if (v.getId() == R.id.tv_preview)
			preview();
		else if (v.getId() == R.id.tv_camera_vc)
			catchPicture();
//		else if (v.getId() == R.id.ll_back)
//			finish();
	}

	private void catchPicture() {
		CommonUtil.launchActivityForResult(this, new Intent(
				MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
			ImageModel photoModel = new ImageModel(CommonUtil.query(
                    getApplicationContext(), data.getData()));
			if (selectedImageModels.size() >= maxCount) {
				Toast.makeText(this, "max count", Toast.LENGTH_SHORT).show();
				photoModel.setChecked(false);
				imagePickerAdapter.notifyDataSetChanged();
			} else {
				if (!selectedImageModels.contains(photoModel)) {
					selectedImageModels.add(photoModel);
				}
			}
			ok();
		}
	}

	private void ok() {
		if (selectedImageModels.isEmpty()) {
			setResult(RESULT_CANCELED);
		} else {
			Intent data = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("imageModelList", selectedImageModels);
			data.putExtras(bundle);
			setResult(RESULT_OK, data);
		}
		finish();
	}

	private void preview() {
		Bundle bundle = new Bundle();
		bundle.putSerializable("imageModelList", selectedImageModels);
		CommonUtil.launchActivity(this, ImagePreviewActivity.class, bundle);
	}

	private void album() {
		if (albumLayout.getVisibility() == View.GONE) {
			popAlbum();
		} else {
			hideAlbum();
		}
	}

	private void popAlbum() {
		albumLayout.setVisibility(View.VISIBLE);
		new AnimationUtil(getApplicationContext(), R.anim.translate_up_current)
				.setLinearInterpolator().startAnimation(albumLayout);
	}

	private void hideAlbum() {
		new AnimationUtil(getApplicationContext(), R.anim.translate_down)
				.setLinearInterpolator().startAnimation(albumLayout);
		albumLayout.setVisibility(View.GONE);
	}

	private void reset() {
		selectedImageModels.clear();
//		numberTextView.setText("(0)");
		previewTextView.setEnabled(false);
	}

	@Override
	public void onImageClick(int position) {
		Bundle bundle = new Bundle();
		if (albumTextView.getText().toString().equals(getString(R.string.recent_photos)))
			bundle.putInt("position", position - 1);
		else
			bundle.putInt("position", position);
		bundle.putString("album", albumTextView.getText().toString());
		CommonUtil.launchActivity(this, ImagePreviewActivity.class, bundle);
	}

	@Override
	public void onImageCheckChanged(ImageModel imageModel, CompoundButton compoundButton, boolean isChecked) {
		if (isChecked) {
			if (!selectedImageModels.contains(imageModel))
				selectedImageModels.add(imageModel);
			previewTextView.setEnabled(true);
		} else {
			selectedImageModels.remove(imageModel);
		}
//		numberTextView.setText("(" + selectedImageModels.size() + ")");

		if (selectedImageModels.isEmpty()) {
			previewTextView.setEnabled(false);
			previewTextView.setText("Ԥ��");
		}
	}

	@Override
	public void onBackPressed() {
		if (albumLayout.getVisibility() == View.VISIBLE) {
			hideAlbum();
		} else
			super.onBackPressed();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		AlbumModel current = (AlbumModel) parent.getItemAtPosition(position);
		for (int i = 0; i < parent.getCount(); i++) {
			AlbumModel album = (AlbumModel) parent.getItemAtPosition(i);
			if (i == position)
				album.setCheck(true);
			else
				album.setCheck(false);
		}
		albumAdapter.notifyDataSetChanged();
		hideAlbum();
		albumTextView.setText(current.getName());

		if (current.getName().equals(getString(R.string.recent_photos)))
			imagePickLoader.loadRecentImageList(recentListener);
		else
			imagePickLoader.getAlbum(current.getName(), recentListener);
	}

	private OnAlbumLoadListener albumListener = new OnAlbumLoadListener() {
		@Override
		public void onAlbumLoaded(List<AlbumModel> albumModelList) {
			albumAdapter.update(albumModelList);
		}
	};

	private OnImageLoadListener recentListener = new OnImageLoadListener() {
		@Override
		public void onImageLoaded(List<ImageModel> imageModelList) {
			for (ImageModel model : imageModelList) {
				if (selectedImageModels.contains(model)) {
					model.setChecked(true);
				}
			}
			imagePickerAdapter.update(imageModelList);
			imageGridView.smoothScrollToPosition(0);
			// reset(); //--keep selectedImageModels imageModelList
		}
    };
}
