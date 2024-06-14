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
                currentScreen = new MenuScreen(spineAnimationHandler, this);
                break;
            case KNIFE1:
                currentScreen = new SpineKnife1Screen(spineAnimationHandler, this);
                break;
            case KNIFE2:
                currentScreen = new SpineKnife2Screen(spineAnimationHandler, this);
                break;
            case OPTIONS:
                currentScreen = new OptionsScreen(spineAnimationHandler, this);
                break;
            case COUNTER:
                currentScreen = new CounterScreen(spineAnimationHandler, this);
                break;
            case SPINOSAURUS:
                currentScreen = new SpinosaurusScreen(spineAnimationHandler, this);
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

