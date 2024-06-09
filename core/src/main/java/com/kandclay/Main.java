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

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private MyAssetManager assetManager;
    private AudioManager audioManager;
    private ConfigurationManager configManager;
    private ScreenManager screenManager;

    @Override
    public void create() {
        // Initialize configuration manager first
        configManager = ConfigurationManager.getInstance();

        // Initialize asset manager and audio manager
        assetManager = MyAssetManager.getInstance();
        audioManager = AudioManager.getInstance();

        // Initialize screen manager
        screenManager = new ScreenManager(assetManager, audioManager, configManager);

        // Load initial assets and set the initial screen
        loadInitialAssets();
        screenManager.setScreen(ScreenType.MENU);
    }

    private void loadInitialAssets() {
        // Load essential assets
        assetManager.load("sprites/anim/earth.png", Texture.class);
        //assetManager.load("sounds/jump.wav", Sound.class);
        //assetManager.load("music/background.mp3", Music.class);
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

