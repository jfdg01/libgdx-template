package com.kandclay;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FirstScreen implements Screen {
    private final MyAssetManager myAssetManager;
    private final SpriteBatch batch;
    private final AnimationManager animationManager;
    private Animation<TextureRegion> animation;
    private float stateTime;

    public FirstScreen(Main game) {
        this.myAssetManager = game.getAssetManager();
        this.batch = game.getBatch();
        this.animationManager = game.getAnimationManager();
    }

    @Override
    public void show() {
        animation = animationManager.createAnimationFromAssetManager("earth");
        stateTime = 0f;
    }

    @Override
    public void render(float delta) {
        stateTime += delta;
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

        batch.begin();
        batch.draw(currentFrame, 50, 50);
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
