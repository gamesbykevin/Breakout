package com.gamesbykevin.breakout;

import com.gamesbykevin.breakout.panel.GamePanel;

import android.app.Activity;
import android.content.Intent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity 
{
    //our game panel
    private GamePanel panel;
    

    /**
     * Called when the activity is first created
     * @param savedInstanceState 
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        //turn the title off
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set the screen to full screen
        super.getWindow().setFlags(
        	WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        	WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        
        //call parent create
        super.onCreate(savedInstanceState);
        
        //if the panel has not been created
        if (getGamePanel() == null)
        {
            //create the game panel
            setGamePanel(new GamePanel(this));

            //add callback to the game panel to intercept events
            getGamePanel().getHolder().addCallback(getGamePanel());
        }

        //set the content view to our game
        setContentView(getGamePanel());
    }
    
    /**
     * Override the finish call
     */
    @Override
    public void finish()
    {
        //cleanup game panel if it exists
        if (getGamePanel() != null)
        {
            getGamePanel().dispose();
            setGamePanel(null);
        }
        
        //call parent
        super.finish();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onStart()
    {
        //call parent
        super.onStart();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onStop()
    {
        //unregister listener
        if (getGamePanel() != null)
        	getGamePanel().getSensorManager().unregisterListener(getGamePanel());
        
        //call parent
        super.onStop();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onDestroy()
    {
        //unregister listener
        if (getGamePanel() != null)
        	getGamePanel().getSensorManager().unregisterListener(getGamePanel());
    	
        //finish the activity
        this.finish();
        
        //perform final cleanup
        super.onDestroy();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onPause()
    {
    	//call parent
        super.onPause();
        
        //unregister listener
        if (getGamePanel() != null)
        	getGamePanel().getSensorManager().unregisterListener(getGamePanel());
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onWindowFocusChanged(final boolean hasFocus)
    {
    	//call parent
    	super.onWindowFocusChanged(true);
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onResume()
    {
    	//call parent
    	super.onResume();
    	
    	//if the sensor exists make sure it is registered
    	if (getGamePanel().getSensor() != null)
    		getGamePanel().getSensorManager().registerListener(getGamePanel(), getGamePanel().getSensor(), SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    /**
     * Navigate to the desired web page
     * @param url The desired url
     */
    public void openWebpage(final String url)
    {
        //create action view intent
        Intent intent = new Intent(Intent.ACTION_VIEW);
        
        //the content will be the web page
        intent.setData(Uri.parse(url.trim()));
        
        //start this new activity
        startActivity(intent);        
    }
    
    /**
     * Get the game panel.
     * @return The object containing our game logic, assets, threads, etc...
     */
    private GamePanel getGamePanel()
    {
        return this.panel;
    }
    
    /**
     * Assign the game panel
     * @param panel The game panel
     */
    private void setGamePanel(final GamePanel panel)
    {
        this.panel = panel;
    }
}