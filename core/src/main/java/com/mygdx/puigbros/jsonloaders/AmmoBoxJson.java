package com.mygdx.puigbros.jsonloaders;

public class AmmoBoxJson {
    private float x;
    private float y;

    public AmmoBoxJson(){

    }

    public AmmoBoxJson(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
