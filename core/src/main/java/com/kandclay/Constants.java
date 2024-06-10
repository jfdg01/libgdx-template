package com.kandclay;

public class Constants {
    public static class Animation {
        public static final int NUM_COLS = 28; // Number of columns in the sprite sheet
        public static final int NUM_ROWS = 28; // Number of rows in the sprite sheet
        public static final float FRAME_DURATION = 5f / 100f; // Duration of each frame
    }

    public static class Game {
        public static final float ROTATION_SPEED = 1f / 20f; // Speed factor for rotation
        public static final float LERP_FACTOR = 0.1f; // Factor for interpolation
    }

    public static class Buttons {
        public static final int BUTTON_WIDTH = 100;
        public static final int BUTTON_HEIGHT = 80;
        public static final int PADDING = 5;
        public static final int SLIDER_WIDTH = 300;
    }
}


