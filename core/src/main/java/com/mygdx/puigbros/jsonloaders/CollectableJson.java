package com.mygdx.puigbros.jsonloaders;

public class CollectableJson
{
    private int x;
    private int y;
    private String type;
    private int[] count;

    public CollectableJson()
    {

    }

    public int getX() { return x; }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int[] getCount() {
        return count;
    }

    public void setCount(int[] count) {
        this.count = count;
    }
}
