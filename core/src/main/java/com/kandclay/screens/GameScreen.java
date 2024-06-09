package com.kandclay.screens;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kandclay.*;

public class GameScreen extends BaseScreen {
    private GameState gameState;
    private Animation<TextureRegion> earthAnimation;

    public GameScreen(MyAssetManager assetManager, AudioManager audioManager, AnimationHandler animationHandler) {
        super(assetManager, audioManager);
        gameState = GameState.RUNNING;
        this.earthAnimation = animationHandler.createAnimationFromAssetManager("earth");
    }

    @Override
    public void show() {
        super.show();
        AnimatedActor earthActor = new AnimatedActor(earthAnimation);
        earthActor.setPosition(stage.getWidth() / 2, stage.getHeight() / 2);
        stage.addActor(earthActor);
    }

    @Override
    public void render(float delta) {
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

