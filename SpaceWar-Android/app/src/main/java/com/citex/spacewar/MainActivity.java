package com.citex.spacewar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;


/**
 This class is the main activity context and creates GLSurfaceView which
 renders the games graphics.

 @version 1.0
 @modified 28/10/2023
 @author Lawrence Schmid<BR><BR>

 This file is part of Space War.<BR><BR>

 Space War is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.<BR><BR>

 Space War is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.<BR><BR>

 You should have received a copy of the GNU General Public License
 along with Space War.  If not, see http://www.gnu.org/licenses/.<BR><BR>

 Copyright 2012 Lawrence Schmid
 */

public class MainActivity extends AppCompatActivity {

    /** The surface for displaying OpenGL rendering */
    private GLSurfaceView mGLSurfaceView;
    /** Interface to global information about an application environment. */
    public static Context mContext;
    /** The main activity */
    public static Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        mContext = this;
        mActivity = this;
        mGLSurfaceView = new GLSurfaceViewEvent(this, getAssets());
        setContentView(mGLSurfaceView);
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        super.onPause();
        System.exit(2);
    }

    public void onDestroy() {
        super.onDestroy();
        System.exit(2);
    }
}