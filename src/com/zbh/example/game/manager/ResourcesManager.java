package com.zbh.example.game.manager;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;


public class ResourcesManager {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final ResourcesManager INSTANCE = new ResourcesManager();
	private static final float FONT_SIZE = 20;
	// ===========================================================
	// Fields
	// ===========================================================
	public Engine engine;
	public BaseGameActivity activity;
	public Camera camera;	
	public VertexBufferObjectManager vbom;
	public PhysicsWorld physicsWorld;
	
	public Font mPlokFont;
	public Text scoreText;
	
	public BuildableBitmapTextureAtlas playerTexture;
	public BuildableBitmapTextureAtlas enemyAndObstacleTA;
	public BitmapTextureAtlas logoTA;
	public BitmapTextureAtlas menuTA;
	private BitmapTextureAtlas endTA;
	
	public TiledTextureRegion playerTexureRegion;
	public TiledTextureRegion enemyTR;
	private TiledTextureRegion effectTR;

	public TextureRegion obstacleTR;
	public TextureRegion obstacleTR2;
	public TextureRegion obstacleTR3;
	public TextureRegion obstacleTR4;
	public TextureRegion mBackgroundLeftTextureRegion;
	public TextureRegion mBackgroundRightTextureRegion;
	public TextureRegion logoTR;
	public TextureRegion menuTR1;
	public TextureRegion menuTR2;
	private TextureRegion endTRPanel;
	private TextureRegion endTRBack;
	private TextureRegion endTRRetry;
	private TextureRegion momoiloveyouTR;
	private TextureRegion particleSystemTR; 
	
	public Music musicBackground;
	public Music musicBeat;
	private Music musicApplause;
	private int scores;

	// ===========================================================
	// Constructors
	// ===========================================================
	public static void setResourcesManager(BaseGameActivity bga, Engine engine, Camera camera, VertexBufferObjectManager vbom){
		getInstance().activity = bga;
		getInstance().engine = engine;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
	}
	
	public static void setResourcesManager(BaseGameActivity bga,
			Engine engine, BoundCamera camera,
			VertexBufferObjectManager vbom,
			PhysicsWorld mPhysicsWorld) {
		// TODO Auto-generated method stub
		getInstance().activity = bga;
		getInstance().engine = engine;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
		getInstance().physicsWorld = mPhysicsWorld;
	}
	
	public void loadMenuResources()
	{
		loadMenuTA();
		loadMenuAudio();
		loadMenuFonts();
	}
	
	public void loadGameResources()
	{
		loadGameTA();
		loadGameFonts();
		loadGameAudio();
	}
	
	public void loadSplashResources()
	{
		loadSplashTA();
	}
	
	public void loadEndResources() {
		loadEndAudio();
		//loadEndFonts();
		loadGameFonts();
		loadEndTA();		
	}
	
