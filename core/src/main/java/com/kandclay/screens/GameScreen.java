package com.kandclay.screens;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kandclay.*;

public class GameScreen extends BaseScreen {
    private GameState gameState;
    private Animation<TextureRegion> earthAnimation;
    private Animation<TextureRegion> venusAnimation;
    private AnimatedActor animatedActor;
    private float timer;
    private boolean showingEarth;

    public GameScreen(MyAssetManager assetManager, AudioManager audioManager, AnimationHandler animationHandler) {
        super(assetManager, audioManager);
        gameState = GameState.RUNNING;
        this.earthAnimation = animationHandler.createAnimationFromAssetManager("earth");
        this.venusAnimation = animationHandler.createAnimationFromAssetManager("venus");
        this.timer = 0;
        this.showingEarth = true;
    }

    @Override
    public void show() {
        super.show();
        animatedActor = new AnimatedActor(earthAnimation);
        animatedActor.setPosition(stage.getWidth() / 2, stage.getHeight() / 2);
        stage.addActor(animatedActor);
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

        // Update timer and switch animation if needed
        timer += delta;
        if (timer >= 3) {
            switchAnimation();
            timer = 0;
        }
    }

    private void updateGame(float delta) {
        // Game update logic
    }

    private void switchAnimation() {
        if (showingEarth) {
            animatedActor.setAnimation(venusAnimation);
        } else {
            animatedActor.setAnimation(earthAnimation);
        }
        showingEarth = !showingEarth;
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

