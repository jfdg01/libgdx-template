package com.kandclay.managers;

import com.kandclay.handlers.AnimationHandler;
import com.kandclay.utils.ScreenType;
import com.kandclay.handlers.SpineAnimationHandler;
import com.kandclay.screens.*;

public class ScreenManager {
    private MyAssetManager assetManager;
    private AudioManager audioManager;
    private ConfigurationManager configManager;
    private BaseScreen currentScreen;
    private AnimationHandler animationHandler;
    private SpineAnimationHandler spineAnimationHandler;

    public ScreenManager(MyAssetManager assetManager, AudioManager audioManager, ConfigurationManager configManager) {
        this.assetManager = assetManager;
        this.audioManager = audioManager;
        this.configManager = configManager;
        this.animationHandler = new AnimationHandler(assetManager);
        this.spineAnimationHandler = new SpineAnimationHandler(assetManager);
    }

    public void setScreen(ScreenType screenType) {
        if (currentScreen != null) {
            currentScreen.dispose();
        }
        switch (screenType) {
            case MENU:
                currentScreen = new MenuScreen(assetManager, audioManager);
                break;
            case GAME:
                currentScreen = new GameScreen(assetManager, audioManager, animationHandler, spineAnimationHandler);
                break;
            case OPTIONS:
                currentScreen = new OptionsScreen(assetManager, audioManager);
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

