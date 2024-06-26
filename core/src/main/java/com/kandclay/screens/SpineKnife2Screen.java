package com.kandclay.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.spine.*;

import com.kandclay.handlers.SpineAnimationHandler;
import com.kandclay.utils.Constants;
import com.kandclay.managers.*;

public class SpineKnife2Screen extends BaseScreen {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private SkeletonRenderer renderer;
    private SkeletonRendererDebug debugRenderer;
    private SpineAnimationHandler spineAnimationHandler;

    private Skeleton skeleton;
    private AnimationState state;

    private Slider slider;
    private TextButton modeButton;
    private BitmapFont font;

    private boolean isLooping = true; // Set initial state to automatic (looping)

    private int inEventCounter = 0;
    private boolean inEventTriggered = false;
    private float speedMultiplier = 1f;
    private boolean animationStarted = false;
    private float lastSliderValue = 0f; // Track last slider value for changes

    public SpineKnife2Screen(SpineAnimationHandler spineAnimationHandler, ScreenManager screenManager) {
        super(spineAnimationHandler, screenManager);
        this.spineAnimationHandler = spineAnimationHandler;
    }

    @Override
    public void show() {
        super.show();

        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        renderer = new SkeletonRenderer();
        renderer.setPremultipliedAlpha(true);
        debugRenderer = new SkeletonRendererDebug();

        initializeAnimations();

        Skin skin = assetManager.get(Constants.Skin.JSON, Skin.class);
        slider = new Slider(0, 1, 0.01f, false, skin);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!isLooping) {
                    float progress = slider.getValue();
                    float animationDuration = state.getCurrent(0).getAnimation().getDuration();
                    state.getCurrent(0).setTrackTime(progress * animationDuration);

                    // Reset the event trigger flag if the slider is moved back past the trigger point
                    if (progress < 0.5f) {
                        inEventTriggered = false;
                    }

                    if (progress != lastSliderValue) {
                        lastSliderValue = progress;
                        System.out.println("Slider changed: " + slider.getValue() + " Mode: Manual");
                    }
                }
            }
        });

        modeButton = new TextButton("Pasar a modo manual", skin); // Set initial button text to switch to manual
        modeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isLooping = !isLooping;
                System.out.println("Mode changed to: " + (isLooping ? "Automatic" : "Manual"));
                if (isLooping) {
                    modeButton.setText("Pasar a modo manual");
                    state.setAnimation(0, "animation", true);  // Restart the animation loop
                } else {
                    modeButton.setText("Pasar a modo automatico");
                }
            }
        });

        // Speed control buttons
        TextButton speed1xButton = new TextButton("1x", skin);
        speed1xButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                speedMultiplier = 1f;
                System.out.println("Speed set to 1x");
            }
        });

        TextButton speed2xButton = new TextButton("2x", skin);
        speed2xButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                speedMultiplier = 2f;
                System.out.println("Speed set to 2x");
            }
        });

        TextButton speed3xButton = new TextButton("3x", skin);
        speed3xButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                speedMultiplier = 3f;
                System.out.println("Speed set to 3x");
            }
        });

        Table controlTable = new Table();
        controlTable.top().left();
        controlTable.setFillParent(true);
        controlTable.add(speed1xButton).size(Constants.Buttons.BUTTON_WIDTH, Constants.Buttons.BUTTON_HEIGHT).pad(Constants.Buttons.PADDING).row();
        controlTable.add(speed2xButton).size(Constants.Buttons.BUTTON_WIDTH, Constants.Buttons.BUTTON_HEIGHT).pad(Constants.Buttons.PADDING).row();
        controlTable.add(speed3xButton).size(Constants.Buttons.BUTTON_WIDTH, Constants.Buttons.BUTTON_HEIGHT).pad(Constants.Buttons.PADDING).row();

        Table table = new Table();
        table.setFillParent(true);
        table.bottom();
        table.add(slider).width(Constants.Buttons.SLIDER_WIDTH).padBottom(Constants.Buttons.PADDING);
        table.row();
        table.add(modeButton).padBottom(Constants.Buttons.PADDING);

        stage.addActor(controlTable);
        stage.addActor(table);

        // Set up the camera
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Initialize font
        font = new BitmapFont();
    }

    private void initializeAnimations() {
        String atlasPath = Constants.KnifePlanet2.ATLAS;
        String skeletonPath = Constants.KnifePlanet2.JSON;

        skeleton = spineAnimationHandler.createSkeleton(atlasPath, skeletonPath);
        state = spineAnimationHandler.createAnimationState(skeleton);
        skeleton.setScale(4f, 4f);

        setSkeletonPosition();

        state.setAnimation(0, "animation", true);  // Loop the animation by default

        state.addListener(new AnimationState.AnimationStateAdapter() {

            @Override
            public void start(AnimationState.TrackEntry entry) {
                System.out.println("Animation started");
                animationStarted = true;
            }

            @Override
            public void interrupt(AnimationState.TrackEntry entry) {
                System.out.println("Animation interrupted");
                animationStarted = false;
            }

            @Override
            public void event(AnimationState.TrackEntry entry, com.esotericsoftware.spine.Event event) {
                if ("splatter".equals(event.getData().getName()) && !inEventTriggered) {
                    inEventCounter++;
                    inEventTriggered = true;  // Mark event as triggered
                    System.out.println("Event 'splatter' triggered! Counter: " + inEventCounter);
                }
            }

            @Override
            public void complete(AnimationState.TrackEntry entry) {
                // Reset the event trigger flag when the animation completes a loop
                System.out.println("Animation completed");
                inEventTriggered = false;
                animationStarted = false;
            }

            @Override
            public void end(AnimationState.TrackEntry entry) {
                // This will be called at the end of the animation loop
                System.out.println("Animation ended, starting new loop");
                inEventTriggered = false;
                animationStarted = true; // Ensure the start message for the new loop
            }
        });
    }

    @Override
    public void render(float delta) {

        super.render(delta);

        if (isLooping) {
            state.update(delta * speedMultiplier);
        }

        state.apply(skeleton);
        skeleton.updateWorldTransform();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        renderer.draw(batch, skeleton);
        // Render the counter in the top right corner
        font.draw(batch, "Counter: " + inEventCounter, Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 10);
        batch.end();

        super.renderTrail(delta);
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
        float centerX = camera.viewportWidth / 2 - skeleton.getData().getWidth() / 2;
        float centerY = camera.viewportHeight / 2 - skeleton.getData().getHeight() / 2;
        skeleton.setPosition(centerX, centerY);
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        font.dispose();
    }
}
