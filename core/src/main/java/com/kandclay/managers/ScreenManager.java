package com.kandclay.managers;

import com.kandclay.handlers.SpriteSheetAnimationHandler;
import com.kandclay.handlers.SpineAnimationHandler;
import com.kandclay.screens.*;
import com.kandclay.utils.Constants.ScreenType;

public class ScreenManager {
    private MyAssetManager assetManager;
    private AudioManager audioManager;
    private ConfigurationManager configManager;
    private BaseScreen currentScreen;
    private SpriteSheetAnimationHandler spriteSheetAnimationHandler;
    private SpineAnimationHandler spineAnimationHandler;

    public ScreenManager() {
        this.assetManager = MyAssetManager.getInstance();
        this.audioManager = AudioManager.getInstance();
        this.configManager = ConfigurationManager.getInstance();
        this.spriteSheetAnimationHandler = new SpriteSheetAnimationHandler();
        this.spineAnimationHandler = new SpineAnimationHandler();
    }

    public void setScreen(ScreenType screenType) {
        if (currentScreen != null) {
            currentScreen.dispose();
        }
        switch (screenType) {
            case MENU:
                currentScreen = new MainMenuScreen(spineAnimationHandler, this);
                break;
            case GAME:
                currentScreen = new MainAnimationScreen(spineAnimationHandler, this);
                break;
            case OPTIONS:
                currentScreen = new ConfigurationScreen(spineAnimationHandler, this);
                break;
        }
        currentScreen.show();
    }

    public void render(float delta) {
        if (currentScreen != null) {
            currentScreen.render(delta);
        }
    }

    public void resize(int width, int height) {
        if (currentScreen != null) {
            currentScreen.resize(width, height);
        }
    }

    public void pause() {
        if (currentScreen != null) {
            currentScreen.pause();
        }
    }

    public void resume() {
        if (currentScreen != null) {
            currentScreen.resume();
        }
    }

    public void dispose() {
        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }
}

