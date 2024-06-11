package com.kandclay.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import static com.kandclay.utils.Constants.Buttons.*;

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
    TextButton modeButton;
    BitmapFont font;

    boolean isLooping = true; // Set initial state to automatic (looping)

    // Counter for the "in" event
    int inEventCounter = 0;

    // Flag to track if "in" event has been triggered in the current cycle
    boolean inEventTriggered = false;

    // Speed multiplier for the animation
    float speedMultiplier = 1f;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        renderer = new SkeletonRenderer();
        renderer.setPremultipliedAlpha(true);
        debugRenderer = new SkeletonRendererDebug();

        atlas = new TextureAtlas(Gdx.files.internal("spine/skeleton.atlas"));
        SkeletonJson json = new SkeletonJson(atlas);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("spine/skeleton.json"));

        skeleton = new Skeleton(skeletonData);
        setSkeletonPosition();
        skeleton.setScale(4f, 4f);

        AnimationStateData stateData = new AnimationStateData(skeletonData);
        state = new AnimationState(stateData);
        state.setAnimation(0, "animation", true);  // Loop the animation by default

        state.addListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void event(AnimationState.TrackEntry entry, com.esotericsoftware.spine.Event event) {
                if ("in".equals(event.getData().getName()) && !inEventTriggered) {
                    inEventCounter++;
                    inEventTriggered = true;  // Mark event as triggered
                    System.out.println("Event 'in' triggered! Counter: " + inEventCounter);
                }
            }
        });

        // Set up the UI
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("skin/default/skin/uiskin.json"));
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
                }
            }
        });

        modeButton = new TextButton("Pasar a modo manual", skin); // Set initial button text to switch to manual
        modeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isLooping = !isLooping;
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
            }
        });

        TextButton speed2xButton = new TextButton("2x", skin);
        speed2xButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                speedMultiplier = 2f;
            }
        });

        TextButton speed3xButton = new TextButton("3x", skin);
        speed3xButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                speedMultiplier = 3f;
            }
        });

        Table controlTable = new Table();
        controlTable.top().left();
        controlTable.setFillParent(true);
        controlTable.add(speed1xButton).size(BUTTON_WIDTH, BUTTON_HEIGHT).pad(PADDING).row();
        controlTable.add(speed2xButton).size(BUTTON_WIDTH, BUTTON_HEIGHT).pad(PADDING).row();
        controlTable.add(speed3xButton).size(BUTTON_WIDTH, BUTTON_HEIGHT).pad(PADDING).row();

        Table table = new Table();
        table.setFillParent(true);
        table.bottom();
        table.add(slider).width(SLIDER_WIDTH).padBottom(PADDING);
        table.row();
        table.add(modeButton).padBottom(PADDING);

        stage.addActor(controlTable);
        stage.addActor(table);

        // Set up the camera
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Initialize font
        font = new BitmapFont();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (isLooping) {
            state.update(Gdx.graphics.getDeltaTime() * speedMultiplier);
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
        font.dispose();
    }
}
