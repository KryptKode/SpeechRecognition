package com.text.speech.utils;

/**
 * Created by Cyberman on 12/22/2017.
 */




import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.text.speech.R;


public class ToolTipHelper {
    /**
     * The estimated height of a toast, in dips (density-independent pixels). This is used to
     * determine whether or not the toast should appear above or below the UI element.
     */
    private static final int ESTIMATED_TOAST_HEIGHT_DIPS = 48;

    /**
     * Sets up a cheat sheet (tooltip) for the given view by setting its {@link
     * View.OnLongClickListener}. When the view is long-pressed, a {@link Toast} with
     * the view's {@link View#getContentDescription() content description} will be
     * shown either above (default) or below the view (if there isn't room above it).
     *
     * @param view The view to add a cheat sheet for.
     */
    public static void setup(View view) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view1) {
                showCheatSheet(view1, view1.getContentDescription());
                return true;
            }
        });
    }

    /**
     * Sets up a cheat sheet (tooltip) for the given view by setting its {@link
     * View.OnLongClickListener}. When the view is long-pressed, a {@link Toast} with
     * the given text will be shown either above (default) or below the view (if there isn't room
     * above it).
     *
     * @param view      The view to add a cheat sheet for.
     * @param textResId The string resource containing the text to show on long-press.
     */
    public static void setup(View view, final int textResId) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return showCheatSheet(view, view.getContext().getString(textResId));
            }
        });
    }

    public static void setupOnClick(View view, final int textResId) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCheatSheet(view, view.getContext().getString(textResId));
            }
        });
    }

    /**
     * Sets up a cheat sheet (tooltip) for the given view by setting its {@link
     * View.OnLongClickListener}. When the view is long-pressed, a {@link Toast} with
     * the given text will be shown either above (default) or below the view (if there isn't room
     * above it).
     *
     * @param view The view to add a cheat sheet for.
     * @param text The text to show on long-press.
     */
    public static void setup(View view, final CharSequence text) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return showCheatSheet(view, text);
            }
        });
    }

    /**
     * Removes the cheat sheet for the given view by removing the view's {@link
     * View.OnLongClickListener}.
     *
     * @param view The view whose cheat sheet should be removed.
     */
    public static void remove(final View view) {
        view.setOnLongClickListener(null);
    }

    /**
     * Internal helper method to show the cheat sheet toast.
     */
    public static boolean showCheatSheet(View view, CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return false;
        }

        final int[] screenPos = new int[2]; // origin is device display
        final Rect displayFrame = new Rect(); // includes decorations (e.g. status bar)
        view.getLocationOnScreen(screenPos);
        view.getWindowVisibleDisplayFrame(displayFrame);

        final Context context = view.getContext();
        final int viewWidth = view.getWidth();
        final int viewHeight = view.getHeight();
        final int viewCenterX = screenPos[0] + viewWidth / 2;
        final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        final int estimatedToastHeight = (int) (ESTIMATED_TOAST_HEIGHT_DIPS
                * context.getResources().getDisplayMetrics().density);
        View layout = LayoutInflater.from(context).inflate(R.layout.tool_tip_layout, null);
        ((TextView)layout.findViewById(R.id.tv_text)).setText(text);

        Toast cheatSheet = new Toast(context);
        cheatSheet.setGravity(Gravity.CENTER, 0, 0);
        cheatSheet.setDuration(Toast.LENGTH_LONG);
        cheatSheet.setView(layout);


        // Show above
        // Offsets are after decorations (e.g. status bar) are factored in
        // NOTE: We can't use Gravity.BOTTOM because when the keyboard is up
        // its height isn't factored in.
        cheatSheet.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
                viewCenterX - screenWidth / 2,
                screenPos[1] - displayFrame.top - estimatedToastHeight);
        cheatSheet.show();
        return true;
    }
}