package com.gamesbykevin.breakout.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import static com.gamesbykevin.breakout.activity.MainActivity.DEBUG;

/**
 * Created by Kevin on 7/19/2017.
 */
public class UtilityHelper {

    public static void handleException(final Exception exception) {

        //if not debugging, don't continue
        if (!DEBUG)
            return;

        //log as error
        Log.e("Breakout", exception.getMessage(), exception);

        //handle process
        exception.printStackTrace();
    }

    public static void logEvent(final String message) {

        //if not debugging, don't continue
        if (!DEBUG)
            return;

        //don't do anything if null
        if (message == null)
            return;

        //length limit of each line we print
        int maxLogSize = 4000;

        //if the string is too long
        if (message.length() > maxLogSize) {

            //we will display a portion at a time
            for(int i = 0; i <= message.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i+1) * maxLogSize;
                end = end > message.length() ? message.length() : end;
                Log.i("Breakout", message.substring(start, end));
            }

        } else {
            //log string as information
            Log.i("Breakout", message);
        }
    }

    public static void displayMessage(final Context context, final String message) {

        //if not debugging, don't continue
        if (!DEBUG)
            return;

        //show text
        Toast.makeText(context, message , Toast.LENGTH_SHORT).show();
    }
}