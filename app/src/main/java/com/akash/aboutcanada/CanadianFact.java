package com.akash.aboutcanada;

import android.graphics.drawable.Drawable;

/**
 * Created by akash on 8/03/18.
 */

// creating data model that can be used for array list to transmit between adapter and activities
public class CanadianFact {
    String title, Description;
    Drawable image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
