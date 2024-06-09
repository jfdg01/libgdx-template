package com.kandclay;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private MyAssetManager assetManager;
    private AudioManager audioManager;
    private ConfigurationManager configManager;
    private ScreenManager screenManager;
    private AnimationHandler animationHandler;

    @Override
    public void create() {
        // Initialize configuration manager first
        configManager = ConfigurationManager.getInstance();

        // Initialize asset manager and audio manager
        assetManager = MyAssetManager.getInstance();
        audioManager = AudioManager.getInstance();

        // Initialize screen manager
        screenManager = new ScreenManager(assetManager, audioManager, configManager);

        // Initialize animation handler
        animationHandler = new AnimationHandler(assetManager);

        // Load initial assets and set the initial screen
        loadInitialAssets();
        screenManager.setScreen(ScreenType.GAME);
    }

    private void loadInitialAssets() {
        // Load essential assets
        String[] textures = {
            "sprites/static/backgroundSimple.png",
            "sprites/anim/earth.png",
            "sprites/anim/saturn.png",
            "sprites/anim/moon.png",
            "sprites/anim/sun.png",
            "sprites/anim/uranus.png",
            "sprites/anim/venus.png",
            "sprites/anim/jupiter.png",
            "sprites/anim/mars.png",
            "sprites/anim/neptune.png",
            "sprites/anim/mercury.png",
        };
        for (String texture : textures) {
            assetManager.load(texture, Texture.class);
        }
        assetManager.load("skin/default/skin/uiskin.json", Skin.class);
        assetManager.finishLoading();
    }

    @Override
    public void render() {
        // Delegate rendering to the screen manager
        screenManager.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize(int width, int height) {
        // Delegate resizing to the current screen
        screenManager.resize(width, height);
    }

    @Override
    public void pause() {
        // Delegate pausing to the current screen
        screenManager.pause();
    }

    @Override
    public void resume() {
        // Delegate resuming to the current screen
        screenManager.resume();
    }

    @Override
    public void dispose() {
        // Dispose of all resources
        screenManager.dispose();
        assetManager.dispose();
        audioManager.dispose();
        batch.dispose();
    }
}

