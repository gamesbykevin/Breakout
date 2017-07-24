package com.gamesbykevin.breakout.util;

import com.gamesbykevin.breakout.entity.Entity;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Kevin on 7/15/2017.
 */
public class StatDescription extends Entity {

    //the dimensions of each number character animation
    public static final int ANIMATION_WIDTH = 66;
    public static final int ANIMATION_HEIGHT = 83;

    private final static float RATIO = 0.35F;

    private final static int STAT_WIDTH = (int)(ANIMATION_WIDTH * RATIO);
    private final static int STAT_HEIGHT = (int)(ANIMATION_HEIGHT * RATIO);

    //our array object for each digit in our score
    private ArrayList<Character> characters;

    //the text to display
    private String desc = "";

    //track the value
    private long statValue = -1;

    private double anchorX = 0;

    //the total number of characters we need access to render
    public static final int TOTAL_CHARACTERS = 12;

    //object to format the text displayed
    private static StringBuilder BUILDER = new StringBuilder();

    /**
     * Default constructor
     */
    public StatDescription()
    {
        this(STAT_WIDTH, STAT_HEIGHT);
    }

    public StatDescription(final int width, final int height) {
        super(width, height);

        //create a new numbers list
        this.characters = new ArrayList<>();

        //set the dimensions
        setDefaultDimensions();
    }

    public void setDefaultDimensions() {

        //set the dimensions
        super.setWidth(STAT_WIDTH);
        super.setHeight(STAT_HEIGHT);
    }

    public void setAnchorX(final double anchorX) {
        this.anchorX = anchorX;
    }

    public long getStatValue() {
        return this.statValue;
    }

    public void setDescription(long newStatValue) {
        setDescription(newStatValue, false);
    }

    public void setDescription(long newStatValue, boolean reset) {

        //reset stat value
        if (reset)
            this.statValue = -1;

        setDescription(String.valueOf(newStatValue));
    }

    public void setDescription(String desc)
    {
        /*
        //disable any unnecessary digits
        for (int i = desc.length(); i < characters.size(); i++)
        {
            characters.get(i).enabled = false;
        }

        //check each character so we can map the animations
        for (int i = 0; i < desc.length(); i++)
        {
            final int textureId;

            //identify which animation
            switch (desc.charAt(i))
            {
                case '0':
                    textureId = TEXTURES[Block.VALUES.length + 0];
                    break;

                case '1':
                    textureId = TEXTURES[Block.VALUES.length + 1];
                    break;

                case '2':
                    textureId = TEXTURES[Block.VALUES.length + 2];
                    break;

                case '3':
                    textureId = TEXTURES[Block.VALUES.length + 3];
                    break;

                case '4':
                    textureId = TEXTURES[Block.VALUES.length + 4];
                    break;

                case '5':
                    textureId = TEXTURES[Block.VALUES.length + 5];
                    break;

                case '6':
                    textureId = TEXTURES[Block.VALUES.length + 6];
                    break;

                case '7':
                    textureId = TEXTURES[Block.VALUES.length + 7];
                    break;

                case '8':
                    textureId = TEXTURES[Block.VALUES.length + 8];
                    break;

                case '9':
                    textureId = TEXTURES[Block.VALUES.length + 9];
                    break;

                case ':':
                    textureId = TEXTURES[Block.VALUES.length + 10];
                    break;

                case '.':
                    textureId = TEXTURES[Block.VALUES.length + 11];
                    break;

                default:
                    throw new RuntimeException("Character not found '" + desc.charAt(i) + "'");
            }

            //if we are within the array we can reuse
            if (i < characters.size())
            {
                characters.get(i).enabled = true;
                characters.get(i).textureId = textureId;
            }
            else
            {
                //else we add a new object to the array
                characters.add(new Character(textureId));
            }
        }
        */
    }

    @Override
    public void render(GL10 gl)
    {
        //check every digit in the list
        for (int i = 0; i < characters.size(); i++)
        {
            //get the current digit object
            final Character character = characters.get(i);

            //if this is not enabled no need to continue
            if (!character.enabled)
                return;

            //assign x-coordinate location
            setX(anchorX + (i * super.getWidth()));

            //assign texture
            super.setTextureId(character.textureId);

            //render animation
            super.render(gl);
        }
    }

    /**
     * This class will keep track of each character in our number
     */
    private class Character
    {
        //texture to render
        protected int textureId;

        //are we rendering this?
        protected boolean enabled = true;

        private Character(final int textureId)
        {
            this.textureId = textureId;
            this.enabled = true;
        }
    }
}