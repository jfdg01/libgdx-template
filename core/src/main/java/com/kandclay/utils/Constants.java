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

    public static class KnifePlanet {
        public static final String ATLAS = "spine/knifePlanet.atlas";
        public static final String JSON = "spine/knifePlanet.json";
        public static final String PNG = "spine/knifePlanet.png";
    }

    public static class KnifePlanet2 {
        public static final String ATLAS = "spine/knifePlanet2.atlas";
        public static final String JSON = "spine/knifePlanet2.json";
        public static final String PNG = "spine/knifePlanet2.png";
    }

    public static class Spinosaurus {
        public static final String ATLAS = "spine/spinosaurus-ess.atlas";
        public static final String JSON = "spine/spinosaurus-ess.json";
    }

    public static class Trail {
        public static final String ATLAS = "spine/explosion.atlas";
        public static final String JSON = "spine/explosion.json";
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
        KNIFE1,
        KNIFE2,
        OPTIONS,
        COUNTER,
        SPINOSAURUS;
    }

    public enum GameState {
        RUNNING,
        PAUSED,
        GAME_OVER;
    }

    public static class Audio {
        public static final float DEFAULT_VOLUME = 1f;
    }
}


