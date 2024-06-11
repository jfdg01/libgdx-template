package com.kandclay.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.kandclay.actors.SpineAnimatedActor;
import com.kandclay.handlers.SpriteSheetAnimationHandler;
import com.kandclay.handlers.SpineAnimationHandler;
import com.kandclay.managers.AudioManager;
import com.kandclay.managers.MyAssetManager;
import com.kandclay.utils.Constants;
import com.kandclay.utils.GameState;

public class GameScreen extends BaseScreen {
    private GameState gameState;
    private final Skin skin;
    private final SpriteSheetAnimationHandler spriteSheetAnimationHandler;
    private final SpineAnimationHandler spineAnimationHandler;
    private SnapshotArray<SpineAnimatedActor> spineActors;
    private float elapsedTime;

    // Additional fields from SpineExample
    private Slider slider;
    private TextButton modeButton;
    private BitmapFont font;
    private boolean isLooping = true; // Set initial state to automatic (looping)
    private int inEventCounter = 0;
    private boolean inEventTriggered = false;
    private float speedMultiplier = 1f;

    public GameScreen(MyAssetManager assetManager, AudioManager audioManager, SpriteSheetAnimationHandler spriteSheetAnimationHandler, SpineAnimationHandler spineAnimationHandler) {
        super(assetManager, audioManager);
        gameState = GameState.RUNNING;
        this.spriteSheetAnimationHandler = spriteSheetAnimationHandler;
        this.spineAnimationHandler = spineAnimationHandler;
        this.skin = assetManager.get("skin/default/skin/uiskin.json", Skin.class);
        initializeStage();
        initializeAnimations();
        initializeUI();
    }

    private void initializeStage() {
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    private void initializeAnimations() {
        spineActors = new SnapshotArray<>();
        String[] spineFiles = {
            "spine/skeleton.atlas", "spine/skeleton.json"
        };

        Skeleton skeleton = spineAnimationHandler.createSkeleton(spineFiles[0], spineFiles[1]);
        AnimationState state = spineAnimationHandler.createAnimationState(skeleton);
        SpineAnimatedActor spineActor = new SpineAnimatedActor(skeleton, state);
        spineActor.setAnimation("animation", true);

        // Add event listener
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

        spineActors.add(spineActor);
        stage.addActor(spineActor);
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(stage);
    }

    private void initializeUI() {
        // Initialize skin and font
        font = new BitmapFont();

        // Slider
        slider = new Slider(0, 1, 0.01f, false, skin);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!isLooping) {
                    float progress = slider.getValue();
                    float animationDuration = spineActors.get(0).getAnimationState().getCurrent(0).getAnimation().getDuration();
                    spineActors.get(0).getAnimationState().getCurrent(0).setTrackTime(progress * animationDuration);

                    // Reset the event trigger flag if the slider is moved back past the trigger point
                    if (progress < 0.5f) {
                        inEventTriggered = false;
                    }
                }
            }
        });

        // Mode button
        modeButton = new TextButton("Pasar a modo manual", skin); // Set initial button text to switch to manual
        modeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isLooping = !isLooping;
                if (isLooping) {
                    modeButton.setText("Pasar a modo manual");
                    spineActors.get(0).getAnimationState().setAnimation(0, "animation", true);  // Restart the animation loop
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

        stage.act(delta);
        stage.draw();

        // Render the counter in the top right corner
        stage.getBatch().begin();
        font.draw(stage.getBatch(), "Counter: " + inEventCounter, Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 10);
        stage.getBatch().end();

        super.render(delta);
    }

    private void updateGame(float delta) {
        // Update the animation state based on mode and speed multiplier
        if (isLooping) {
            spineActors.get(0).getAnimationState().update(delta * speedMultiplier);
        }

        // Apply the animation state to the skeleton
        for (SpineAnimatedActor actor : spineActors) {
            actor.getAnimationState().apply(actor.getSkeleton());
            actor.getSkeleton().updateWorldTransform();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
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

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        font.dispose();
    }
}

