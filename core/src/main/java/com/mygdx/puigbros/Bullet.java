package com.mygdx.puigbros;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Bullet extends Actor {
    private final float SPEED = 200;
    private final boolean isMovingRight;
    private AssetManager manager;
    private TileMap map;

    public Bullet(float x, float y, boolean movingRight, AssetManager manager,
                  TileMap map){
        setPosition(x, y);
        setBounds(getX(), getY(), 32, 32);
        this.isMovingRight = movingRight;
        this.manager = manager;
        this.map = map;
    }

    @Override
    public void act(float delta){
        super.act(delta);

        if (isMovingRight) {
            setX(getX() + SPEED * delta);
        } else {
            setX(getX() - SPEED * delta);
        }

        if (getX() < 0 || getX() > Gdx.graphics.getWidth()){
            remove();
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.draw(
            manager.get("bullets/bullet (1).png", Texture.class),
            getX() - 64f- map.scrollX, getY() - 64f, 32, 32,
            0, 0, 200, 196, isMovingRight, true
        );
    }

    public float getWorldX(){
        return getX();
    }

    public float getWorldY(){
        return getY();
    }
}
