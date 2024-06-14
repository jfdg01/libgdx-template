package com.kandclay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.kandclay.handlers.SpriteSheetAnimationHandler;
import com.kandclay.handlers.SpineAnimationHandler;
import com.kandclay.managers.AudioManager;
import com.kandclay.managers.ConfigurationManager;
import com.kandclay.managers.MyAssetManager;
import com.kandclay.managers.ScreenManager;
import com.kandclay.utils.Constants;
import com.kandclay.utils.Constants.ScreenType;

import java.util.Random;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private MyAssetManager assetManager;
    private AudioManager audioManager;
    private ConfigurationManager configManager;
    private ScreenManager screenManager;
    private SpriteSheetAnimationHandler spriteSheetAnimationHandler;
    private SpineAnimationHandler spineAnimationHandler;

    @Override
    public void create() {
        configManager = ConfigurationManager.getInstance();
        assetManager = MyAssetManager.getInstance();
        audioManager = AudioManager.getInstance();
        screenManager = new ScreenManager();
        spriteSheetAnimationHandler = new SpriteSheetAnimationHandler();
        spineAnimationHandler = new SpineAnimationHandler();

        loadInitialAssets();

        loadCustomCursor();

        screenManager.setScreen(ScreenType.SPINOSAURUS);
        // loadAndPlayRandomMusic();
    }

    private void loadInitialAssets() {
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

        assetManager.load(Constants.Skin.JSON, Skin.class);
        assetManager.load(Constants.KnifePlanet.ATLAS, TextureAtlas.class);
        assetManager.load(Constants.KnifePlanet2.ATLAS, TextureAtlas.class);
        assetManager.load(Constants.Spinosaurus.ATLAS, TextureAtlas.class);
        assetManager.load(Constants.Trail.ATLAS, TextureAtlas.class);
        assetManager.load("cursor.png", Pixmap.class);

        assetManager.finishLoading();
    }

    private void loadCustomCursor() {
        Pixmap pixmap = new Pixmap(Gdx.files.internal("cursor.png"));
        Cursor cursor = Gdx.graphics.newCursor(pixmap, pixmap.getWidth() / 2, pixmap.getHeight() / 2);
        Gdx.graphics.setCursor(cursor);
        pixmap.dispose();
    }

    private void loadAndPlayRandomMusic() {
        FileHandle dirHandle = Gdx.files.internal("music");
        Array<FileHandle> musicFiles = new Array<>(dirHandle.list("ogg"));
        for (FileHandle file : musicFiles) {
            audioManager.loadMusic(file.path());
        }

        audioManager.loadSound(Constants.Sounds.OOF);

        Random random = new Random(System.nanoTime());
        int randomIndex = random.nextInt(musicFiles.size);
        String randomMusicFile = musicFiles.get(randomIndex).path();
        Gdx.app.log("Playing music", randomMusicFile);
        audioManager.playMusic(randomMusicFile, true);
    }

    @Override
    public void render() {
        screenManager.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize(int width, int height) {
        screenManager.resize(width, height);
    }

    @Override
    public void pause() {
        screenManager.pause();
    }

    @Override
    public void resume() {
        screenManager.resume();
    }

    @Override
    public void dispose() {
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

