/*
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.MondialAssistance.Liberty.analytics;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * This is a subclass of {@link Application} used to provide shared objects for this app, such as
 * the {@link Tracker}.
 */
public class AnalyticsApplication /*extends Application*/ {

    private static AnalyticsApplication instance = null;

    private Tracker mTracker;

    /**
     * Método construtor privado.
     */
    private AnalyticsApplication() {
        super();
    }

    /**
     * @return DadosLoginSegurado - unica instancia da classe
     */
    public static AnalyticsApplication getInstance()
    {
        if (instance == null)
        {
            instance = new AnalyticsApplication();
        }
        return instance;
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker(Application application) {
        if (mTracker == null) {
//            GoogleAnalytics analytics = GoogleAnalytics.getInstance();
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(application);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
//            mTracker = analytics.newTracker(R.xml.global_tracker);
            mTracker = analytics.newTracker("UA-3257253-38");
        }
        return mTracker;
    }
}