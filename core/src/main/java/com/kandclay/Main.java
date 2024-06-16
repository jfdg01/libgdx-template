package com.kandclay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
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

        screenManager.setScreen(ScreenType.MENU);
        // loadAndPlayRandomMusic();
    }

    private void loadInitialAssets() {

        assetManager.load(Constants.Skin.JSON, Skin.class);
        assetManager.load(Constants.MainMenu.ATLAS, TextureAtlas.class);
        assetManager.load(Constants.CursorTrail.ATLAS, TextureAtlas.class);
        assetManager.load(Constants.MainAnimation.ATLAS, TextureAtlas.class);
        assetManager.load(Constants.Coin.ATLAS, TextureAtlas.class);
        assetManager.load(Constants.CoinRed.ATLAS, TextureAtlas.class);

        assetManager.finishLoading();
    }

    private void loadCustomCursor() {
        Pixmap pixmap = new Pixmap(Gdx.files.internal(Constants.Cursor.IMAGE_PATH));
        Cursor cursor = Gdx.graphics.newCursor(pixmap, Constants.Cursor.HOTSPOT_X, Constants.Cursor.HOTSPOT_Y);
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

