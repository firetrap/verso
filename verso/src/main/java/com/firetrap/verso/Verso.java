/*
 * Copyright (c) 2016. Firetrap Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.firetrap.verso;

import android.content.Context;
import android.util.Log;

import com.firetrap.verso.model.GeoInfo;
import com.firetrap.verso.model.ReverseGeoCode;

import java.io.IOException;
import java.util.zip.ZipInputStream;

/**
 * Created by firetrap on 28/11/2016.
 */

public class Verso implements Runnable {

    private final String TAG = "Verso";
    private boolean isInitialized = false;

    private Context context;
    private ReverseGeoCode reverseGeoCode;

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        setupGeoData(context);
    }

    private static class InstanceHolder {

        static Verso verso = new Verso();
    }

    public static Verso getInstance() {

        return InstanceHolder.verso;
    }

    public void setup(Context context) {

        this.context = context;
        this.run();
    }

    private void setupGeoData(Context context) {
        try {

            isInitialized = true;
            this.reverseGeoCode = new ReverseGeoCode(new ZipInputStream(context.getAssets().open("offlineData.zip")), true);
        } catch (IOException e) {

            Log.e(TAG, "Error opening offline reverse geocode file", e);
            new IOException("Error opening offline reverse geocode file");
        }
    }

    public GeoInfo getGeoInfo(double latitude, double longitude) {
        checkInitialization();

        return reverseGeoCode.nearestPlace(latitude, longitude);
    }

    private void checkInitialization() {

        while (!isInitialized) {

            try {

                Thread.sleep(10);
            } catch (InterruptedException ex) {

                Log.e(TAG, "initialization wait thread interrupted", ex);
            }
        }
    }
}
