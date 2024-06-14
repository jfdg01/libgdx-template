package com.kandclay.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kandclay.handlers.SpineAnimationHandler;
import com.kandclay.managers.AudioManager;
import com.kandclay.managers.MyAssetManager;
import com.kandclay.managers.ScreenManager;
import com.kandclay.utils.Constants;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class CounterScreen extends BaseScreen {
    private TextField intervalField;
    private TextField maxTimeField;
    private Label timerLabel;
    private float intervalTime;
    private float maxTime;
    private float elapsedTime;
    private float nextInterval;
    private boolean running;

    private BitmapFont largeFont;

    public CounterScreen(MyAssetManager assetManager, AudioManager audioManager, SpineAnimationHandler spineAnimationHandler, ScreenManager screenManager) {
        super(assetManager, audioManager, spineAnimationHandler, screenManager);
    }

    @Override
    public void show() {
        super.show();

        Skin skin = assetManager.get(Constants.Skin.JSON, Skin.class);

        // Generate a high-resolution font using FreeTypeFontGenerator
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/my-font.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 48; // Set the desired font size
        largeFont = generator.generateFont(parameter);
        generator.dispose(); // Dispose the generator to avoid memory leaks

        // Create input fields and labels
        intervalField = new TextField("", skin);
        intervalField.setMessageText("Interval Time (seconds)");
        intervalField.getStyle().font = largeFont;
        intervalField.setHeight(largeFont.getLineHeight() + 20); // Adjust height to fit the font size

        maxTimeField = new TextField("", skin);
        maxTimeField.setMessageText("Max Time (seconds)");
        maxTimeField.getStyle().font = largeFont;
        maxTimeField.setHeight(largeFont.getLineHeight() + 20); // Adjust height to fit the font size

        timerLabel = new Label("0:00", new Label.LabelStyle(largeFont, Color.WHITE));

        TextButton startButton = new TextButton("Start", skin);
        startButton.getLabel().setStyle(new Label.LabelStyle(largeFont, Color.WHITE));
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startCounter();
            }
        });

        // Arrange UI elements in a table
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(new Label("Interval:", new Label.LabelStyle(largeFont, Color.WHITE))).pad(20);
        table.add(intervalField).width(400).height(intervalField.getHeight()).pad(20);
        table.row();
        table.add(new Label("Max Time:", new Label.LabelStyle(largeFont, Color.WHITE))).pad(20);
        table.add(maxTimeField).width(400).height(maxTimeField.getHeight()).pad(20);
        table.row();
        table.add(startButton).colspan(2).pad(20);
        table.row();
        table.add(timerLabel).colspan(2).pad(20);

        stage.addActor(table);
    }

    private void startCounter() {
        try {
            intervalTime = Float.parseFloat(intervalField.getText());
            maxTime = Float.parseFloat(maxTimeField.getText());
            elapsedTime = 0;
            nextInterval = intervalTime;
            running = true;
        } catch (NumberFormatException e) {
            // Handle invalid input
            Gdx.app.log("CounterScreen", "Invalid input for interval or max time");
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (running) {
            elapsedTime += delta;
            if (elapsedTime >= nextInterval) {
                audioManager.playSound(Constants.Sounds.OOF);
                nextInterval += intervalTime;
            }
            if (elapsedTime >= maxTime) {
                running = false;
            }
            updateTimerLabel();
        }

        stage.act(delta);
        stage.draw();
    }

    private void updateTimerLabel() {
        int minutes = (int) (elapsedTime / 60);
        int seconds = (int) (elapsedTime % 60);
        timerLabel.setText(String.format("%d:%02d", minutes, seconds));
    }

    @Override
    public void dispose() {
        super.dispose();
        if (largeFont != null) {
            largeFont.dispose();
        }
    }
}
