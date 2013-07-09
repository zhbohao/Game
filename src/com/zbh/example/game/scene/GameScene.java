package com.zbh.example.game.scene;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.engine.Engine;
import org.andengine.engine.Engine.EngineLock;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
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
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.zbh.example.game.EnemySprite;
import com.zbh.example.game.manager.ResourcesManager;
import com.zbh.example.game.manager.SceneManager;
import com.zbh.example.game.manager.SceneManager.SceneType;
import com.zbh.example.game.util.Const;

public class GameScene extends Scene {
	
	// ===========================================================
	// Constants
	// ===========================================================
	
	private static final int Y_RANDOM_ENEMY = 420;
	private static final int ANIMATE_TIME_SHORT = 200;
	private static final int LOOP_TIMES = 300;
	private static final int FONT_SIZE = 20;
	private static final long[] PLAYER_ANIM_DURATION = {150,150,150,150};
	private static final long[] EFFECT_ANIM_DURATION = {150,150,150,150,150};
	private static final int[] PLAYER_ANIM_FRAME = {0,1,2,0}; // anim occur with 0
	private static final int[] PLAYER_SLIP_FRAME = {2,0};
	private static final int[] EFFECT_FRAMES = {10,11,12,13,14};
	final FixtureDef PLAYER_FIX = PhysicsFactory.createFixtureDef(10.0f, 0f, 0f);
	final FixtureDef OBSTACLE_FIX = PhysicsFactory.createFixtureDef(0f, 0f, 0.5f);

	// ===========================================================
	// Fields
	// ===========================================================
	
