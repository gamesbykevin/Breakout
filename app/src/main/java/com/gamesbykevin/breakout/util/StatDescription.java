package com.gamesbykevin.breakout.util;

import com.gamesbykevin.breakout.entity.Entity;
import com.gamesbykevin.breakout.game.GameHelper;
import com.gamesbykevin.breakout.opengl.Textures;
import com.gamesbykevin.breakout.wall.Wall;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import static com.gamesbykevin.breakout.game.GameHelper.STAT_X;
import static com.gamesbykevin.breakout.game.GameHelper.STAT_Y;

/**
 * Created by Kevin on 7/15/2017.
 */
public class StatDescription extends Entity {

    //the dimensions of each number character animation
    public static final int ANIMATION_WIDTH = 65;
    public static final int ANIMATION_HEIGHT = 100;

    public final static int STAT_WIDTH = 32;
    public final static int STAT_HEIGHT = 50;

    //our array object for each digit in our score
    private ArrayList<Character> characters;

    public static final int DEFAULT_LIVES = 5;

    //track the value
    private long statValue = DEFAULT_LIVES;

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

        //set default value
        setDescription(DEFAULT_LIVES);
    }

    public void setDefaultDimensions() {

        //set the dimensions
        super.setWidth(STAT_WIDTH);
        super.setHeight(STAT_HEIGHT);
    }

    public long getStatValue() {
        return this.statValue;
    }

    public void setDescription(long newStatValue) {

        //assign our value
        this.statValue = newStatValue;

        //assign animations
        setDescription(String.valueOf(this.statValue));
    }

    private void setDescription(String desc)
    {
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
                    textureId = Textures.IDS[Textures.IDS.length - 10];
                    break;

                case '1':
                    textureId = Textures.IDS[Textures.IDS.length - 9];
                    break;

                case '2':
                    textureId = Textures.IDS[Textures.IDS.length - 8];
                    break;

                case '3':
                    textureId = Textures.IDS[Textures.IDS.length - 7];
                    break;

                case '4':
                    textureId = Textures.IDS[Textures.IDS.length - 6];
                    break;

                case '5':
                    textureId = Textures.IDS[Textures.IDS.length - 5];
                    break;

                case '6':
                    textureId = Textures.IDS[Textures.IDS.length - 4];
                    break;

                case '7':
                    textureId = Textures.IDS[Textures.IDS.length - 3];
                    break;

                case '8':
                    textureId = Textures.IDS[Textures.IDS.length - 2];
                    break;

                case '9':
                    textureId = Textures.IDS[Textures.IDS.length - 1];
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
    }

    @Override
    public void render(GL10 gl)
    {
        //store coordinate
        final double x = getX();

        //check every digit in the list
        for (int i = 0; i < characters.size(); i++)
        {
            try {

                //get the current digit object
                final Character character = characters.get(i);

                //if this is not enabled no need to continue
                if (!character.enabled)
                    return;

                //assign x-coordinate location
                setX(x + (int)(i * getWidth()));

                //assign texture
                super.setTextureId(character.textureId);

                //render animation
                super.render(gl);

            } catch (Exception e) {
                UtilityHelper.handleException(e);
            }
        }

        //restore coordinate
        setX(x);
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