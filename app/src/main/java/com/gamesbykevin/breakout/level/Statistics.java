package com.gamesbykevin.breakout.level;

import android.content.SharedPreferences;

import com.gamesbykevin.breakout.R;
import com.gamesbykevin.breakout.activity.BaseActivity;
import com.gamesbykevin.breakout.util.UtilityHelper;
import com.google.gson.reflect.TypeToken;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kevin on 7/23/2017.
 * Keep track of how many levels we have completed
 */

public class Statistics {

    //list of levels we have beaten
    private HashMap<Integer, Boolean> data;

    //our reference to help save the shared preferences
    private final BaseActivity activity;

    //keep track of our level
    private int index = 0;

    /**
     * Default constructor
     * @param activity Object used to write shared preferences
     * @param count The total number of levels in the game
     */
    public Statistics(BaseActivity activity, int count) {

        //store the activity reference
        this.activity = activity;

        //get our data from the shared preferences
        this.data = (HashMap<Integer, Boolean>)activity.getObjectValue(R.string.game_stats_file_key, new TypeToken<HashMap<Integer, Boolean>>(){}.getType());

        //the list needs to match the total number of levels
        if (getList().size() != count) {

            //make sure every level is there
            for (int i = 0; i < count; i++) {

                //if the entry does not exist, add it to the list
                if (getList().get(i) == null)
                    update(i, false);
            }

            //save our results
            save();
        }
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    /**
     * Update our list with the specified result
     * @param result true if we beat the current level, false otherwise
     */
    public void update(boolean result) {
        update(getIndex(), result);
    }

    /**
     * Update our list with the specified result
     * @param index The level we want to update
     * @param result true if we beat the level, false otherwise
     */
    public void update(int index, boolean result) {
        getList().put(index, result);
    }

    /**
     * Get the list
     * @return The indexes of every completed level (true, false)
     */
    private HashMap<Integer, Boolean> getList() {

        //create if still null
        if (this.data == null)
            this.data = new HashMap<>();

        return this.data;
    }

    public List<Boolean> getResults() {
        return (List<Boolean>)getList().values();
    }

    /**
     * Store the data in the shared preferences
     */
    public void save() {

        //get the editor so we can change the shared preferences
        SharedPreferences.Editor editor = activity.getSharedPreferences().edit();

        //convert object to json string
        final String json = activity.GSON.toJson(data);

        //print data
        UtilityHelper.logEvent(json);

        //convert to json string and store in preferences
        editor.putString(activity.getString(R.string.game_stats_file_key), json);

        //make it final by committing the change
        editor.commit();
    }
}