	private void loadEndTA() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		endTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 1024, 1024);
		endTRPanel = BitmapTextureAtlasTextureRegionFactory.createFromAsset(endTA, this.activity, "ranking-panel.png", 0 ,0);
		endTRBack = BitmapTextureAtlasTextureRegionFactory.createFromAsset(endTA, this.activity, "ranking-back.png", 0 ,700);
		endTRRetry = BitmapTextureAtlasTextureRegionFactory.createFromAsset(endTA, this.activity, "ranking-retry.png", 0 ,800);
		endTA.load();
	}

	private void loadEndAudio() {
		try {
			musicApplause = MusicFactory.createMusicFromAsset(engine.getMusicManager(), this.activity,"music/applause.ogg");
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Music getMusicApplause() {
		return musicApplause;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public TextureRegion getLogoTR() {
		return logoTR;
	}
	
	public static ResourcesManager getInstance(){
		return INSTANCE;
	}

	public int getScores() {
		return scores;
	}

	public void setScores(int scores) {
		this.scores = scores;
	}
	
	public Music getMusicBackground(){
		return this.musicBackground;
	}
	
	public TiledTextureRegion getEffectTR() {
		return effectTR;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	private void loadMenuTA(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		menuTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 512, 512);
		menuTR1 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, this.activity, "menu_button_background1.png", 0 ,0);
		menuTR2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, this.activity, "menu_button_background2.png", 0 ,76);
		momoiloveyouTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, this.activity, "momoiloveyou.png", 0 ,200);
		particleSystemTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, this.activity, "particle_point.png", 0 ,460);
		menuTA.load();
	}


	private void loadMenuAudio(){
		try {
			musicBackground = MusicFactory.createMusicFromAsset(engine.getMusicManager(), this.activity,"music/swordland.ogg");
			musicBeat = MusicFactory.createMusicFromAsset(engine.getMusicManager(), this.activity,"music/beat.ogg");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void loadMenuFonts(){}
	
	private void loadGameTA(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		enemyAndObstacleTA = new BuildableBitmapTextureAtlas(
				this.activity.getTextureManager(), 2048, 512, TextureOptions.NEAREST);
		enemyTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				enemyAndObstacleTA, this.activity, "enemy.png", 2, 1);
		obstacleTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				enemyAndObstacleTA, this.activity, "obstacle.png"); 
		obstacleTR2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				enemyAndObstacleTA, this.activity, "obstacle2.png"); 
		obstacleTR3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				enemyAndObstacleTA, this.activity, "obstacle3.png");
		obstacleTR4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				enemyAndObstacleTA, this.activity, "obstacle4.png");	
		try {
			enemyAndObstacleTA
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 1));
			enemyAndObstacleTA.load();
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}
		BuildableBitmapTextureAtlas backgroundTextureLeft = new BuildableBitmapTextureAtlas(
				this.engine.getTextureManager(), 800, 480);
		BuildableBitmapTextureAtlas backgroundTextureRight = new BuildableBitmapTextureAtlas(
				this.engine.getTextureManager(), 800, 480);
		/* Create the background left texture region */
		mBackgroundLeftTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(backgroundTextureLeft, this.activity,
						"background_1.png");
		// mBackgroundRightTextureRegion =
		// BitmapTextureAtlasTextureRegionFactory.createFromAsset(backgroundTextureLeft,
		// getAssets(), "background_2.png");
		mBackgroundRightTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(backgroundTextureRight, this.activity,
						"background_2.png");
		/* Build and load the background left and right texture atlas */
		try {
			backgroundTextureLeft
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			backgroundTextureLeft.load();
			backgroundTextureRight
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			backgroundTextureRight.load();
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}
		
		playerTexture = new BuildableBitmapTextureAtlas(this.activity.getTextureManager(), 512, 512, TextureOptions.NEAREST);
		effectTR = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(playerTexture, this.activity, "effect.png", 5, 5);
		playerTexureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(playerTexture, this.activity, "heroin_tiled3.png", 4, 1); 
		try {
			playerTexture.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
			playerTexture.load();
		} catch (TextureAtlasBuilderException e) {			
			e.printStackTrace();
		}

	}
	private void loadGameFonts(){
		FontFactory.setAssetBasePath("font/");
		final ITexture plokFontTexture = new BitmapTextureAtlas(this.activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		this.mPlokFont = FontFactory.createFromAsset(this.activity.getFontManager(), plokFontTexture, this.activity.getAssets(), "Plok.ttf", FONT_SIZE, true, android.graphics.Color.BLUE);
		this.mPlokFont.load();
	}
	private void loadGameAudio(){}
	

	private void loadSplashTA() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		logoTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 256, 128);
		logoTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(logoTA, this.activity, "logo.png", 0 ,0);
		logoTA.load();
	}

	public TextureRegion getEndTRPanel() {
		return endTRPanel;
	}

	public TextureRegion getEndTRBack() {
		return endTRBack;
	}


	public TextureRegion getEndTRRetry() {
		return endTRRetry;
	}

	public TextureRegion getMomoiloveyouTR() {
		return momoiloveyouTR;
	}
	public TextureRegion getParticleSystemTR() {
		return particleSystemTR;
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
