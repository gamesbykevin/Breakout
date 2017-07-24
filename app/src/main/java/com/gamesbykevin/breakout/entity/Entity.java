package com.gamesbykevin.breakout.entity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public abstract class Entity extends com.gamesbykevin.androidframework.base.Entity 
{
	//is the power up hidden
	private boolean hidden = false;

	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;

	private float[] vertices = {
			0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
			1.0f, 0.0f, 0.0f,
			1.0f, 1.0f, 0.0f
	};

	private float[] textures = {
			0.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 0.0f,
			1.0f, 1.0f
	};

	private int textureId;

	/**
	 * Default constructor
	 */
	public Entity(final int width, final int height)
	{
		super();
		
		//assign dimensions
		super.setWidth(width);
		
		//assign dimensions
		super.setHeight(height);

		//create our vertices buffer
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		//create our texture buffer
		ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		textureBuffer = tbb.asFloatBuffer();
		textureBuffer.put(textures);
		textureBuffer.position(0);
	}
	
	/**
	 * Set the entity up as hidden
	 * @param hidden true if you want to hide, false otherwise
	 */
	public final void setHidden(final boolean hidden)
	{
		this.hidden = hidden;
	}
	
	/**
	 * Is the entity hidden
	 * @return true if we don't want to interact/display with this entity
	 */
	public final boolean isHidden()
	{
		return this.hidden;
	}

	public void setTextureId(final int textureId) {
		this.textureId = textureId;
	}

	public int getTextureId() {
		return this.textureId;
	}

	/**
	 * Is there collision?
	 * @param entity The entity we want to check collision with
	 * @return true if the entities intersect, false otherwise
	 */
	public boolean hasCollision(final Entity entity)
	{
		//if this entity is not located in range of the other entity we can't have collision 
		if (getX() + getWidth() < entity.getX())
			return false;
		if (getX() > entity.getX() + entity.getWidth())
			return false;
		if (getY() + getHeight() < entity.getY())
			return false;
		if (getY() > entity.getY() + entity.getHeight())
			return false;
		
		//we have collision
		return true;
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
	}
	
	public void render(GL10 gl, double x, double y, double w, double h, int textureId) {
		setX(x);
		setY(y);
		setWidth(w);
		setHeight(h);
		setTextureId(textureId);
		render(gl);
	}

	public void render(GL10 gl) {

		//don't render if hidden
		if (isHidden())
			return;

		//get the coordinates
		float x = (float)getX();
		float y = (float)getY();
		float w = (float)getWidth();
		float h = (float)getHeight();

		//use for quick transformations so it will only apply to this object
		gl.glPushMatrix();

		//assign render coordinates
		gl.glTranslatef(x, y, 0.0f);

		//assign dimensions
		gl.glScalef(w, h, 0.0f);

		//enable texture rendering
		gl.glEnable(GL10.GL_TEXTURE_2D);

		//assign texture we want to use
		gl.glBindTexture(GL10.GL_TEXTURE_2D, getTextureId());

		//enable client state for our render
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		//provide our array of vertex coordinates
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		//coordinates on texture we want to render
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		//render our texture based on the texture and vertex coordinates
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);

		//after rendering remove the transformation since we only needed it for this object
		gl.glPopMatrix();

		//now that we are done, undo client state to prevent any render issues
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_2D);
	}
}