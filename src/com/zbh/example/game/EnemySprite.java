package com.zbh.example.game;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class EnemySprite extends AnimatedSprite{
	// ===========================================================
	// Fields
	// ===========================================================

	private boolean killed;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public EnemySprite(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager){
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		
		this.killed = false;
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public boolean isKilled() {
		return killed;
	}

	public void setKilled(boolean killed) {
		this.killed = killed;
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
