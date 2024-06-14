package com.kandclay.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.SnapshotArray;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.kandclay.handlers.SpineAnimationHandler;
import com.kandclay.managers.AudioManager;
import com.kandclay.managers.MyAssetManager;
import com.kandclay.managers.ScreenManager;
import com.kandclay.utils.Constants;

import java.util.Iterator;

public class SpinosaurusScreen extends BaseScreen {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private SkeletonRenderer renderer;
    private SpineAnimationHandler spineAnimationHandler;
    private ScreenManager screenManager;

    private Skeleton skeleton;
    private AnimationState state;

    private SnapshotArray<Explosion> explosions;

    private BitmapFont font;
    private float speedMultiplier = 1f;

    private boolean isPlayHovered = false;
    private boolean isQuitHovered = false;
    private boolean isSettingsHovered = false;

    public SpinosaurusScreen(MyAssetManager assetManager, AudioManager audioManager, SpineAnimationHandler spineAnimationHandler, ScreenManager screenManager) {
        super(assetManager, audioManager);
        this.spineAnimationHandler = spineAnimationHandler;
        this.screenManager = screenManager;
    }

    @Override
    public void show() {
        super.show();

        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        renderer = new SkeletonRenderer();
        renderer.setPremultipliedAlpha(true);
        explosions = new SnapshotArray<>();

        initializeAnimations();

        // Set up the camera
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Initialize font
        font = new BitmapFont();

        // Add input listener for hover and click detection
        stage.addListener(new InputListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                handleHover(x, y);
                return true;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                handleClick(x, y);
                return true;
            }
        });
    }

    private void initializeAnimations() {
        String atlasPath = Constants.UI.ATLAS;
        String skeletonPath = Constants.UI.JSON;

        skeleton = spineAnimationHandler.createSkeleton(atlasPath, skeletonPath);
        state = spineAnimationHandler.createAnimationState(skeleton);
        skeleton.setScale(1f, 1f);

        setSkeletonPosition();
        state.setAnimation(0, "animation", false);

        // Load explosion animation
        String explosionAtlasPath = Constants.UI.EXPLOSION_ATLAS;
        String explosionSkeletonPath = Constants.UI.EXPLOSION_JSON;
    }

    private void handleHover(float x, float y) {
        Vector2 stageCoords = stage.screenToStageCoordinates(new Vector2(x, y));

        if (isHoveringButton(stageCoords.x, stageCoords.y, "play")) {
            if (!isPlayHovered) {
                state.setAnimation(1, "Buttons/PlayHoverIn", false);
                isPlayHovered = true;
            }
        } else {
            if (isPlayHovered) {
                state.setAnimation(1, "Buttons/PlayHoverOut", false);
                isPlayHovered = false;
            }
        }

        if (isHoveringButton(stageCoords.x, stageCoords.y, "quit")) {
            if (!isQuitHovered) {
                state.setAnimation(2, "Buttons/QuitHoverIn", false);
                isQuitHovered = true;
            }
        } else {
            if (isQuitHovered) {
                state.setAnimation(2, "Buttons/QuitHoverOut", false);
                isQuitHovered = false;
            }
        }

        if (isHoveringButton(stageCoords.x, stageCoords.y, "settings")) {
            if (!isSettingsHovered) {
                state.setAnimation(3, "Buttons/SettingsHoverIn", false);
                isSettingsHovered = true;
            }
        } else {
            if (isSettingsHovered) {
                state.setAnimation(3, "Buttons/SettingsHoverOut", false);
                isSettingsHovered = false;
            }
        }
    }

    private void handleClick(float x, float y) {
        Vector2 stageCoords = stage.screenToStageCoordinates(new Vector2(x, y));

        String explosionAtlasPath = Constants.UI.EXPLOSION_ATLAS;
        String explosionSkeletonPath = Constants.UI.EXPLOSION_JSON;

        Skeleton explosionSkeleton = spineAnimationHandler.createSkeleton(explosionAtlasPath, explosionSkeletonPath);
        AnimationState explosionState = spineAnimationHandler.createAnimationState(explosionSkeleton);

        explosionSkeleton.setPosition(x, y);
        explosionState.setAnimation(0, "animation", false);

        explosions.add(new Explosion(explosionSkeleton, explosionState));


        if (isHoveringButton(stageCoords.x, stageCoords.y, "play")) {
            // screenManager.setScreen(Constants.ScreenType.KNIFE1);
        } else if (isHoveringButton(stageCoords.x, stageCoords.y, "quit")) {
            // Gdx.app.exit();
        } else if (isHoveringButton(stageCoords.x, stageCoords.y, "settings")) {
            // screenManager.setScreen(Constants.ScreenType.OPTIONS);
        }
    }

    private boolean isHoveringButton(float x, float y, String buttonName) {
        Rectangle buttonBounds = getButtonBounds(buttonName);
        return buttonBounds.contains(x, y);
    }

    private Rectangle getButtonBounds(String buttonName) {
        Bone bone = skeleton.findBone(buttonName);
        if (bone == null) return new Rectangle();

        RegionAttachment attachment = (RegionAttachment) skeleton.findSlot(buttonName).getAttachment();
        if (attachment == null) return new Rectangle();

        float buttonX = bone.getWorldX() - (attachment.getWidth() * bone.getScaleX() / 2);
        float buttonY = bone.getWorldY() - (attachment.getHeight() * bone.getScaleY() / 2);
        float buttonWidth = attachment.getWidth() * bone.getScaleX();
        float buttonHeight = attachment.getHeight() * bone.getScaleY();

        buttonY = camera.viewportHeight - buttonY - buttonHeight;

        return new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
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
        Iterator<Explosion> iterator = explosions.iterator();
        while (iterator.hasNext()) {
            Explosion explosion = iterator.next();
            explosion.state.update(delta);
            explosion.state.apply(explosion.skeleton);
            explosion.skeleton.updateWorldTransform();

            renderer.draw(batch, explosion.skeleton);

            // Remove the explosion if the animation is complete
            if (explosion.state.getCurrent(0) == null || explosion.state.getCurrent(0).isComplete()) {
                iterator.remove();
            }
        }
        batch.end();

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

    private static class Explosion {
        public Skeleton skeleton;
        public AnimationState state;

        public Explosion(Skeleton skeleton, AnimationState state) {
            this.skeleton = skeleton;
            this.state = state;
        }
    }
}
