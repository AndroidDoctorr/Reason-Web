package com.andrewtorr.reasonweb.Models;

import android.graphics.Bitmap;

/**
 * Created by Andrew on 10/25/2015.
 *
 */
public class CircleItem {
    String text;
    float relSize;
    Bitmap image;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getRelSize() {
        return relSize;
    }

    public void setRelSize(float relSize) {
        this.relSize = relSize;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
