package com.mygdx.puigbros;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Bullet extends Actor {
    private final float SPPED = 800;
    private final boolean isMovingRight;

    public Bullet(float x, float y, boolean movingRight){
        setPosition(x, y);
        this.isMovingRight = movingRight;
        setBounds(getX(), getY(), 32, 16);
    }

    @Override
    public void act(float delta){
        super.act(delta);

        if (isMovingRight){
            setX(getX() + SPPED * delta);
        }else {
            setX(getX() - SPPED * delta);
        }

        if (getX() < 0 || getX() > Gdx.graphics.getWidth()){
            remove();
        }
    }
}
