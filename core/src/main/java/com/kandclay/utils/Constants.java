package com.kandclay.utils;

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

    public static class Audio {
        public static final float DEFAULT_VOLUME = 1f;
    }

    public static class Cursor {
        public static final int HOTSPOT_X = 0;
        public static final int HOTSPOT_Y = 0;
    }

    public static class TrailDot {
        public static final int NUMBER_OF_COLORS = 360;
        public static final float SATURATION = 1.0f;
        public static final float VALUE = 1.0f;
        public static final float ALPHA = 0.5f;
        public static final float MIN_SCALE = 0.2f;
        public static final float MAX_SCALE = 0.5f;
        public static final int MIN_ROTATION = 0;
        public static final int MAX_ROTATION = 359;
    }

        public static class MainAnimation {
        public static final String ATLAS = "spine/main.atlas";
        public static final String JSON = "spine/main.json";
    }

    public static class MainMenu {
        public static final String ATLAS = "spine/menu.atlas";
        public static final String JSON = "spine/menu.json";
    }

    public static class CursorTrail {
        public static final String ATLAS = "spine/trailDot.atlas";
        public static final String JSON = "spine/trailDot.json";
    }

    public static class Skin {
        public static final String JSON = "skin/default/skin/uiskin.json";
    }

    public static class Sounds {
        public static final String OOF = "sounds/sound.ogg";
    }

    public static class Counter {
        public static class Button {
            public static final int WIDTH = 800;
            public static final int HEIGHT = 400;
            public static final int PADDING = 10;
        }
    }

    public enum ScreenType {
        MENU,
        GAME,
        OPTIONS,
    }

    public enum GameState {
        RUNNING,
        PAUSED,
        GAME_OVER;
    }

    public static enum HairColor {
        BLONDE, BRUNETTE, REDHEAD, BLACK, BROWN;

        private static HairColor[] vals = values();
        public HairColor next() {
            return vals[(this.ordinal() + 1) % vals.length];
        }
    }

    public static enum Height {
        SHORT, AVERAGE, TALL;

        private static Height[] vals = values();
        public Height next() {
            return vals[(this.ordinal() + 1) % vals.length];
        }
    }
}


