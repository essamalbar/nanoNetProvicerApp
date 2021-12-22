package com.crazyiter.nanonetprovicerapp;

public class CustomMenuItem {
    private final String title;
    private final int imageRes;
    private int colorTint;

    public CustomMenuItem(String title, int imageRes) {
        this.title = title;
        this.imageRes = imageRes;
        this.colorTint = R.color.colorPrimary;
    }

    public CustomMenuItem(String title, int imageRes, int colorTint) {
        this(title, imageRes);
        this.colorTint = colorTint;
    }

    public String getTitle() {
        return title;
    }

    public int getImageRes() {
        return imageRes;
    }

    public int getColorTint() {
        return colorTint;
    }
}
