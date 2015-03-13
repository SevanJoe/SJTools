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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.sevan.sjlibrary.R;
import com.sevan.sjlibrary.base.BaseActivity;
import com.sevan.sjlibrary.imagepicker.model.ImageModel;
import com.sevan.sjlibrary.imagepicker.ui.widget.ImagePreview;
import com.sevan.sjlibrary.utils.AnimationUtil;

import java.util.List;

/**
 * Created by Sevan Joe on 2015/3/12.
 */
public class BaseImagePreviewActivity extends BaseActivity implements OnPageChangeListener {

    private Toolbar toolbar;
    private ViewPager viewPager;

	protected List<ImageModel> imageModelList;

	protected int currentIndex;
	protected boolean isUp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imagepreview);

        initViews();

        overridePendingTransition(R.anim.activity_alpha_action_in, 0);
	}

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.vp_image);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void bindData() {
		viewPager.setAdapter(mPagerAdapter);
		viewPager.setCurrentItem(currentIndex);
	}

	private PagerAdapter mPagerAdapter = new PagerAdapter() {

		@Override
		public int getCount() {
			if (imageModelList == null) {
				return 0;
			} else {
				return imageModelList.size();
			}
		}

		@Override
		public View instantiateItem(final ViewGroup container, final int position) {
			ImagePreview imagePreview = new ImagePreview(getApplicationContext());
			container.addView(imagePreview);
			imagePreview.loadImage(imageModelList.get(position));
			imagePreview.setOnClickListener(imageItemOnClickListener);
			return imagePreview;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	};

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		currentIndex = arg0;
		updatePercent();
	}

	protected void updatePercent() {
        getSupportActionBar().setTitle(String.format(getString(R.string.select_count),
                currentIndex + 1, imageModelList.size()));
	}

	private OnClickListener imageItemOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!isUp) {
				new AnimationUtil(getApplicationContext(), R.anim.translate_up)
						.setInterpolator(new LinearInterpolator()).setFillAfter(true).startAnimation(toolbar);
				isUp = true;
			} else {
				new AnimationUtil(getApplicationContext(), R.anim.translate_down_current)
						.setInterpolator(new LinearInterpolator()).setFillAfter(true).startAnimation(toolbar);
				isUp = false;
			}
		}
	};
}
