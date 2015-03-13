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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.sevan.sjlibrary.Constants;
import com.sevan.sjlibrary.R;
import com.sevan.sjlibrary.base.BaseActivity;
import com.sevan.sjlibrary.imagepicker.controller.ImagePickLoader;
import com.sevan.sjlibrary.imagepicker.controller.OnAlbumLoadListener;
import com.sevan.sjlibrary.imagepicker.controller.OnImageLoadListener;
import com.sevan.sjlibrary.imagepicker.model.AlbumModel;
import com.sevan.sjlibrary.imagepicker.model.ImageModel;
import com.sevan.sjlibrary.imagepicker.ui.adapter.AlbumAdapter;
import com.sevan.sjlibrary.imagepicker.ui.adapter.ImagePickerAdapter;
import com.sevan.sjlibrary.imagepicker.ui.widget.ImageItem;
import com.sevan.sjlibrary.utils.AnimationUtil;
import com.sevan.sjlibrary.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sevan Joe on 2015/3/12.
 */
public class ImagePickerActivity extends BaseActivity implements
        ImageItem.OnImageClickListener, ImageItem.OnImageLongClickListener,
        ImageItem.OnImageCheckChangedListener,
        OnItemClickListener, OnClickListener {

	public static final String COUNT_MAX = "key_max";
	private static final int DEFAULT_MAX = 9;
	private int maxCount;

	public static final int REQUEST_PHOTO = 0;
	private static final int REQUEST_CAMERA = 1;

    private MenuItem countMenuItem;
    private GridView imageGridView;
    private ButtonFlat albumButton;
    private ButtonRectangle previewButton;
    private RelativeLayout albumLayout;
    private ListView albumListView;

	private ImagePickLoader imagePickLoader;
	private ImagePickerAdapter imagePickerAdapter;
	private AlbumAdapter albumAdapter;
	private ArrayList<ImageModel> selectedImageModels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imagepicker);

		if (getIntent().getExtras() != null) {
			maxCount = getIntent().getIntExtra(COUNT_MAX, DEFAULT_MAX);
		}

        initViews();

		imagePickLoader = new ImagePickLoader(getApplicationContext());
        imagePickLoader.loadRecentImageList(recentListener);
        imagePickLoader.loadAlbumList(albumListener);

		selectedImageModels = new ArrayList<>();
	}

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageGridView = (GridView) findViewById(R.id.gv_images);
        albumButton = (ButtonFlat) findViewById(R.id.btn_album);
        previewButton = (ButtonRectangle) findViewById(R.id.btn_preview);
        albumLayout = (RelativeLayout) findViewById(R.id.rl_album);
        albumListView = (ListView) findViewById(R.id.lv_album);

        imagePickerAdapter = new ImagePickerAdapter(getApplicationContext(),
                new ArrayList<ImageModel>(), CommonUtil.getWidthPixels(this), this, this, this);
        imageGridView.setAdapter(imagePickerAdapter);

        albumButton.setOnClickListener(this);
        previewButton.setOnClickListener(this);

        albumAdapter = new AlbumAdapter(getApplicationContext(), new ArrayList<AlbumModel>());
        albumListView.setAdapter(albumAdapter);
        albumListView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.imagepicker, menu);
        countMenuItem = menu.findItem(R.id.action_count);
        updateSelectCount();
        return super.onCreateOptionsMenu(menu);
    }

    private void updateSelectCount() {
        countMenuItem.setTitle(String.format(getString(R.string.select_count),
                selectedImageModels.size(), maxCount));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home || id == R.id.action_select) {
            select();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void select() {
        if (selectedImageModels.isEmpty()) {
            setResult(RESULT_CANCELED);
        } else {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.IMAGE_LIST, selectedImageModels);
            data.putExtras(bundle);
            setResult(RESULT_OK, data);
        }
        finish();
    }

    @Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_album) {
            album();
        }
		else if (v.getId() == R.id.btn_preview) {
            preview();
        }
	}

    private void preview() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.IMAGE_LIST, selectedImageModels);
        CommonUtil.launchActivity(this, ImagePreviewActivity.class, bundle);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
			ImageModel photoModel = new ImageModel(CommonUtil.query(
                    getApplicationContext(), data.getData()));
			if (selectedImageModels.size() >= maxCount) {
                showMaxTip();
				photoModel.setChecked(false);
				imagePickerAdapter.notifyDataSetChanged();
			} else {
				if (!selectedImageModels.contains(photoModel)) {
					selectedImageModels.add(photoModel);
				}
			}
			select();
		}
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

    private void showMaxTip() {
        Toast.makeText(this, String.format(getString(R.string.select_max_count), maxCount),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onImageClick(int position) {
        if (selectedImageModels.size() < maxCount) {
            return true;
        } else {
            if (((ImageModel) imagePickerAdapter.getItem(position)).isChecked()) {
                return true;
            }
            showMaxTip();
            return false;
        }
    }

	@Override
	public void onImageLongClick(int position) {
		Bundle bundle = new Bundle();
        bundle.putInt(Constants.IMAGE_POSITION, position);
//		if (albumTextView.getText().toString().equals(getString(R.string.recent_photos))) {
//            bundle.putInt("position", position - 1);
//        } else {
//        }
		bundle.putString(Constants.ALBUM, albumButton.getText().toString());
		CommonUtil.launchActivity(this, ImagePreviewActivity.class, bundle);
	}

	@Override
	public void onImageCheckChanged(ImageModel imageModel, CompoundButton compoundButton, boolean isChecked) {
		if (isChecked) {
			if (!selectedImageModels.contains(imageModel)) {
                selectedImageModels.add(imageModel);
            }
            previewButton.setClickable(true);
		} else {
			selectedImageModels.remove(imageModel);
		}

		if (selectedImageModels.isEmpty()) {
            previewButton.setClickable(false);
            previewButton.setText(getString(R.string.preview));
		} else {
            previewButton.setText(String.format(getString(R.string.preview_count), selectedImageModels.size()));
        }

        updateSelectCount();
	}

	@Override
	public void onBackPressed() {
		if (albumLayout.getVisibility() == View.VISIBLE) {
			hideAlbum();
		} else {
            super.onBackPressed();
        }
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		AlbumModel current = (AlbumModel) parent.getItemAtPosition(position);
		for (int i = 0; i < parent.getCount(); i++) {
			AlbumModel album = (AlbumModel) parent.getItemAtPosition(i);
			if (i == position) {
                album.setCheck(true);
            } else {
                album.setCheck(false);
            }
		}
		albumAdapter.notifyDataSetChanged();
		hideAlbum();
		albumButton.setText(current.getName());

		if (current.getName().equals(getString(R.string.recent_photos)))
			imagePickLoader.loadRecentImageList(recentListener);
		else
			imagePickLoader.loadAlbumImageList(current.getName(), recentListener);
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
		}
    };
}
