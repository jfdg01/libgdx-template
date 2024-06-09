package com.kandclay;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
    private MyAssetManager myAssetManager;
    private AnimationManager animationManager;
    private SpriteBatch batch;

    public void create() {

        myAssetManager = new MyAssetManager();
        myAssetManager.finishLoading();
        animationManager = new AnimationManager(myAssetManager);
        batch = new SpriteBatch();

        setScreen(new FirstScreen(this));
    }

    @Override
    public void render() {
        super.render(); // This will call render() on the active screen
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height); // This will call resize() on the active screen
    }

    @Override
    public void pause() {
        super.pause(); // This will call pause() on the active screen
    }

    @Override
    public void resume() {
        super.resume(); // This will call resume() on the active screen
    }

    @Override
    public void dispose() {
        super.dispose(); // This will call dispose() on the active screen
        myAssetManager.dispose();
        batch.dispose();
    }

    public MyAssetManager getAssetManager() {
        return myAssetManager;
    }

    public AnimationManager getAnimationManager() {
        return animationManager;
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
