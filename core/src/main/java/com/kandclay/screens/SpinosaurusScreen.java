package com.kandclay.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.kandclay.handlers.SpineAnimationHandler;
import com.kandclay.managers.AudioManager;
import com.kandclay.managers.MyAssetManager;
import com.kandclay.utils.Constants;

public class SpinosaurusScreen extends BaseScreen {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private SkeletonRenderer renderer;
    private SpineAnimationHandler spineAnimationHandler;

    private Skeleton skeleton;
    private AnimationState state;

    private BitmapFont font;
    private float speedMultiplier = 1f;

    public SpinosaurusScreen(MyAssetManager assetManager, AudioManager audioManager, SpineAnimationHandler spineAnimationHandler) {
        super(assetManager, audioManager);
        this.spineAnimationHandler = spineAnimationHandler;
    }

    @Override
    public void show() {
        super.show();

        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        renderer = new SkeletonRenderer();
        renderer.setPremultipliedAlpha(true);

        initializeAnimations();

        // Set up the camera
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Initialize font
        font = new BitmapFont();
    }

    private void initializeAnimations() {
        String atlasPath = Constants.Spinosaurus.ATLAS;
        String skeletonPath = Constants.Spinosaurus.JSON;

        skeleton = spineAnimationHandler.createSkeleton(atlasPath, skeletonPath);
        state = spineAnimationHandler.createAnimationState(skeleton);
        skeleton.setScale(1f, 1f);

        setSkeletonPosition();
        state.setAnimation(0, "animation", true);  // Loop the animation by default
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        state.update(delta * speedMultiplier);

        state.apply(skeleton);
        skeleton.updateWorldTransform();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        renderer.draw(batch, skeleton);
        batch.end();

        // Ensure the skeleton position is updated every frame to follow the center
        setSkeletonPosition();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        camera.setToOrtho(false, width, height);
        camera.update();
        setSkeletonPosition();
    }

    private void setSkeletonPosition() {
        // Center the skeleton on the screen
        float centerX = camera.viewportWidth / 2;
        float centerY = camera.viewportHeight / 2;
        skeleton.setPosition(centerX, centerY);
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        font.dispose();
    }
}