	private PhysicsWorld mPhysicsWorld;	
	private BoundCamera mCamera;
	private Entity layerBackground;
	private Entity layerMiddle;
	private Entity layerTop;
	private ArrayList<EnemySprite> enemySprite;
	private AnimatedSprite sPlayer;
	private Sprite infiniteBackgroundSprite;
	private AnimatedSprite effSprite;
	private HUD hud;
	private int score;
	private Random random;
	private Font mPlokFont;
	private Text scoreText;
	private Text timeText;
	private Rectangle ground;
	private Rectangle ceil;
	private Sprite obstacleSprite;
	protected Engine engine;
	private ResourcesManager rm;
	private boolean mGameRunning;
	private int mTime;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public GameScene(){
		rm = ResourcesManager.getInstance();
		engine = rm.engine;
		mCamera = (BoundCamera) rm.camera;
		
		initFields();
		loadFontTA();
		
		this.setBackground(new Background(0, 0, 0));
		mPhysicsWorld = ResourcesManager.getInstance().physicsWorld;
		this.registerUpdateHandler(mPhysicsWorld);
		layerBackground = new Entity();
		layerMiddle = new Entity();
		layerTop = new Entity();
		this.attachChild(layerBackground);
		this.attachChild(layerMiddle);
		this.attachChild(layerTop);
		createWalls(layerMiddle);
		createObstaclesToScene();
		
		// =============================
		// play musicBackground 
		// =============================
		rm.musicBackground.play();
		//==================================
		// create sprite
		//==================================
		
		sPlayer = new AnimatedSprite(Const.CAMERA_WIDTH / 2, Const.CAMERA_HEIGHT / 2,
				ResourcesManager.getInstance().playerTexureRegion, ResourcesManager.getInstance().vbom);		
		Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, sPlayer,
				BodyType.DynamicBody, PLAYER_FIX);
		layerTop.attachChild(sPlayer);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(sPlayer,
				body, true, false));

		//effect sprite
		effSprite = new AnimatedSprite(0, 0, rm.getEffectTR(), rm.vbom);
		effSprite.setVisible(false);
		layerTop.attachChild(effSprite);
		
		// set camera chase sprite
		this.mCamera.setChaseEntity(sPlayer);

		// Create left background sprite
		Sprite mBackgroundLeftSprite = new Sprite(0, 0,
				ResourcesManager.getInstance().mBackgroundLeftTextureRegion,
				ResourcesManager.getInstance().engine.getVertexBufferObjectManager());

		// Attach left background sprite to the background scene
		layerBackground.attachChild(mBackgroundLeftSprite);
		// ParallaxBackground background = new ParallaxBackground(0, 0, 0);
		// background.attachParallaxEntity(new ParallaxEntity(0,
		// mBackgroundLeftSprite));
		// scene.setBackground(background);

		// Create the right background sprite, positioned directly to the right
		// of the first segment
		Sprite mBackgroundRightSprite = new Sprite(mBackgroundLeftSprite.getX()
				+ ResourcesManager.getInstance().mBackgroundLeftTextureRegion.getWidth(), 0,
				ResourcesManager.getInstance().mBackgroundRightTextureRegion,
				ResourcesManager.getInstance().engine.getVertexBufferObjectManager());
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
					random.nextInt(Y_RANDOM_ENEMY), ResourcesManager.getInstance().enemyTR,
					ResourcesManager.getInstance().engine.getVertexBufferObjectManager());
			enemySprite.add(tempAS);
			layerTop.attachChild(tempAS);
		}
		infiniteBackgroundSprite = new Sprite(mBackgroundRightSprite.getX(), 0,
				ResourcesManager.getInstance().mBackgroundRightTextureRegion,
				ResourcesManager.getInstance().engine.getVertexBufferObjectManager()); // 初始化
		for (int j = 0; j < random.nextInt(8); j++) {
			EnemySprite tempAS = new EnemySprite(800 + random.nextInt(800),
					random.nextInt(Y_RANDOM_ENEMY), ResourcesManager.getInstance().enemyTR,
					ResourcesManager.getInstance().engine.getVertexBufferObjectManager());
			enemySprite.add(tempAS);
			layerTop.attachChild(tempAS);
		}
		// generate infinite loop background and infinite enemySprite

		for (int i = 0; i < LOOP_TIMES ; i++) {
			switch (random.nextInt(2)) {
			case 0:
				// generate and attach enemy
				for (int j = 0; j < random.nextInt(5); j++) {
					EnemySprite tempAS = new EnemySprite(
							infiniteBackgroundSprite.getX()
									+ random.nextInt(800), random.nextInt(Y_RANDOM_ENEMY),
									ResourcesManager.getInstance().enemyTR, ResourcesManager.getInstance().engine.getVertexBufferObjectManager());
					enemySprite.add(tempAS);
					layerTop.attachChild(tempAS);
				}
				// generate left textureRegion
				infiniteBackgroundSprite = new Sprite(
						infiniteBackgroundSprite.getX()
								+ infiniteBackgroundSprite.getWidth(), 0,
								ResourcesManager.getInstance().mBackgroundLeftTextureRegion,
						ResourcesManager.getInstance().engine.getVertexBufferObjectManager());

				layerBackground.attachChild(infiniteBackgroundSprite);

				break;
			case 1:
				// generate and attach enemy
				for (int j = 0; j < random.nextInt(5); j++) {
					EnemySprite tempAS = new EnemySprite(
							infiniteBackgroundSprite.getX()
									+ random.nextInt(800), random.nextInt(Y_RANDOM_ENEMY),
									ResourcesManager.getInstance().enemyTR, ResourcesManager.getInstance().engine.getVertexBufferObjectManager());
					enemySprite.add(tempAS);
					layerTop.attachChild(tempAS);
				}
				// generate right TextureRegion
				infiniteBackgroundSprite = new Sprite(
						infiniteBackgroundSprite.getX()
								+ infiniteBackgroundSprite.getWidth(), 0,
								ResourcesManager.getInstance().mBackgroundRightTextureRegion,
						ResourcesManager.getInstance().engine.getVertexBufferObjectManager());
				layerBackground.attachChild(infiniteBackgroundSprite);

				break;
			default:
				break;
			}
		}
		
		engine.registerUpdateHandler(new IUpdateHandler(){

			@Override
			public void onUpdate(float pSecondsElapsed) {
				// wall collision detection
//				if(ceil.collidesWith(sPlayer) || ground.collidesWith(sPlayer)){
////					sPlayer.animate(PLAYER_ANIM_DURATION, 1, 3, false);
//					// TODO sometimes can't catch collision!!!!!!!!
//					Debug.d("crush to wall!");
//					//sPlayer.animate(PLAYER_ANIM_DURATION, PLAYER_SLIP_FRAME, false);
//				}
				
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
						
						// play beat music
						rm.musicBeat.play();
						
						effSprite.setPosition(sPlayer.getX(), sPlayer.getY());
						effSprite.setVisible(true);
						effSprite.animate(EFFECT_ANIM_DURATION,EFFECT_FRAMES,false,new IAnimationListener(){

							@Override
							public void onAnimationStarted(
									AnimatedSprite pAnimatedSprite,
									int pInitialLoopCount) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onAnimationFrameChanged(
									AnimatedSprite pAnimatedSprite,
									int pOldFrameIndex, int pNewFrameIndex) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onAnimationLoopFinished(
									AnimatedSprite pAnimatedSprite,
									int pRemainingLoopCount,
									int pInitialLoopCount) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onAnimationFinished(
									AnimatedSprite pAnimatedSprite) {
								// TODO Auto-generated method stub
								effSprite.setVisible(false);
							}});
						
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

										engine.runOnUpdateThread(new Runnable() {
											public void run() {
												final EngineLock engineLock = ResourcesManager.getInstance().engine
														.getEngineLock();
												engineLock.lock(); // this is irrelative to efficiency

												// Now it is save to remove the entity! 
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
				// TODO Auto-generated method stub
				
			}});

		registerUpdateHandler(new TimerHandler(1f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				if(GameScene.this.mTime > 0) {
					mTime -= 1;
					timeText.setText("Time:" + mTime);
				}
				else{
					rm.musicBackground.pause();
					mCamera.setHUD(null);
					ResourcesManager.getInstance().setScores(score);
					SceneManager.getInstance().setCurrentScene(SceneType.SCENE_END);
				}
			}
		}));
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
	
	private void initFields() {
		enemySprite = new ArrayList<EnemySprite>();
		random = new Random(12411322);
		hud = new HUD();
		score = 0;
		scoreText = null; // init in loadFont
		mTime = 60;
		mGameRunning = true;
	}
	
	private void createWalls(Entity layerMiddle) {

		FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
		Rectangle startLeftWall = new Rectangle(0, 0, 15, 480,
				ResourcesManager.getInstance().vbom);
		ground = new Rectangle(0, Const.CAMERA_HEIGHT - 15, Const.CAMERA_WIDTH
				* LOOP_TIMES, 15, ResourcesManager.getInstance().vbom);
		ceil = new Rectangle(0, 0, Const.CAMERA_WIDTH * LOOP_TIMES, 20,
				ResourcesManager.getInstance().vbom);
		ground.setColor(new Color(15, 50, 0));
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
	
	private void createObstaclesToScene() {		
		for (int i = 0; i < LOOP_TIMES*Const.CAMERA_WIDTH; i += random.nextInt(Const.CAMERA_WIDTH)){
			
			switch(random.nextInt(4)){
			case 0:
				obstacleSprite = new Sprite(i, Const.CAMERA_HEIGHT-100, ResourcesManager.getInstance().obstacleTR, ResourcesManager.getInstance().vbom);
				break;
			case 1:
				obstacleSprite = new Sprite(i, Const.CAMERA_HEIGHT-334, ResourcesManager.getInstance().obstacleTR2, ResourcesManager.getInstance().vbom);
				break;
			case 2:
				obstacleSprite = new Sprite(i, Const.CAMERA_HEIGHT-191, ResourcesManager.getInstance().obstacleTR3, ResourcesManager.getInstance().vbom);
				break;
			case 3:
				obstacleSprite = new Sprite(i, Const.CAMERA_HEIGHT-62, ResourcesManager.getInstance().obstacleTR4, ResourcesManager.getInstance().vbom);
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

	private void loadFontTA() {
		// add fontTR to represent score in HUD
		timeText = new Text(0, 0, rm.mPlokFont, "Time:60", "Time:00".length(), rm.vbom);
		scoreText = new Text(600, 0, rm.mPlokFont, "Score:0", "Score: XXXXX".length(), rm.vbom);		
		hud.attachChild(scoreText);
		hud.attachChild(timeText);
		mCamera.setHUD(hud);
	}
}
