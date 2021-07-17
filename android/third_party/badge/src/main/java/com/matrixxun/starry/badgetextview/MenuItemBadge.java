package com.matrixxun.starry.badgetextview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.applyDimension;

/**
 * @author donghua.xdh
 */
public class MenuItemBadge {

    public static class Builder {

        public int textBackgroundColor; // TRANSPARENT = 0;
        public int textColor; // TRANSPARENT = 0;
        public int iconTintColor; // TRANSPARENT = 0;
        private Drawable iconDrawable;

        public Builder() {

        }

        public Builder textBackgroundColor(int textBackgroundColor) {
            this.textBackgroundColor = textBackgroundColor;
            return this;
        }

        public Builder textColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder iconTintColor(int iconTintColor) {
            this.iconTintColor = iconTintColor;
            return this;
        }

        public Builder iconDrawable(Drawable iconDrawable) {
            this.iconDrawable = iconDrawable;
            return this;
        }

    }

//    public static void update(final MenuItem menu, int badgeCount) {
//        update(null,menu, null, String.valueOf(formatNumber(badgeCount,true)));
//    }

    public static void update(final Activity activity, final MenuItem menu, MenuItemBadge.Builder builder) {
        update(activity, menu, builder, null);

    }

    public static void update(final Activity activity, final MenuItem menu, MenuItemBadge.Builder builder, final ActionItemBadgeListener listener) {
        if (menu == null) return;
        FrameLayout badge;
        MaterialBadgeTextView badgeTextView;
        ImageView imageView;

        badge = (FrameLayout) menu.getActionView();

        badgeTextView = (MaterialBadgeTextView) badge.findViewById(R.id.menu_badge);
        imageView = (ImageView) badge.findViewById(R.id.menu_badge_icon);

        //Display icon in ImageView
        if (imageView != null && builder != null) {
            if (builder.iconDrawable != null) {
                imageView.setImageDrawable(builder.iconDrawable);
            }

            if (builder.iconTintColor != Color.TRANSPARENT) {
                imageView.setColorFilter(builder.iconTintColor);
            }
        }

        if (badgeTextView != null && builder != null && builder.textBackgroundColor != Color.TRANSPARENT) {
            badgeTextView.setBackgroundColor(builder.textBackgroundColor);
        }

        if (badgeTextView != null && builder != null && builder.textColor != Color.TRANSPARENT) {
            badgeTextView.setTextColor(builder.textColor);
        }


        //Bind onOptionsItemSelected to the activity
        if (activity != null) {
            badge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean consumed = false;
                    if (listener != null) {
                        consumed = listener.onOptionsItemSelected(menu);
                    }
                    if (!consumed) {
                        activity.onMenuItemSelected(Window.FEATURE_OPTIONS_PANEL, menu);
                    }
                }
            });

            badge.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Display display = activity.getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int width = size.x;
                    Toast toast = Toast.makeText(activity, menu.getTitle(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, width / 5, convertDpToPx(activity, 48));
                    toast.show();
                    return true;
                }
            });
        }

        menu.setVisible(true);
    }

    public static MaterialBadgeTextView getBadgeTextView(MenuItem menu) {
        if (menu == null) {
            return null;
        }
        FrameLayout badge = (FrameLayout) menu.getActionView();
        MaterialBadgeTextView badgeView = (MaterialBadgeTextView) badge.findViewById(R.id.menu_badge);
        return badgeView;
    }

    /**
     * hide the given menu item
     *
     * @param menu
     */
    public static void hide(MenuItem menu) {
        menu.setVisible(false);
    }


    public interface ActionItemBadgeListener {
        boolean onOptionsItemSelected(MenuItem menu);
    }

    public static int convertDpToPx(Context context, float dp) {
        return (int) applyDimension(COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static void setBackgroundCompat(View v, Drawable d) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackgroundDrawable(d);
        } else {
            v.setBackground(d);
        }
    }

    public static String formatNumber(int value, boolean limitLength) {
        if (value < 0) {
            return "-" + formatNumber(-value, limitLength);
        } else if (value < 100) {
            return Long.toString(value);
        } else {
            return "99+";
        }

    }
}
