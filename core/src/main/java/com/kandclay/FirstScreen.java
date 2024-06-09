package com.kandclay;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class FirstScreen implements Screen {
    private final MyAssetManager myAssetManager;
    private final AnimationManager animationManager;
    private Animation<TextureRegion> animation;
    private Stage stage;

    public FirstScreen(Main game) {
        this.myAssetManager = game.getAssetManager();
        this.animationManager = game.getAnimationManager();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Animation<TextureRegion> planetAnimation = animationManager.createAnimationFromAssetManager("earth");

        AnimatedActor planetActor = new AnimatedActor(planetAnimation);
        planetActor.setPosition(100, 100);
        planetActor.setSize(50, 50);

        stage.addActor(planetActor);
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
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
        stage.dispose();
    }
}
