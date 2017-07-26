package com.gamesbykevin.breakout.level;

import android.content.SharedPreferences;

import com.gamesbykevin.breakout.R;
import com.gamesbykevin.breakout.activity.BaseActivity;
import com.gamesbykevin.breakout.game.GameHelper;
import com.gamesbykevin.breakout.util.StatDescription;
import com.gamesbykevin.breakout.util.UtilityHelper;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.gamesbykevin.breakout.game.GameHelper.STAT_X;
import static com.gamesbykevin.breakout.game.GameHelper.getLevelDescription;
import static com.gamesbykevin.breakout.game.GameHelper.getStatDescription;
import static com.gamesbykevin.breakout.util.StatDescription.STAT_WIDTH;

/**
 * Created by Kevin on 7/23/2017.
 * Keep track of how many levels we have completed
 */
public class Statistics {

    //list of levels we have beaten
    private ArrayList<Boolean> data;

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
        this.data = (ArrayList<Boolean>)activity.getObjectValue(R.string.game_stats_file_key, ArrayList.class);

        //the list needs to match the total number of levels
        if (getList().size() != count) {

            //continue until the size matches our total count meaning all are there
            while (getList().size() < count) {
                update(getList().size(), false);
            }

            //save our results
            save();
        }
    }

    /**
     * Assign the current level.<br>
     * Also when assigning the level
     * @param index The index in our list of the desired level
     */
    public void setIndex(int index) {
        this.index = index;

        //if we are at the end, start back at the beginning
        if (getIndex() >= getList().size())
            this.index = 0;

        //reset lives whenever we start a new level
        getStatDescription().setDescription(StatDescription.DEFAULT_LIVES);

        //update text word x-coordinate
        GameHelper.LIVES_X = STAT_X + ((String.valueOf(getStatDescription().getStatValue()).length() + 1) * STAT_WIDTH);

        //update level #
        getLevelDescription().setDescription(this.index + 1);
    }

    /**
     * Get the current level
     * @return The index of the level we are currently playing
     */
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

        //if exists update, else add it
        if (index < getList().size()) {
            getList().set(index, result);
        } else {
            getList().add(result);
        }
    }

    /**
     * Get the list
     * @return The indexes of every completed level (true, false)
     */
    public ArrayList<Boolean> getList() {

        //create if still null
        if (this.data == null)
            this.data = new ArrayList<>();

        return this.data;
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