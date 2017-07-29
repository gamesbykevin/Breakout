package com.gamesbykevin.breakout.activity;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import com.gamesbykevin.breakout.R;
import com.gamesbykevin.breakout.util.UtilityHelper;

public class OptionsActivity extends BaseActivity {

    //has the activity been paused
    private boolean paused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_options);

        //retrieve our buttons so we can update based on current setting (shared preferences)
        ToggleButton buttonSound = (ToggleButton)findViewById(R.id.ToggleButtonSound);
        ToggleButton buttonVibrate = (ToggleButton)findViewById(R.id.ToggleButtonVibrate);
        ToggleButton buttonControl = (ToggleButton)findViewById(R.id.ToggleButtonControl);

        //update our buttons accordingly
        buttonSound.setChecked(getBooleanValue(R.string.sound_file_key));
        buttonVibrate.setChecked(getBooleanValue(R.string.vibrate_file_key));
        buttonControl.setChecked(getBooleanValue(R.string.control_file_key));
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop sound
        super.stopSound();

        //flag paused
        paused = true;
    }

    @Override
    public void onResume() {
        super.onResume();

        //play sound
        super.playSong(R.raw.menu);

        //flag false
        paused = false;
    }

    /**
     * Override the back pressed so we save the shared preferences
     */
    @Override
    public void onBackPressed() {

        try {

            //get the editor so we can change the shared preferences
            Editor editor = getSharedPreferences().edit();

            //store the sound setting based on the toggle button
            editor.putBoolean(getString(R.string.sound_file_key), ((ToggleButton)findViewById(R.id.ToggleButtonSound)).isChecked());

            //store the vibrate setting based on the toggle button
            editor.putBoolean(getString(R.string.vibrate_file_key), ((ToggleButton)findViewById(R.id.ToggleButtonVibrate)).isChecked());

            //store the control setting based on the toggle button
            editor.putBoolean(getString(R.string.control_file_key), ((ToggleButton)findViewById(R.id.ToggleButtonControl)).isChecked());

            //make it final by committing the change
            editor.commit();

        } catch (Exception ex) {

            //handle exception
            UtilityHelper.handleException(ex);
        }

        //call parent function
        super.onBackPressed();
    }

    public void onClickVibrate(View view) {

        //get the button
        ToggleButton button = (ToggleButton)view.findViewById(R.id.ToggleButtonVibrate);

        //if the button is checked we will vibrate the phone
        if (button.isChecked()) {
            super.vibrate(true);
        }
    }

    public void onClickSound(View view) {

        //get the button
        ToggleButton button = (ToggleButton)view.findViewById(R.id.ToggleButtonSound);

        if (!button.isChecked())
            super.stopSound();
    }

    public void onClickControl(View view) {

        //get the button
        //ToggleButton button = (ToggleButton)view.findViewById(R.id.ToggleButtonControl);
    }
}