package com.text.speech.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;


import com.text.speech.R;

import androidx.annotation.RestrictTo;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.TextViewCompat;


/**
 * A custom TextView that supports using vector drawables with the {@code
 * android:drawable[Start/End/Top/Bottom]} attribute pre-L.
 * <p>
 * AppCompat can only load vector drawables with srcCompat pre-L and doesn't provide a similar
 * compatibility attribute for compound drawables. Thus, we must load compound drawables at runtime
 * using AppCompat and inject them into the button to support pre-L devices.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class SupportVectorDrawableTextView extends AppCompatTextView {
    public SupportVectorDrawableTextView(Context context) {
        super(context);
    }

    public SupportVectorDrawableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSupportVectorDrawablesAttrs(attrs);
    }

    public SupportVectorDrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSupportVectorDrawablesAttrs(attrs);
    }

    /**
     * Loads the compound drawables natively on L+ devices and using AppCompat pre-L.
     * <p>
     * <i>Note:</i> If we ever need a TextView with compound drawables, this same technique is
     * applicable.
     */
    private void initSupportVectorDrawablesAttrs(AttributeSet attrs) {
        if (attrs == null) { return; }

        TypedArray attributeArray = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.SupportVectorDrawableTextView);

        Drawable drawableStart = null;
        Drawable drawableEnd = null;
        Drawable drawableTop = null;
        Drawable drawableBottom = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawableStart = attributeArray.getDrawable(
                    R.styleable.SupportVectorDrawableTextView_tv_DrawableStartCompat);
            drawableEnd = attributeArray.getDrawable(
                    R.styleable.SupportVectorDrawableTextView_tv_DrawableEndCompat);
            drawableTop = attributeArray.getDrawable(
                    R.styleable.SupportVectorDrawableTextView_tv_DrawableTopCompat);
            drawableBottom = attributeArray.getDrawable(
                    R.styleable.SupportVectorDrawableTextView_tv_DrawableBottomCompat);
        } else {
            int drawableStartId = attributeArray.getResourceId(
                    R.styleable.SupportVectorDrawableTextView_tv_DrawableStartCompat, -1);
            int drawableEndId = attributeArray.getResourceId(
                    R.styleable.SupportVectorDrawableTextView_tv_DrawableEndCompat, -1);
            int drawableTopId = attributeArray.getResourceId(
                    R.styleable.SupportVectorDrawableTextView_tv_DrawableTopCompat, -1);
            int drawableBottomId = attributeArray.getResourceId(
                    R.styleable.SupportVectorDrawableTextView_tv_DrawableBottomCompat, -1);

            if (drawableStartId != -1) {
                drawableStart = AppCompatResources.getDrawable(getContext(), drawableStartId);
            }
            if (drawableEndId != -1) {
                drawableEnd = AppCompatResources.getDrawable(getContext(), drawableEndId);
            }
            if (drawableTopId != -1) {
                drawableTop = AppCompatResources.getDrawable(getContext(), drawableTopId);
            }
            if (drawableBottomId != -1) {
                drawableBottom = AppCompatResources.getDrawable(getContext(), drawableBottomId);
            }
        }

        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(
                this, drawableStart, drawableTop, drawableEnd, drawableBottom);

        attributeArray.recycle();
    }
}
