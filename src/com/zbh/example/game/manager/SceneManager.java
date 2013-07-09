package com.zbh.example.game.manager;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import com.zbh.example.game.scene.EndScene;
import com.zbh.example.game.scene.GameScene;
import com.zbh.example.game.scene.MenuScene;
import com.zbh.example.game.scene.SplashScene;

public class SceneManager {
	// ===========================================================
	// Constants
	// ===========================================================
	
	public static final SceneManager INSTANCE = new SceneManager();
	
	public enum SceneType
	{
		SCENE_SPLASH,
		SCENE_MENU,
		SCENE_GAME,
		SCENE_END,
	}
	
	// ===========================================================
	// Fields
	// ===========================================================

    private SceneType currentSceneType;

	private BaseGameActivity activity;
	private Engine engine = ResourcesManager.getInstance().engine;
    private Camera camera;
    private Scene currentScene;
    
	// ===========================================================
	// Constructors
	// ===========================================================
//    public static void setSceneManager(BaseGameActivity act, Engine eng, Camera cam){
//    	getInstance().activity = act;
//    	getInstance().engine = eng;
//    	getInstance().camera = cam;
//    }
	// ===========================================================
	// Getter & Setter
	// ===========================================================
    
    public static SceneManager getInstance(){
    	return INSTANCE;
    }
    
    public SceneType getCurrentSceneType(){
		return currentSceneType;
    }
    
    public Scene getCurrentScene(){
    	return this.currentScene;
    }
    
    public void setCurrentScene(SceneType currentSceneType) {
		this.currentSceneType = currentSceneType;
		switch(currentSceneType){
		case SCENE_MENU:	
			ResourcesManager.getInstance().loadMenuResources();
			currentScene = new MenuScene();
			engine.setScene(currentScene);
			break;
		case SCENE_GAME:
			ResourcesManager.getInstance().loadGameResources();
			currentScene = new GameScene();
			engine.setScene(currentScene);
			break;
		case SCENE_SPLASH:
			ResourcesManager.getInstance().loadSplashResources();
			currentScene = new SplashScene();
			engine.setScene(currentScene);
			break;
		case SCENE_END:
			ResourcesManager.getInstance().loadEndResources();
			currentScene = new EndScene();
			engine.setScene(currentScene);
		}
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
