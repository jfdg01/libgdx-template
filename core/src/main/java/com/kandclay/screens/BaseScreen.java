package com.kandclay.screens;


import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kandclay.*;

public abstract class BaseScreen implements Screen {
    protected Stage stage;
    protected MyAssetManager assetManager;
    protected AudioManager audioManager;

    public BaseScreen(MyAssetManager assetManager, AudioManager audioManager) {
        this.assetManager = assetManager;
        this.audioManager = audioManager;
        this.stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        // Override in subclasses to set up the screen
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Override in subclasses if needed
    }

    @Override
    public void resume() {
        // Override in subclasses if needed
    }

    @Override
    public void hide() {
        // Override in subclasses to hide the screen
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

