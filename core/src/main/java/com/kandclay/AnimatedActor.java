package com.kandclay;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AnimatedActor extends Actor {
    private Animation<TextureRegion> animation;
    private float stateTime;

    public AnimatedActor(Animation<TextureRegion> animation) {
        this.animation = animation;
        this.stateTime = 0f;
        TextureRegion initialFrame = animation.getKeyFrame(0);
        setBounds(getX(), getY(), initialFrame.getRegionWidth(), initialFrame.getRegionHeight());
    }

    public void setAnimation(Animation<TextureRegion> newAnimation) {
        this.animation = newAnimation;
        this.stateTime = 0f; // Reset the state time to start the new animation from the beginning
        TextureRegion initialFrame = newAnimation.getKeyFrame(0);
        setBounds(getX(), getY(), initialFrame.getRegionWidth(), initialFrame.getRegionHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
    }
}

