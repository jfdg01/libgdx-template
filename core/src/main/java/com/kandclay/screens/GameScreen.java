package com.kandclay.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.SnapshotArray;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.kandclay.actors.SpineAnimatedActor;
import com.kandclay.handlers.AnimationHandler;
import com.kandclay.handlers.SpineAnimationHandler;
import com.kandclay.managers.AudioManager;
import com.kandclay.managers.MyAssetManager;
import com.kandclay.utils.GameState;

public class GameScreen extends BaseScreen {
    private GameState gameState;
    private final Skin skin;
    private final AnimationHandler animationHandler;
    private final SpineAnimationHandler spineAnimationHandler;
    private SnapshotArray<SpineAnimatedActor> spineActors;
    private float elapsedTime;

    public GameScreen(MyAssetManager assetManager, AudioManager audioManager, AnimationHandler animationHandler, SpineAnimationHandler spineAnimationHandler) {
        super(assetManager, audioManager);
        gameState = GameState.RUNNING;
        this.animationHandler = animationHandler;
        this.spineAnimationHandler = spineAnimationHandler;
        this.skin = assetManager.get("skin/default/skin/uiskin.json", Skin.class);
    }

    private void loadAnimations() {
        spineActors = new SnapshotArray<>();
        String[] spineFiles = {
            "spine/skeleton.atlas", "spine/skeleton.json"
        };

        Skeleton skeleton = spineAnimationHandler.createSkeleton(spineFiles[0], spineFiles[1]);
        AnimationState state = spineAnimationHandler.createAnimationState(skeleton);
        SpineAnimatedActor spineActor = new SpineAnimatedActor(skeleton, state);
        spineActor.setAnimation("animation", true);
        spineActors.add(spineActor);
        stage.addActor(spineActor);
    }

    @Override
    public void show() {
        super.show();
        loadAnimations();
        Gdx.input.setInputProcessor(stage);
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        elapsedTime += delta;
        updateGame(delta);
        super.render(delta);
    }

    private void updateGame(float delta) {
        // Update the positions of the actors
        arrangeActorsInCircle(spineActors, elapsedTime);
    }

    private void arrangeActorsInCircle(SnapshotArray<SpineAnimatedActor> actors, float elapsedTime) {
        float centerX = stage.getWidth() / 2;
        float centerY = stage.getHeight() / 2;
        float radius = Math.min(stage.getWidth(), stage.getHeight()) / 3;

        int numberOfActors = actors.size;
        for (int i = 0; i < numberOfActors; i++) {
            float angle = (float) (2 * Math.PI * i / numberOfActors);
            float x = centerX + radius * (float) Math.cos(angle);
            float y = centerY + radius * (float) Math.sin(angle);
            SpineAnimatedActor actor = actors.get(i);
            actor.setSkeletonPosition(x - actor.getWidth() / 2, y - actor.getHeight() / 2);
            actor.setVisible(true);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        arrangeActorsInCircle(spineActors, elapsedTime);
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

