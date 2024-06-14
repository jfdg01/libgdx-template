package com.kandclay.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.kandclay.handlers.SpineAnimationHandler;
import com.kandclay.managers.AudioManager;
import com.kandclay.managers.MyAssetManager;
import com.kandclay.managers.ScreenManager;
import com.kandclay.utils.Constants;

import java.util.Iterator;

public abstract class BaseScreen implements Screen {
    protected Stage stage;
    protected MyAssetManager assetManager;
    protected AudioManager audioManager;
    protected SpineAnimationHandler spineAnimationHandler;
    protected ScreenManager screenManager;
    private SnapshotArray<TrailDot> trailDots;
    private int trailDotCount = 0;

    public BaseScreen(MyAssetManager assetManager, AudioManager audioManager, SpineAnimationHandler spineAnimationHandler, ScreenManager screenManager) {
        this.assetManager = assetManager;
        this.audioManager = audioManager;
        this.spineAnimationHandler = spineAnimationHandler;
        this.screenManager = screenManager;
        this.stage = new Stage(new ScreenViewport());
        this.trailDots = new SnapshotArray<>();
        Gdx.input.setInputProcessor(stage);

        stage.addListener(new InputListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                createTrailDot(x, y);
                return true;
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    void renderTrail(float delta) {
        stage.getBatch().begin();
        Iterator<TrailDot> iterator = trailDots.iterator();
        while (iterator.hasNext()) {
            TrailDot trailDot = iterator.next();
            trailDot.state.update(delta);
            trailDot.state.apply(trailDot.skeleton);
            trailDot.skeleton.updateWorldTransform();
            trailDot.skeleton.setPosition(trailDot.x, trailDot.y);

            trailDot.renderer.draw(stage.getBatch(), trailDot.skeleton);

            if (trailDot.state.getCurrent(0) == null || trailDot.state.getCurrent(0).isComplete()) {
                iterator.remove();
            }
        }
        stage.getBatch().end();
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

    private void createTrailDot(float x, float y) {
        final int numberOfColors = 360;
        final float saturation = 1.0f;
        final float value = 1.0f;
        final float alpha = 0.5f;

        float hue = (trailDotCount % numberOfColors);

        Color currentColor = new Color();
        currentColor.fromHsv(hue, saturation, value);
        currentColor.a = alpha;

        String trailAtlasPath = Constants.Trail.ATLAS;
        String trailSkeletonPath = Constants.Trail.JSON;

        Skeleton trailSkeleton = spineAnimationHandler.createSkeleton(trailAtlasPath, trailSkeletonPath);
        AnimationState trailState = spineAnimationHandler.createAnimationState(trailSkeleton);
        SkeletonRenderer trailRenderer = new SkeletonRenderer();
        trailRenderer.setPremultipliedAlpha(true);

        trailSkeleton.setPosition(x, y);
        trailSkeleton.setColor(currentColor);
        trailSkeleton.setScale(0.5f, 0.5f);

        trailState.setAnimation(0, "animation", false);

        trailDots.add(new TrailDot(trailSkeleton, trailState, trailRenderer, x, y));
        trailDotCount++;
    }

    private static class TrailDot {
        public Skeleton skeleton;
        public AnimationState state;
        public SkeletonRenderer renderer;
        public float x, y;

        public TrailDot(Skeleton skeleton, AnimationState state, SkeletonRenderer renderer, float x, float y) {
            this.skeleton = skeleton;
            this.state = state;
            this.renderer = renderer;
            this.x = x;
            this.y = y;
        }
    }
}

