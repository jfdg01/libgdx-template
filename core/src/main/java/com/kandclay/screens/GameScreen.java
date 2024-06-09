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
    private AnimatedActor animatedActor;
    private Skin skin = assetManager.get("skin/default/skin/uiskin.json", Skin.class);
    private AnimationHandler animationHandler;
    private SnapshotArray<Animation<TextureRegion>> animations;
    private int currentAnimationIndex;

    public GameScreen(MyAssetManager assetManager, AudioManager audioManager, AnimationHandler animationHandler) {
        super(assetManager, audioManager);
        gameState = GameState.RUNNING;
        this.animationHandler = animationHandler;
    }

    private void loadAnimations() {
        animations = new SnapshotArray<>();
        String[] textures = {
            "earth", "venus", "saturn", "moon", "sun", "uranus", "jupiter", "mars", "neptune", "mercury"
        };
        for (String texture : textures) {
            animations.add(animationHandler.createAnimationFromAssetManager(texture));
        }

        currentAnimationIndex = 0;
    }

    @Override
    public void show() {
        super.show();
        loadAnimations();
        animatedActor = new AnimatedActor(animations.get(currentAnimationIndex));
        animatedActor.setPosition(stage.getWidth() / 2, stage.getHeight() / 2);
        stage.addActor(animatedActor);

        createButton();

        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor
    }

    private void createButton() {
        TextButton button = new TextButton("Switch Animation", skin);
        button.setPosition(50, 50);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                switchAnimation();
            }
        });

        stage.addActor(button);
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

    private void switchAnimation() {
        currentAnimationIndex = (currentAnimationIndex + 1) % animations.size;
        animatedActor.setAnimation(animations.get(currentAnimationIndex));
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
