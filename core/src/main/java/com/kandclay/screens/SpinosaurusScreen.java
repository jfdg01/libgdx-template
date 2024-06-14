package com.kandclay.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.kandclay.handlers.SpineAnimationHandler;
import com.kandclay.managers.AudioManager;
import com.kandclay.managers.MyAssetManager;
import com.kandclay.utils.Constants;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.attachments.RegionAttachment;

public class SpinosaurusScreen extends BaseScreen {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private SkeletonRenderer renderer;
    private SpineAnimationHandler spineAnimationHandler;

    private Skeleton skeleton;
    private AnimationState state;

    private BitmapFont font;
    private float speedMultiplier = 1f;

    private boolean isPlayHovered = false;
    private boolean isQuitHovered = false;
    private boolean isSettingsHovered = false;

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

        // Add input listener for hover detection
        stage.addListener(new InputListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                handleHover(x, y);
                return true;
            }
        });
    }

    private void initializeAnimations() {
        String atlasPath = Constants.Spinosaurus.ATLAS;
        String skeletonPath = Constants.Spinosaurus.JSON;

        skeleton = spineAnimationHandler.createSkeleton(atlasPath, skeletonPath);
        state = spineAnimationHandler.createAnimationState(skeleton);
        skeleton.setScale(1f, 1f);

        setSkeletonPosition();
        state.setAnimation(0, "animation", false);
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

    private boolean isHoveringButton(float x, float y, String buttonName) {
        Rectangle buttonBounds = getButtonBounds(buttonName);
        return buttonBounds.contains(x, y);
    }

    private Rectangle getButtonBounds(String buttonName) {
        Bone bone = skeleton.findBone(buttonName);
        if (bone == null) return new Rectangle();

        RegionAttachment attachment = (RegionAttachment) skeleton.findSlot(buttonName).getAttachment();
        if (attachment == null) return new Rectangle();

        // Get the world position of the bone
        float buttonX = bone.getWorldX() - (attachment.getWidth() * bone.getScaleX() / 2);
        float buttonY = bone.getWorldY() - (attachment.getHeight() * bone.getScaleY() / 2);

        // Get the size of the attachment
        float buttonWidth = attachment.getWidth() * bone.getScaleX();
        float buttonHeight = attachment.getHeight() * bone.getScaleY();

        // Adjust coordinates to match the bottom-left origin of the screen
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
