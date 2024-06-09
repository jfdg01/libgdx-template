package com.kandclay;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

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

        // Load and play random music
        loadAndPlayRandomMusic();
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

    private void loadAndPlayRandomMusic() {
        FileHandle dirHandle = Gdx.files.internal("music");
        Array<FileHandle> musicFiles = new Array<>(dirHandle.list("ogg"));

        // Load all music files
        for (FileHandle file : musicFiles) {
            audioManager.loadMusic(file.path());
        }

        // Select a random music file
        Random random = new Random();
        int randomIndex = random.nextInt(musicFiles.size);
        String randomMusicFile = musicFiles.get(randomIndex).path();

        // Play the random music file in a loop
        Gdx.app.log("Playing music", randomMusicFile);
        audioManager.playMusic(randomMusicFile, true);
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
        if (screenManager != null) {
            screenManager.dispose();
        }
        if (assetManager != null) {
            assetManager.dispose();
        }
        if (audioManager != null) {
            audioManager.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }
    }
}

