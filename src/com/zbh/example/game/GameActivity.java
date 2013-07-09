package com.zbh.example.game;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.engine.Engine.EngineLock;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
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
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.zbh.example.game.manager.ResourcesManager;
import com.zbh.example.game.manager.SceneManager;
import com.zbh.example.game.manager.SceneManager.SceneType;

public class GameActivity extends BaseGameActivity implements IAccelerationListener {

	// ===========================================================
	// Constants
	// ===========================================================
	protected static final int CAMERA_WIDTH = 800;
	protected static final int CAMERA_HEIGHT = 480;
	private static final int ANIMATE_TIME_SHORT = 200;
	private static final int LOOP_TIMES = 100;
	private static final int FONT_SIZE = 20;
	private static final long[] PLAYER_ANIM_DURATION = {150,150,150,150};
	private static final long[] EFFECT_ANIM_DURATION = {150,150,150,150,150};
	private static final int[] PLAYER_ANIM_FRAME = {0,1,2,0}; // anim occur with 0
	private static final int[] PLAYER_SLIP_FRAME = {2,0};
	private static final int[] EFFECT_FRAMES = {10,11,12,13,14};
	final FixtureDef PLAYER_FIX = PhysicsFactory.createFixtureDef(5.0f, 1.0f, 1.0f);
	final FixtureDef OBSTACLE_FIX = PhysicsFactory.createFixtureDef(0f, 0.5f, 0.5f);
	
	// ===========================================================
	// Fields
	// ===========================================================

	private Scene scene;
	
	private BuildableBitmapTextureAtlas playerTexture;
	private BuildableBitmapTextureAtlas enemyAndObstacleTA;
	private TiledTextureRegion playerTexureRegion;
	private TiledTextureRegion enemyTR;
	private TiledTextureRegion effectTR;
	private TextureRegion obstacleTR;
	private TextureRegion obstacleTR2;
	private TextureRegion obstacleTR3;
	private TextureRegion obstacleTR4;
	private TextureRegion mBackgroundLeftTextureRegion;
	private TextureRegion mBackgroundRightTextureRegion;
	
	private PhysicsWorld mPhysicsWorld;	
	private BoundCamera mCamera;
	private Entity layerBackground;
	private Entity layerMiddle;
	private Entity layerTop;
	private ArrayList<EnemySprite> enemySprite;
	private AnimatedSprite sPlayer;
	private Sprite infiniteBackgroundSprite;
	private HUD hud;
	private int score;
	private Random random;
	private Font mPlokFont;
	private Text scoreText;
	private Rectangle ground;
	private Rectangle ceil;
	private Sprite obstacleSprite;


