package com.mygdx.puigbros;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AmmoBox extends Actor {
    private static final int MIN_AMMO = 2;
    private static final int MAX_AMMO = 5;
    private Texture texture;
    private TileMap map;
    private float scrollOffsetX;


    public AmmoBox(float x, float y, AssetManager manager){
        setBounds(x, y, 32, 32);
        this.texture = manager.get("bullets/bullet (2).png");
        this.scrollOffsetX = 0f;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(
            texture,
            getX() - getWidth() / 2 - scrollOffsetX,
            getY() - getHeight() / 2,
            getWidth(), getHeight()
        );
    }

    public int getAmmoCount(){
        return MIN_AMMO + (int) (Math.random() * (MAX_AMMO - MIN_AMMO + 1));
    }
    public void setMap(TileMap map) {
        this.map = map;
    }
    public void updateScrollOffset() {
        if (map != null) {
            scrollOffsetX = map.scrollX;
        }
    }
}
