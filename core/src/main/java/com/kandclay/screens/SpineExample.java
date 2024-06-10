package com.kandclay.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;

public class SpineExample extends ApplicationAdapter {
    OrthographicCamera camera;
    SpriteBatch batch;
    SkeletonRenderer renderer;
    SkeletonRendererDebug debugRenderer;

    TextureAtlas atlas;
    Skeleton skeleton;
    AnimationState state;

    @Override
    public void create () {
        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        batch = new SpriteBatch();
        renderer = new SkeletonRenderer();
        renderer.setPremultipliedAlpha(true);
        debugRenderer = new SkeletonRendererDebug();

        // Load the Spine atlas and JSON
        atlas = new TextureAtlas(Gdx.files.internal("spine/skeleton.atlas"));
        SkeletonJson json = new SkeletonJson(atlas);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("spine/skeleton.json"));

        skeleton = new Skeleton(skeletonData);
        skeleton.setPosition(250, 150);

        AnimationStateData stateData = new AnimationStateData(skeletonData);
        state = new AnimationState(stateData);
        state.setAnimation(0, "animation", true);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        state.update(Gdx.graphics.getDeltaTime());
        state.apply(skeleton);
        skeleton.updateWorldTransform();

        camera.update();
        batch.getProjectionMatrix().set(camera.combined);

        batch.begin();
        renderer.draw(batch, skeleton);
        batch.end();

        debugRenderer.draw(skeleton);
    }

    @Override
    public void dispose () {
        atlas.dispose();
        batch.dispose();
    }
}
