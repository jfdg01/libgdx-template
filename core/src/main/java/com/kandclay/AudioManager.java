package com.kandclay;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class AudioManager implements Disposable {
    private static AudioManager instance;

    private Map<String, Sound> soundMap;
    private Map<String, Music> musicMap;
    private Music currentMusic;

    // Private constructor to prevent instantiation
    private AudioManager() {
        soundMap = new HashMap<>();
        musicMap = new HashMap<>();
    }

    // Thread-safe method to get the singleton instance
    public static synchronized AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    // Load a sound file
    public void loadSound(String fileName) {
        if (!soundMap.containsKey(fileName)) {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(fileName));
            soundMap.put(fileName, sound);
        }
    }

    // Play a sound file
    public void playSound(String fileName) {
        Sound sound = soundMap.get(fileName);
        if (sound != null) {
            sound.play();
        } else {
            throw new RuntimeException("Sound not loaded: " + fileName);
        }
    }

    // Stop a sound file
    public void stopSound(String fileName) {
        Sound sound = soundMap.get(fileName);
        if (sound != null) {
            sound.stop();
        } else {
            throw new RuntimeException("Sound not loaded: " + fileName);
        }
    }

    // Load a music file
    public void loadMusic(String fileName) {
        if (!musicMap.containsKey(fileName)) {
            Music music = Gdx.audio.newMusic(Gdx.files.internal(fileName));
            musicMap.put(fileName, music);
        }
    }

    // Play a music file
    public void playMusic(String fileName, boolean looping) {
        if (currentMusic != null) {
            currentMusic.stop();
        }
        Music music = musicMap.get(fileName);
        if (music != null) {
            music.setLooping(looping);
            music.play();
            currentMusic = music;
        } else {
            throw new RuntimeException("Music not loaded: " + fileName);
        }
    }

    // Stop the current music
    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic = null;
        }
    }

    // Dispose of all sounds and music
    @Override
    public void dispose() {
        for (Sound sound : soundMap.values()) {
            sound.dispose();
        }
        soundMap.clear();

        for (Music music : musicMap.values()) {
            music.dispose();
        }
        musicMap.clear();

        if (currentMusic != null) {
            currentMusic.dispose();
            currentMusic = null;
        }
    }
}
