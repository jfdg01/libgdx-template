package com.kandclay;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class MyAssetManager implements IAssetManager {
    private final AssetManager assetManager;

    public MyAssetManager() {
        assetManager = new AssetManager();
        loadAssets();
    }

    @Override
    public void loadAssets() {
        // Load skins
        // assetManager.load("skin/default/raw/pack.json", Skin.class);

        // Load textures
        assetManager.load("sprites/anim/earth.png", Texture.class);
        assetManager.load("sprites/anim/jupiter.png", Texture.class);
        assetManager.load("sprites/anim/mars.png", Texture.class);
        assetManager.load("sprites/anim/mercury.png", Texture.class);
        assetManager.load("sprites/anim/moon.png", Texture.class);
        assetManager.load("sprites/anim/neptune.png", Texture.class);
        assetManager.load("sprites/anim/saturn.png", Texture.class);
        assetManager.load("sprites/anim/sun.png", Texture.class);
        assetManager.load("sprites/anim/uranus.png", Texture.class);
        assetManager.load("sprites/anim/venus.png", Texture.class);

        // Load atlases
        assetManager.load("sprites/atlas/solarSystemAssets.atlas", TextureAtlas.class);
    }

    public void finishLoading() {
        assetManager.finishLoading();
    }

    @Override
    public <T> T get(String fileName, Class<T> type) {
        return assetManager.get(fileName, type);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}

