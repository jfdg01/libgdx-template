package com.kandclay.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.SnapshotArray;
import com.kandclay.*;

public class GameScreen extends BaseScreen {
    private GameState gameState;
    private final Skin skin = assetManager.get("skin/default/skin/uiskin.json", Skin.class);
    private final AnimationHandler animationHandler;
    private SnapshotArray<AnimatedActor> animatedActors;

    public GameScreen(MyAssetManager assetManager, AudioManager audioManager, AnimationHandler animationHandler) {
        super(assetManager, audioManager);
        gameState = GameState.RUNNING;
        this.animationHandler = animationHandler;
    }

    private void loadAnimations() {
        animatedActors = new SnapshotArray<>();
        String[] textures = {
            "sun", "mercury", "venus", "earth", "moon", "mars", "jupiter", "saturn", "uranus", "neptune",
        };
        for (String texture : textures) {
            Animation<TextureRegion> animation = animationHandler.createAnimationFromAssetManager(texture);
            AnimatedActor actor = new AnimatedActor(animation);
            animatedActors.add(actor);
            stage.addActor(actor);
        }

        arrangeActorsInCircle(animatedActors);
    }

    @Override
    public void show() {
        super.show();
        loadAnimations();
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Set the clear color to black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the color buffer
    }

    @Override
    public void render(float delta) {
        clearScreen();

        super.render(delta);

        switch (gameState) {
            case RUNNING:
                // Implement game logic when running
                updateGame(delta);
                break;
            case PAUSED:
                // Implement logic when the game is paused
                break;
            case GAME_OVER:
                // Implement logic when the game is over
                break;
        }
    }

    private void updateGame(float delta) {
        // Game update logic
    }

    private void arrangeActorsInCircle(SnapshotArray<AnimatedActor> actors) {
        float centerX = stage.getWidth() / 2;
        float centerY = stage.getHeight() / 2;
        float radius = Math.min(stage.getWidth(), stage.getHeight()) / 3;

        int numberOfActors = actors.size;
        for (int i = 0; i < numberOfActors; i++) {
            float angle = (float) (2 * Math.PI * i / numberOfActors);
            float x = centerX + radius * (float) Math.cos(angle);
            float y = centerY + radius * (float) Math.sin(angle);
            AnimatedActor actor = actors.get(i);
            actor.setPosition(x - actor.getWidth() / 2, y - actor.getHeight() / 2);
            actor.setVisible(true); // Make all actors visible
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        arrangeActorsInCircle(animatedActors); // Rearrange actors on resize
    }

    public void pauseGame() {
        gameState = GameState.PAUSED;
    }

    public void resumeGame() {
        gameState = GameState.RUNNING;
    }

    public void gameOver() {
        gameState = GameState.GAME_OVER;
    }
}


