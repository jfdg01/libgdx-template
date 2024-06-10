package com.kandclay.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.spine.Bone;

public class SpineExample extends ApplicationAdapter {
    OrthographicCamera camera;
    SpriteBatch batch;
    SkeletonRenderer renderer;
    SkeletonRendererDebug debugRenderer;

    TextureAtlas atlas;
    Skeleton skeleton;
    AnimationState state;

    Stage stage;
    Slider slider;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        renderer = new SkeletonRenderer();
        renderer.setPremultipliedAlpha(true);
        debugRenderer = new SkeletonRendererDebug();

        // Load the Spine atlas and JSON
        atlas = new TextureAtlas(Gdx.files.internal("spine/skeleton.atlas"));
        SkeletonJson json = new SkeletonJson(atlas);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("spine/skeleton.json"));

        skeleton = new Skeleton(skeletonData);
        setSkeletonPosition();
        skeleton.setScale(4f, 4f);

        AnimationStateData stateData = new AnimationStateData(skeletonData);
        state = new AnimationState(stateData);
        state.setAnimation(0, "animation", true); // Set to false to prevent autoplay

        // Add event listener
        state.addListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void event(AnimationState.TrackEntry entry, com.esotericsoftware.spine.Event event) {
                if ("in".equals(event.getData().getName())) {
                    System.out.println("Event 'in' triggered!");
                }
            }
        });

        // Set up the UI
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("skin/default/skin/uiskin.json"));
        slider = new Slider(0, 1, 0.01f, false, skin); // Slider range from 0 to 1
        slider.addListener(event -> {
            if (!slider.isDragging()) {
                float progress = slider.getValue();
                float animationDuration = state.getCurrent(0).getAnimation().getDuration();
                state.getCurrent(0).setTrackTime(progress * animationDuration);
            }
            return false;
        });

        Table table = new Table();
        table.setFillParent(true);
        table.bottom();
        table.add(slider).width(400); // Adjust width as needed

        stage.addActor(table);

        // Set up the camera
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        state.update(Gdx.graphics.getDeltaTime());
        state.apply(skeleton);
        skeleton.updateWorldTransform();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        renderer.draw(batch, skeleton);
        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        // debugRenderer.draw(skeleton);
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
        setSkeletonPosition();
        stage.getViewport().update(width, height, true);
    }

    private void setSkeletonPosition() {
        // Center the skeleton on the screen
        float centerX = camera.viewportWidth / 2 - skeleton.getData().getWidth() / 2;
        float centerY = camera.viewportHeight / 2 - skeleton.getData().getHeight() / 2;
        skeleton.setPosition(centerX, centerY);
    }

    @Override
    public void dispose() {
        atlas.dispose();
        batch.dispose();
        stage.dispose();
    }
}
