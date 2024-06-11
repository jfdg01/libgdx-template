package com.kandclay.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.esotericsoftware.spine.*;

import com.kandclay.handlers.SpineAnimationHandler;
import com.kandclay.utils.Constants;
import com.kandclay.managers.*;

public class SpineKnife1Screen extends BaseScreen {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private SkeletonRenderer renderer;
    private SkeletonRendererDebug debugRenderer;
    private SpineAnimationHandler spineAnimationHandler;

    private Skeleton skeleton;
    private AnimationState animationState;

    private BitmapFont font;

    private int inEventCounter = 0;
    private float speedMultiplier = 1f;

    public SpineKnife1Screen(MyAssetManager assetManager, AudioManager audioManager, SpineAnimationHandler spineAnimationHandler) {
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
        debugRenderer = new SkeletonRendererDebug();

        initializeAnimations();

        // Set up the UI
        Gdx.input.setInputProcessor(stage);

        Skin skin = assetManager.get(Constants.Skin.JSON, Skin.class);

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

        stage.addActor(controlTable);
        stage.addActor(table);

        // Set up the camera
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Initialize font
        font = new BitmapFont();
    }

    private void initializeAnimations() {
        String atlasPath = Constants.KnifePlanet.ATLAS;
        String skeletonPath = Constants.KnifePlanet.JSON;

        skeleton = spineAnimationHandler.createSkeleton(atlasPath, skeletonPath);
        animationState = spineAnimationHandler.createAnimationState(skeleton);
        skeleton.setScale(4f, 4f);

        setSkeletonPosition();

        animationState.setAnimation(0, "animation", true);  // Loop the animation by default

        animationState.addListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void event(AnimationState.TrackEntry entry, com.esotericsoftware.spine.Event event) {
                if ("in".equals(event.getData().getName())) {
                    inEventCounter++;
                    audioManager.playSound(Constants.Sounds.OOF);
                    System.out.println("Event 'in' triggered! Counter: " + inEventCounter);
                }
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        animationState.update(delta * speedMultiplier);

        animationState.apply(skeleton);
        skeleton.updateWorldTransform();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        renderer.draw(batch, skeleton);
        // Render the counter in the top right corner
        font.draw(batch, "Counter: " + inEventCounter, Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 10);
        batch.end();

        super.render(delta);
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
