package com.zbh.example.game.scene;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.color.Color;
import org.andengine.ui.activity.BaseGameActivity;
import com.zbh.example.game.manager.ResourcesManager;
import com.zbh.example.game.util.Const;

public class SplashScene extends Scene{
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	ResourcesManager rm;
	// ===========================================================
	// Constructors
	// ===========================================================
	
	public SplashScene(){
		rm = ResourcesManager.getInstance();
		
		this.setBackground(new Background(Color.BLACK));
		
		float x = (Const.CAMERA_WIDTH - rm.logoTR.getWidth())/2;
		float y = (Const.CAMERA_HEIGHT - rm.logoTR.getHeight())/2;
		Sprite sprite = new Sprite(x, y, rm.logoTR, rm.vbom);
		this.attachChild(sprite);
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

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
