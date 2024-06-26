package com.kandclay.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ConfigurationManager {
    private static ConfigurationManager instance;
    private Preferences preferences;

    // Private constructor to prevent instantiation
    private ConfigurationManager() {
        preferences = Gdx.app.getPreferences("MyGamePreferences");
    }

    // Thread-safe method to get the singleton instance
    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    // Method to get a preference value with a default
    public String getPreference(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public int getPreference(String key, int defaultValue) {
        return preferences.getInteger(key, defaultValue);
    }

    public boolean getPreference(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public float getPreference(String key, float defaultValue) {
        return preferences.getFloat(key, defaultValue);
    }

    // Method to set a preference value
    public void setPreference(String key, String value) {
        preferences.putString(key, value);
        preferences.flush();  // Ensure changes are saved
    }

    public void setPreference(String key, int value) {
        preferences.putInteger(key, value);
        preferences.flush();  // Ensure changes are saved
    }

    public void setPreference(String key, boolean value) {
        preferences.putBoolean(key, value);
        preferences.flush();  // Ensure changes are saved
    }

    public void setPreference(String key, float value) {
        preferences.putFloat(key, value);
        preferences.flush();  // Ensure changes are saved
    }

    // Method to check if a preference exists
    public boolean hasPreference(String key) {
        return preferences.contains(key);
    }

    // Method to remove a preference
    public void removePreference(String key) {
        preferences.remove(key);
        preferences.flush();  // Ensure changes are saved
    }

    // Method to clear all preferences
    public void clearPreferences() {
        preferences.clear();
        preferences.flush();  // Ensure changes are saved
    }

    // Method to handle errors in preferences management
    public void handlePreferenceError(String key, Exception e) {
        System.err.println("Error accessing preference: " + key);
        e.printStackTrace();
    }

    // Example of using the ConfigurationManager
    public static void main(String[] args) {
        ConfigurationManager config = ConfigurationManager.getInstance();

        // Setting preferences
        config.setPreference("volume", 0.8f);
        config.setPreference("musicEnabled", true);

        // Getting preferences
        float volume = config.getPreference("volume", 1.0f);
        boolean musicEnabled = config.getPreference("musicEnabled", false);

        // Checking if a preference exists
        if (config.hasPreference("volume")) {
            System.out.println("Volume preference is set.");
        }

        // Removing a preference
        config.removePreference("musicEnabled");

        // Clearing all preferences
        config.clearPreferences();
    }
}

