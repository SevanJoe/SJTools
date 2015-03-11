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

package com.sevan.sjlibrary.imagepicker.model;

/**
 * Created by Sevan Joe on 3/11/2015.
 */
public class AlbumModel {

    private String name;
    private int count;
    private String recent;
    private boolean isCheck;

    public AlbumModel() {

    }

    public AlbumModel(String name) {
        this.name = name;
    }

    public AlbumModel(String name, int count, String recent) {
        this.name = name;
        this.count = count;
        this.recent = recent;
    }

    public AlbumModel(String name, int count, String recent, boolean isCheck) {
        this.name = name;
        this.count = count;
        this.recent = recent;
        this.isCheck = isCheck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getRecent() {
        return recent;
    }

    public void setRecent(String recent) {
        this.recent = recent;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public void increaseCount() {
        ++count;
    }
}
