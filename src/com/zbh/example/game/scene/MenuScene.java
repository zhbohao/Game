package com.zbh.example.game.scene;

import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.color.Color;

import android.opengl.GLES20;

import com.zbh.example.game.manager.ResourcesManager;
import com.zbh.example.game.manager.SceneManager.SceneType;
import com.zbh.example.game.util.Const;

public class MenuScene extends Scene implements IOnMenuItemClickListener{
	
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int MENU_PLAY = 0;
	private static final int MENU_ABOUT = 1;
	private TextureRegion parTR;
	// ===========================================================
	// Fields
	// ===========================================================
	ResourcesManager rm;
	org.andengine.entity.scene.menu.MenuScene ms;
	// ===========================================================
	// Constructors
	// ===========================================================
	
	public MenuScene(){
		rm = ResourcesManager.getInstance(); // get resources
		parTR = rm.getParticleSystemTR();
		createBackground();
		createMenuItem();
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public boolean onMenuItemClicked(
			org.andengine.entity.scene.menu.MenuScene pMenuScene,
			IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
		switch(pMenuItem.getID())
		{
			case MENU_PLAY:
				//Load Game Scene!
				com.zbh.example.game.manager.SceneManager.getInstance().setCurrentScene(SceneType.SCENE_GAME);
				return true;
			case MENU_ABOUT:
				/* Left to right Particle System. */
			{
				final SpriteParticleSystem particleSystem = new SpriteParticleSystem(new PointParticleEmitter(0, Const.CAMERA_HEIGHT), 6, 10, 200, this.parTR, rm.vbom);
				particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
				particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(15, 22, -60, -90));
				particleSystem.addParticleInitializer(new AccelerationParticleInitializer<Sprite>(5, 15));
				particleSystem.addParticleInitializer(new RotationParticleInitializer<Sprite>(0.0f, 360.0f));
				particleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(1.0f, 0.0f, 0.0f));
				particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(11.5f));

				particleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(0, 5, 0.5f, 2.0f));
				particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(2.5f, 3.5f, 1.0f, 0.0f));
				particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(3.5f, 4.5f, 0.0f, 1.0f));
				particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(0.0f, 11.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f));
				particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(4.5f, 11.5f, 1.0f, 0.0f));

				attachChild(particleSystem);
			}

			/* Right to left Particle System. */
			{
				final SpriteParticleSystem particleSystem = new SpriteParticleSystem(new PointParticleEmitter(Const.CAMERA_WIDTH - 32, Const.CAMERA_HEIGHT), 8, 12, 200, this.parTR, rm.vbom);
				particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
				particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-15, -22, -60, -90));
				particleSystem.addParticleInitializer(new AccelerationParticleInitializer<Sprite>(-5, 15));
				particleSystem.addParticleInitializer(new RotationParticleInitializer<Sprite>(0.0f, 360.0f));
				particleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(0.0f, 0.0f, 1.0f));
				particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(11.5f));

				particleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(0, 5, 0.5f, 2.0f));
				particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(2.5f, 3.5f, 1.0f, 0.0f));
				particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(3.5f, 4.5f, 0.0f, 1.0f));
				particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(0.0f, 11.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f));
				particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(4.5f, 11.5f, 1.0f, 0.0f));

				attachChild(particleSystem);
			}

				return true;
			default:
				return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void createMenuItem() {
		// TODO Auto-generated method stub
		ms = new org.andengine.entity.scene.menu.MenuScene(rm.camera);
		
		final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, rm.menuTR1, rm.vbom), 1.2f, 1);
		final IMenuItem aboutMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_ABOUT, rm.menuTR2, rm.vbom), 1.2f, 1);
		final Sprite momojiang = new Sprite(Const.CAMERA_WIDTH/9, (Const.CAMERA_HEIGHT-rm.getMomoiloveyouTR().getHeight())/3,rm.getMomoiloveyouTR(),rm.vbom);
		ms.attachChild(momojiang);
		ms.addMenuItem(playMenuItem);
		ms.addMenuItem(aboutMenuItem);
		
		ms.buildAnimations();
		ms.setBackgroundEnabled(false);
		
		float pX = (Const.CAMERA_WIDTH - playMenuItem.getWidth())/2;
		float pY = (Const.CAMERA_HEIGHT - playMenuItem.getHeight())/2;
		playMenuItem.setPosition(pX, pY - 50);
		aboutMenuItem.setPosition(pX, pY + 50);
		
		ms.setOnMenuItemClickListener(this);
		
		this.setChildScene(ms);
	}

	private void createBackground() {
		this.setBackground(new Background(0f, 0.8f, 1f));
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
