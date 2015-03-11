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

package com.sevan.sjlibrary.utils;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * Created by Sevan Joe on 3/11/2015.
 */
public class AnimationUtils implements AnimationListener {

    private Animation animation;
    private OnAnimationEndListener animationEndListener;
    private OnAnimationStartListener animationStartListener;
    private OnAnimationRepeatListener animationRepeatListener;

    public AnimationUtils(Context context, int resId) {
        this.animation = android.view.animation.AnimationUtils.loadAnimation(context, resId);
        this.animation.setAnimationListener(this);
    }

    public AnimationUtils(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
        animation = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
    }

    public static void startAnimation(int resId, View view) {
        view.setBackgroundResource(resId);
        ((AnimationDrawable) view.getBackground()).start();
    }

    public AnimationUtils setStartOffSet(long startOffset) {
        animation.setStartOffset(startOffset);
        return this;
    }

    public AnimationUtils setInterpolator(Interpolator i) {
        animation.setInterpolator(i);
        return this;
    }

    public AnimationUtils setLinearInterpolator() {
        animation.setInterpolator(new LinearInterpolator());
        return this;
    }

    public void startAnimation(View view) {
        view.startAnimation(animation);
    }

    public AnimationUtils setDuration(long durationMillis) {
        animation.setDuration(durationMillis);
        return this;
    }

    public AnimationUtils setFillAfter(boolean fillAfter) {
        animation.setFillAfter(fillAfter);
        return this;
    }

    public AnimationUtils setOnAnimationEndLinstener(
            OnAnimationEndListener listener) {
        this.animationEndListener = listener;
        return this;
    }

    public AnimationUtils setOnAnimationStartLinstener(
            OnAnimationStartListener listener) {
        this.animationStartListener = listener;
        return this;
    }

    public AnimationUtils setOnAnimationRepeatLinstener(
            OnAnimationRepeatListener listener) {
        this.animationRepeatListener = listener;
        return this;
    }

    public void setAnimationListener(AnimationListener animationListener) {
        animation.setAnimationListener(animationListener);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if (this.animationStartListener != null) {
            this.animationStartListener.onAnimationStart(animation);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (this.animationEndListener != null) {
            this.animationEndListener.onAnimationEnd(animation);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        if (this.animationRepeatListener != null) {
            this.animationRepeatListener.onAnimationRepeat(animation);
        }
    }

    public interface OnAnimationEndListener {
        void onAnimationEnd(Animation animation);
    }

    public interface OnAnimationStartListener {
        void onAnimationStart(Animation animation);
    }

    public interface OnAnimationRepeatListener {
        void onAnimationRepeat(Animation animation);
    }

}