package com.zbh.example.game.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.util.GLState;

import com.zbh.example.game.manager.ResourcesManager;
import com.zbh.example.game.manager.SceneManager;
import com.zbh.example.game.manager.SceneManager.SceneType;
import com.zbh.example.game.util.Const;

public class EndScene extends Scene implements IOnMenuItemClickListener{
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int MENU_BACK = 0;
	private static final int MENU_RETRY = 1;
	// ===========================================================
	// Fields
	// ===========================================================
	private int scores;
	private Text scoreText;
	private ResourcesManager rm;
	private org.andengine.entity.scene.menu.MenuScene ms;
	// ===========================================================
	// Constructors
	// ===========================================================

	public EndScene(){
		rm = ResourcesManager.getInstance();
		rm.getMusicApplause().play();
		createBackground();
		createMenuItem();
		
	}

	private void createMenuItem() {
		ms = new org.andengine.entity.scene.menu.MenuScene(rm.camera);
		
		// scores
		scores = rm.getScores();
		float x = Const.CAMERA_WIDTH;
		float y = 0;
		scoreText = new Text(x/2, 40, rm.mPlokFont, "" + scores, 10, rm.vbom);
		scoreText.setScale(2);
//		scoreText.setSize(100, 60);
		Sprite bgSprite = new Sprite((x - rm.getEndTRPanel().getWidth())/2, y, rm.getEndTRPanel().getWidth(), Const.CAMERA_HEIGHT, rm.getEndTRPanel(), rm.vbom);
		
		ms.attachChild(bgSprite);
		ms.attachChild(scoreText);
		
		final IMenuItem backMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_BACK, 398, 90, rm.getEndTRBack(), rm.vbom), 1.2f, 1);
		final IMenuItem retryMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_RETRY, 398, 90, rm.getEndTRRetry(), rm.vbom), 1.2f, 1);
		
		ms.addMenuItem(backMenuItem);
		ms.addMenuItem(retryMenuItem);
		
		ms.buildAnimations();
		ms.setBackgroundEnabled(false);
		
		float pX = (Const.CAMERA_WIDTH - backMenuItem.getWidth())/2;
		float pY = (Const.CAMERA_HEIGHT - backMenuItem.getHeight())/2;
		backMenuItem.setPosition(pX, pY - 50);
		retryMenuItem.setPosition(pX, pY + 50);
		
		ms.setOnMenuItemClickListener(this);
		
		this.setChildScene(ms);
	}

	private void createBackground() {

		Background bg = new Background(1, 1, 1);
		this.setBackground(bg);
		

		
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch(pMenuItem.getID()){
		case MENU_BACK:
			SceneManager.getInstance().setCurrentScene(SceneType.SCENE_MENU);
			return true;
		case MENU_RETRY:
			rm.musicBackground.resume();
			SceneManager.getInstance().setCurrentScene(SceneType.SCENE_GAME);
			return true;
		default :
			return false;
		}
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
