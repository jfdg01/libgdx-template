package com.kandclay;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FirstScreen implements Screen {
    private MyAssetManager myAssetManager;
    private SpriteBatch batch;
    private Texture earthTexture;

    public FirstScreen(MyAssetManager myAssetManager, SpriteBatch batch) {
        this.myAssetManager = myAssetManager;
        this.batch = batch;
    }

    @Override
    public void show() {
        earthTexture = myAssetManager.get("sprites/anim/earth.png", Texture.class);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(earthTexture, 50, 50);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        batch.dispose();
    }
}
