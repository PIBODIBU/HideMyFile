package com.android.hidemyfile.Support.Graphic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

public class VectorDrawableSupport {
    public static Drawable getDrawableFromVector(Context context, @DrawableRes int imageId) {
        return DrawableCompat.wrap(ContextCompat.getDrawable(context, imageId));
    }
}
