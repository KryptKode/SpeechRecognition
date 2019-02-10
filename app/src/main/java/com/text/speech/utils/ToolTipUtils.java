package com.text.speech.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.text.speech.BuildConfig;
import com.text.speech.R;
import com.text.speech.views.tooltip.Tooltip;
import com.text.speech.views.tooltip.TooltipAnimation;

import androidx.core.content.ContextCompat;

public class ToolTipUtils {

    public static final int ANIMATION_DURATION_MS = 500;
    public static final int AUTO_CANCEL_DURATION = 3000;


    public void showToolTip(Context context, View anchor, ViewGroup rootView, String text) {
        ViewGroup content = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.tool_tip_layout, null);
        TextView textView = (TextView) content.findViewById(R.id.tv_text);
        textView.setText(text);
        int dimen = context.getResources().getDimensionPixelOffset(R.dimen.tip_dimen_small);
        int color = ContextCompat.getColor(context, R.color.colorAccent);
        new Tooltip.Builder(context)
                .anchor(anchor, Tooltip.BOTTOM)
                .animate(new TooltipAnimation(TooltipAnimation.REVEAL, ANIMATION_DURATION_MS))
                .autoAdjust(true)
                .autoCancel(AUTO_CANCEL_DURATION)
                .content(content)
                .withPadding(context.getResources().getDimensionPixelOffset(R.dimen.menu_tooltip_padding))
                .withTip(new Tooltip.Tip(dimen, dimen, color))
                .into(rootView)
                .debug(BuildConfig.DEBUG)
                .show();
    }


    public void showToolTipAbove(Context context, View anchor, ViewGroup rootView, String text) {
        ViewGroup content = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.tool_tip_layout, null);
        TextView textView = (TextView) content.findViewById(R.id.tv_text);
        textView.setText(text);
        int dimen = context.getResources().getDimensionPixelOffset(R.dimen.tip_dimen_small);
        int color = ContextCompat.getColor(context, R.color.colorAccent);
        new Tooltip.Builder(context)
                .anchor(anchor, Tooltip.TOP)
                .animate(new TooltipAnimation(TooltipAnimation.REVEAL, ANIMATION_DURATION_MS))
                .autoAdjust(true)
                .autoCancel(AUTO_CANCEL_DURATION)
                .content(content)
                .withPadding(context.getResources().getDimensionPixelOffset(R.dimen.menu_tooltip_padding))
                .withTip(new Tooltip.Tip(dimen, dimen, color))
                .into(rootView)
                .debug(BuildConfig.DEBUG)
                .show();
    }






}
