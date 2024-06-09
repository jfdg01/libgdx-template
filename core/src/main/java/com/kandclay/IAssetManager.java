package com.kandclay;

public interface IAssetManager {
    <T> T get(String fileName, Class<T> type);
    void loadAssets();
    void dispose();
}