	// ===========================================================
	// Constructors
	// ===========================================================

//	private void initFields() {
//		enemySprite = new ArrayList<EnemySprite>();
//		random = new Random(12412);
//		hud = new HUD();
//		score = 0;
//		scoreText = null; // init in loadFont
//	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) 
	{
	    return new LimitedFPSEngine(pEngineOptions, 60);
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {

		mCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, 0 ,CAMERA_WIDTH * LOOP_TIMES, 0, CAMERA_HEIGHT);

		EngineOptions options = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
		
		options.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		
		return options;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
//		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
//		initFields();
//		loadBG(); // background load
//		loadGfx(); // hero
//		loadEnemyAndObsacle(); // enemy giant TR 
//		loadFontTA(); // and add to HUD
		mPhysicsWorld = new PhysicsWorld(new Vector2(0,
					SensorManager.GRAVITY_EARTH), false);
		ResourcesManager.setResourcesManager(this, getEngine(), mCamera, getVertexBufferObjectManager(), mPhysicsWorld);
		
		// resource
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}


	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
//		this.scene = new Scene();
//		this.scene.setBackground(new Background(0, 0, 0));
//		mPhysicsWorld = new PhysicsWorld(new Vector2(0,
//				SensorManager.GRAVITY_EARTH), false);
//		this.scene.registerUpdateHandler(mPhysicsWorld);
//		layerBackground = new Entity();
//		layerMiddle = new Entity();
//		layerTop = new Entity();
//		scene.attachChild(layerBackground);
//		scene.attachChild(layerMiddle);
//		scene.attachChild(layerTop);
//		createWalls(layerMiddle);
//		createObstaclesToScene();
		SceneManager.getInstance().setCurrentScene(SceneType.SCENE_SPLASH);
		this.scene = SceneManager.getInstance().getCurrentScene();
		pOnCreateSceneCallback.onCreateSceneFinished(this.scene);
	}

	/*@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		// Three layer construction ! Epic!

		sPlayer = new AnimatedSprite(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 2,
				playerTexureRegion, this.getVertexBufferObjectManager());		
		Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, sPlayer,
				BodyType.DynamicBody, PLAYER_FIX);
		layerTop.attachChild(sPlayer);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(sPlayer,
				body, true, false));

		// set camera chase sprite
		this.mCamera.setChaseEntity(sPlayer);



		// Create left background sprite
		Sprite mBackgroundLeftSprite = new Sprite(0, 0,
				mBackgroundLeftTextureRegion,
				mEngine.getVertexBufferObjectManager());

		// Attach left background sprite to the background scene
		layerBackground.attachChild(mBackgroundLeftSprite);
		// ParallaxBackground background = new ParallaxBackground(0, 0, 0);
		// background.attachParallaxEntity(new ParallaxEntity(0,
		// mBackgroundLeftSprite));
		// scene.setBackground(background);

		// Create the right background sprite, positioned directly to the right
		// of the first segment
		Sprite mBackgroundRightSprite = new Sprite(mBackgroundLeftSprite.getX()
				+ mBackgroundLeftTextureRegion.getWidth(), 0,
				mBackgroundRightTextureRegion,
				mEngine.getVertexBufferObjectManager());
		// Attach right background sprite to the background scene
		layerBackground.attachChild(mBackgroundRightSprite);
		// background.attachParallaxEntity(new ParallaxEntity(0,
		// mBackgroundRightSprite));
		// scene.setBackground(background);

		// =====================================
		// implement infinite game background!
		// =====================================
		// Random random = new Random(1231312); // 随机生成地图,and enemy // replaced
		// in initFields()
		// enemySprite first appear here
		// on current scene current background
		int times = random.nextInt(8);
		for (int j = 0; j < times; j++) {
			EnemySprite tempAS = new EnemySprite(random.nextInt(800),
					random.nextInt(480), enemyTR,
					mEngine.getVertexBufferObjectManager());
			enemySprite.add(tempAS);
			layerTop.attachChild(tempAS);
		}
		infiniteBackgroundSprite = new Sprite(mBackgroundRightSprite.getX(), 0,
				mBackgroundRightTextureRegion,
				mEngine.getVertexBufferObjectManager()); // 初始化
		for (int j = 0; j < random.nextInt(8); j++) {
			EnemySprite tempAS = new EnemySprite(800 + random.nextInt(800),
					random.nextInt(480), enemyTR,
					mEngine.getVertexBufferObjectManager());
			enemySprite.add(tempAS);
			layerTop.attachChild(tempAS);
		}
		// generate infinite loop background and infinite enemySprite

		for (int i = 0; i < GameActivity.LOOP_TIMES ; i++) {
			switch (random.nextInt(2)) {
			case 0:
				// generate and attach enemy
				for (int j = 0; j < random.nextInt(5); j++) {
					EnemySprite tempAS = new EnemySprite(
							infiniteBackgroundSprite.getX()
									+ random.nextInt(800), random.nextInt(480),
							enemyTR, mEngine.getVertexBufferObjectManager());
					enemySprite.add(tempAS);
					layerTop.attachChild(tempAS);
				}
				// generate left textureRegion
				infiniteBackgroundSprite = new Sprite(
						infiniteBackgroundSprite.getX()
								+ infiniteBackgroundSprite.getWidth(), 0,
						mBackgroundLeftTextureRegion,
						mEngine.getVertexBufferObjectManager());

				layerBackground.attachChild(infiniteBackgroundSprite);

				break;
			case 1:
				// generate and attach enemy
				for (int j = 0; j < random.nextInt(5); j++) {
					EnemySprite tempAS = new EnemySprite(
							infiniteBackgroundSprite.getX()
									+ random.nextInt(800), random.nextInt(480),
							enemyTR, mEngine.getVertexBufferObjectManager());
					enemySprite.add(tempAS);
					layerTop.attachChild(tempAS);
				}
				// generate right TextureRegion
				infiniteBackgroundSprite = new Sprite(
						infiniteBackgroundSprite.getX()
								+ infiniteBackgroundSprite.getWidth(), 0,
						mBackgroundRightTextureRegion,
						mEngine.getVertexBufferObjectManager());
				layerBackground.attachChild(infiniteBackgroundSprite);

				break;
			default:
				break;
			}
		}
		
		scene.registerUpdateHandler(this);

		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}*/
	
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		mEngine.registerUpdateHandler(new TimerHandler(2f,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						// TODO Auto-generated method stub
						SceneManager.getInstance().setCurrentScene(SceneType.SCENE_MENU);
					}
				}));
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	private void createObstaclesToScene() {		
		for (int i = 0; i < LOOP_TIMES*CAMERA_WIDTH; i += random.nextInt(CAMERA_WIDTH)){
			
			switch(random.nextInt(4)){
			case 0:
				obstacleSprite = new Sprite(i, CAMERA_HEIGHT-100, obstacleTR, this.getVertexBufferObjectManager());
				break;
			case 1:
				obstacleSprite = new Sprite(i, CAMERA_HEIGHT-334, obstacleTR2, this.getVertexBufferObjectManager());
				break;
			case 2:
				obstacleSprite = new Sprite(i, CAMERA_HEIGHT-191, obstacleTR3, this.getVertexBufferObjectManager());
				break;
			case 3:
				obstacleSprite = new Sprite(i, CAMERA_HEIGHT-62, obstacleTR4, this.getVertexBufferObjectManager());
				break;
			default:
//				EnemySprite enemySpriteNew = new EnemySprite(i, random.nextInt(CAMERA_WIDTH), enemyTR, this.getVertexBufferObjectManager());
//				enemySprite.add(enemySpriteNew);
//				layerTop.attachChild(enemySpriteNew);
			}
			Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, obstacleSprite,
					BodyType.StaticBody, OBSTACLE_FIX);			
			mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(obstacleSprite,
					body, true, false));
			layerMiddle.attachChild(obstacleSprite);
		}
	}

	private void loadEnemyAndObsacle() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		// must at ^2 of 2!!!!!!!!!!!!!!!!
		enemyAndObstacleTA = new BuildableBitmapTextureAtlas(
				getTextureManager(), 2048, 512, TextureOptions.NEAREST);
		enemyTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				enemyAndObstacleTA, this, "enemy.png", 2, 1);
		obstacleTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				enemyAndObstacleTA, this, "obstacle.png"); 
		obstacleTR2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				enemyAndObstacleTA, this, "obstacle2.png"); 
		obstacleTR3 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				enemyAndObstacleTA, this, "obstacle3.png");
		obstacleTR4 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				enemyAndObstacleTA, this, "obstacle4.png");	
		try {
			enemyAndObstacleTA
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 1));
			enemyAndObstacleTA.load();
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}
		
	}

	private void loadBG() {
		/* Create the background left texture atlas */
		// BuildableBitmapTextureAtlas backgroundTextureLeft = new
		// BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 1600, 480);
		BuildableBitmapTextureAtlas backgroundTextureLeft = new BuildableBitmapTextureAtlas(
				mEngine.getTextureManager(), 800, 480);
		BuildableBitmapTextureAtlas backgroundTextureRight = new BuildableBitmapTextureAtlas(
				mEngine.getTextureManager(), 800, 480);
		/* Create the background left texture region */
		mBackgroundLeftTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(backgroundTextureLeft, getAssets(),
						"background_1.png");
		// mBackgroundRightTextureRegion =
		// BitmapTextureAtlasTextureRegionFactory.createFromAsset(backgroundTextureLeft,
		// getAssets(), "background_2.png");
		mBackgroundRightTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(backgroundTextureRight, getAssets(),
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

	}

	private void loadGfx() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");		
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!at a ^2 of 2!!!!!!!!!!!! fuck!
		playerTexture = new BuildableBitmapTextureAtlas(getTextureManager(), 512, 512, TextureOptions.NEAREST);
		effectTR = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(playerTexture, this, "effect.png", 5, 5);
		playerTexureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(playerTexture, this, "heroin_tiled3.png", 4, 1); 
		try {
			playerTexture.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
			playerTexture.load();
		} catch (TextureAtlasBuilderException e) {			
			e.printStackTrace();
		}
		
	}

	private void createWalls(Entity layerMiddle) {

		FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
		Rectangle startLeftWall = new Rectangle(0, 0, 15, 480,
				this.mEngine.getVertexBufferObjectManager());
		ground = new Rectangle(0, CAMERA_HEIGHT - 15, CAMERA_WIDTH
				* LOOP_TIMES, 15, this.mEngine.getVertexBufferObjectManager());
		ceil = new Rectangle(0, 0, CAMERA_WIDTH * LOOP_TIMES, 15,
				this.mEngine.getVertexBufferObjectManager());
		// ground.setColor(new Color(15, 50, 0));
		// ceil.setColor(new Color(15, 50, 0));
		// startLeftWall.setColor(new Color(15, 50, 0));
		PhysicsFactory.createBoxBody(mPhysicsWorld, startLeftWall,
				BodyType.StaticBody, WALL_FIX);
		PhysicsFactory.createBoxBody(mPhysicsWorld, ground,
				BodyType.StaticBody, WALL_FIX);
		PhysicsFactory.createBoxBody(mPhysicsWorld, ceil, BodyType.StaticBody,
				WALL_FIX);
		layerMiddle.attachChild(ground);
		layerMiddle.attachChild(ceil);
		layerMiddle.attachChild(startLeftWall);
	}

	// 当设备改变了重力方向的时候。
	@Override
	public void onAccelerationChanged(final AccelerationData pAccelerationData) {
		final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX(),
				pAccelerationData.getY());
		this.mPhysicsWorld.setGravity(gravity);
		Vector2Pool.recycle(gravity);
	}

	@Override
	public void onResumeGame() {
		super.onResumeGame();

		this.enableAccelerationSensor(this); // gravity switch on
	}

	@Override
	public void onPauseGame() {
		super.onPauseGame();

		this.disableAccelerationSensor(); // gravity switch off
	}

	/*@Override
	public void onUpdate(float pSecondsElapsed) {
		// wall collision detection
//		if(ceil.collidesWith(sPlayer) || ground.collidesWith(sPlayer)){
////			sPlayer.animate(PLAYER_ANIM_DURATION, 1, 3, false);
//			// TODO sometimes can't catch collision!!!!!!!!
//			Debug.d("crush to wall!");
//			//sPlayer.animate(PLAYER_ANIM_DURATION, PLAYER_SLIP_FRAME, false);
//		}
		
		// onCollision Detection
		for (int i = 0; i < enemySprite.size(); i++) {
			// final EnemySprite enemyAS = enemySprite.get(i);
			// this method is deprecated , code as AndEngine.example is
			// recommended
			// if(BaseCollisionChecker
			// .checkAxisAlignedRectangleCollision(enemyAS.getX(),
			// enemyAS.getY(), enemyAS.getX()+enemyAS.getWidth(),
			// enemyAS.getY()+enemyAS.getHeight()
			// , sPlayer.getX(), sPlayer.getY(), sPlayer.getX() +
			// sPlayer.getWidth(), sPlayer.getY() + sPlayer.getHeight())){
			if (enemySprite.get(i).collidesWith(sPlayer)
					&& enemySprite.get(i).isKilled() == false) {
				final int ii = i;
				
				sPlayer.animate(PLAYER_ANIM_DURATION, PLAYER_ANIM_FRAME, false);
				
				//AnimatedSprite effectAS = new AnimatedSprite(sPlayer.getX(), sPlayer.getY(), effectTR, this.getVertexBufferObjectManager());
				//layerTop.attachChild(effectAS);
				//effectAS.animate(EFFECT_ANIM_DURATION, EFFECT_FRAMES, this);
				
				score += 100;
				scoreText.setText("Score:" + score);
				
				enemySprite.get(i).setKilled(true); // only first run on here
				enemySprite.get(i).animate(ANIMATE_TIME_SHORT, 0,
						new IAnimationListener() {

							@Override
							public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,int pOldFrameIndex, int pNewFrameIndex) {}

							@Override
							public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,int pRemainingLoopCount,int pInitialLoopCount) {}

							@Override
							public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {

								runOnUpdateThread(new Runnable() {
									public void run() {
										final EngineLock engineLock = getEngine()
												.getEngineLock();
										engineLock.lock(); // this is irrelative to efficiency

										/* Now it is save to remove the entity! 
										layerTop.detachChild(enemySprite.get(ii));
										enemySprite.get(ii).dispose();
										enemySprite.remove(ii);

										engineLock.unlock();
									}
								});
							}

							@Override
							public void onAnimationStarted(AnimatedSprite pAnimatedSprite,int pInitialLoopCount) {}
						});
			}
		}
	}

	@Override
	public void reset() {
		// collision detection's not collide

	}*/

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	@Override
	public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {
		// TODO Auto-generated method stub
		
	}
}
