package com.kandclay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.kandclay.handlers.SpriteSheetAnimationHandler;
import com.kandclay.handlers.SpineAnimationHandler;
import com.kandclay.managers.AudioManager;
import com.kandclay.managers.ConfigurationManager;
import com.kandclay.managers.MyAssetManager;
import com.kandclay.managers.ScreenManager;
import com.kandclay.utils.Constants;
import com.kandclay.utils.Constants.ScreenType;


public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private MyAssetManager assetManager;
    private AudioManager audioManager;
    private ConfigurationManager configManager;
    private ScreenManager screenManager;
    private SpriteSheetAnimationHandler spriteSheetAnimationHandler;
    private SpineAnimationHandler spineAnimationHandler;

    @Override
    public void create() {
        configManager = ConfigurationManager.getInstance();
        assetManager = MyAssetManager.getInstance();
        audioManager = AudioManager.getInstance();
        screenManager = new ScreenManager();
        spriteSheetAnimationHandler = new SpriteSheetAnimationHandler();
        spineAnimationHandler = new SpineAnimationHandler();

        loadInitialAssets();


        screenManager.setScreen(ScreenType.MENU);
    }

    private void loadInitialAssets() {
        assetManager.load(Constants.Skin.JSON, Skin.class);
        assetManager.load(Constants.MainMenu.ATLAS, TextureAtlas.class);
        assetManager.load(Constants.CursorTrail.ATLAS, TextureAtlas.class);
        assetManager.load(Constants.Coin.Yellow.ATLAS, TextureAtlas.class);
        assetManager.load(Constants.Coin.Red.ATLAS, TextureAtlas.class);

        assetManager.finishLoading();
        addFontsToSkin();
    }

    private void addFontsToSkin() {
        Skin skin = assetManager.get(Constants.Skin.JSON, Skin.class);
        BitmapFont snacktimeFont = generateFont(Constants.Font.PATH, 24);
        skin.add("snacktime-font", snacktimeFont, BitmapFont.class);

        Label.LabelStyle snacktimeLabelStyle = new Label.LabelStyle();
        snacktimeLabelStyle.font = snacktimeFont;
        skin.add("snacktime-label", snacktimeLabelStyle);

        // Add ButtonStyle
        TextButton.TextButtonStyle snacktimeButtonStyle = new TextButton.TextButtonStyle();
        snacktimeButtonStyle.font = snacktimeFont;
        snacktimeButtonStyle.up = skin.getDrawable("default-rect");
        snacktimeButtonStyle.down = skin.getDrawable("default-rect-down");
        snacktimeButtonStyle.checked = skin.getDrawable("default-rect");
        skin.add("snacktime-button", snacktimeButtonStyle);
    }

    private BitmapFont generateFont(String fontFile, int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontFile));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose(); // Dispose the generator to free up resources
        return font;
    }

    @Override
    public void render() {
        screenManager.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize(int width, int height) {
        screenManager.resize(width, height);
    }

    @Override
    public void pause() {
        screenManager.pause();
    }

    @Override
    public void resume() {
        screenManager.resume();
    }

    @Override
    public void dispose() {
        if (screenManager != null) {
            screenManager.dispose();
        }
        if (assetManager != null) {
            assetManager.dispose();
        }
        if (audioManager != null) {
            audioManager.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }
    }
}

