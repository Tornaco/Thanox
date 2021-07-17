package com.github.javiersantos.materialstyleddialogs;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

public class MaterialStyledDialog extends DialogBase {
    protected final Builder mBuilder;

    public final Builder getBuilder() {
        return mBuilder;
    }

    protected MaterialStyledDialog(Builder builder) {
        super(builder.context, R.style.MD_Dark);
        mBuilder = builder;
        mBuilder.dialog = initMaterialStyledDialog(builder);
    }

    @UiThread
    public void show() {
        if (mBuilder != null && mBuilder.dialog != null)
            mBuilder.dialog.show();
    }

    @UiThread
    public void dismiss() {
        if (mBuilder != null && mBuilder.dialog != null)
            mBuilder.dialog.dismiss();
    }

    @UiThread
    private MaterialDialog initMaterialStyledDialog(final Builder builder) {
        final MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(builder.context).theme(Theme.LIGHT);

        // Set cancelable
        dialogBuilder.cancelable(builder.isCancelable);

        // Set style
        dialogBuilder.customView(initStyle(builder), false);

        // Set positive button
        if (builder.positive != null && builder.positive.length() != 0)
            dialogBuilder.positiveText(builder.positive);
        if (builder.positiveCallback != null)
            dialogBuilder.onPositive(builder.positiveCallback);

        // set negative button
        if (builder.negative != null && builder.negative.length() != 0)
            dialogBuilder.negativeText(builder.negative);
        if (builder.negativeCallback != null)
            dialogBuilder.onNegative(builder.negativeCallback);

        // Set neutral button
        if (builder.neutral != null && builder.neutral.length() != 0)
            dialogBuilder.neutralText(builder.neutral);
        if (builder.neutralCallback != null)
            dialogBuilder.onNeutral(builder.neutralCallback);

        // Set auto dismiss when touching the buttons
        dialogBuilder.autoDismiss(builder.isAutoDismiss);

        // Build the dialog with the previous configuration
        MaterialDialog materialDialog = dialogBuilder.build();

        // Set dialog animation and animation duration
        if (builder.isDialogAnimation) {
            if (builder.duration == Duration.NORMAL) {
                materialDialog.getWindow().getAttributes().windowAnimations = R.style.MaterialStyledDialogs_DialogAnimationNormal;
            } else if (builder.duration == Duration.FAST) {
                materialDialog.getWindow().getAttributes().windowAnimations = R.style.MaterialStyledDialogs_DialogAnimationFast;
            } else if (builder.duration == Duration.SLOW) {
                materialDialog.getWindow().getAttributes().windowAnimations = R.style.MaterialStyledDialogs_DialogAnimationSlow;
            }
        }

        return materialDialog;
    }

    @UiThread
    @TargetApi(16)
    private View initStyle(final Builder builder) {
        LayoutInflater inflater = LayoutInflater.from(builder.context);
        View contentView;

        switch (builder.style) {
            case HEADER_WITH_ICON:
                contentView = inflater.inflate(R.layout.style_dialog_header_icon, null);
                break;
            case HEADER_WITH_TITLE:
                contentView = inflater.inflate(R.layout.style_dialog_header_title, null);
                break;
            default:
                contentView = inflater.inflate(R.layout.style_dialog_header_icon, null);
                break;
        }

        // Init Views
        RelativeLayout dialogHeaderColor = (RelativeLayout) contentView.findViewById(R.id.md_styled_header_color);
        AppCompatImageView dialogHeader = (AppCompatImageView) contentView.findViewById(R.id.md_styled_header);
        AppCompatImageView dialogPic = (AppCompatImageView) contentView.findViewById(R.id.md_styled_header_pic);
        TextView dialogTitle = (TextView) contentView.findViewById(R.id.md_styled_dialog_title);
        TextView dialogDescription = (TextView) contentView.findViewById(R.id.md_styled_dialog_description);
        FrameLayout dialogCustomViewGroup = (FrameLayout) contentView.findViewById(R.id.md_styled_dialog_custom_view);
        View dialogDivider = contentView.findViewById(R.id.md_styled_dialog_divider);

        // Set header color or drawable
        if (builder.headerDrawable != null) {
            dialogHeader.setImageDrawable(builder.headerDrawable);
            // Apply gray/darker overlay to the header if enabled
            if (builder.isDarkerOverlay) {
                dialogHeader.setColorFilter(Color.rgb(123, 123, 123), PorterDuff.Mode.MULTIPLY);
            }
        }
        dialogHeaderColor.setBackgroundColor(builder.primaryColor);
        dialogHeader.setScaleType(builder.headerScaleType);

        //Set the custom view
        if (builder.customView != null) {
            dialogCustomViewGroup.addView(builder.customView);
            dialogCustomViewGroup.setPadding(builder.customViewPaddingLeft, builder.customViewPaddingTop, builder.customViewPaddingRight, builder.customViewPaddingBottom);
        }

        // Set header icon
        if (builder.iconDrawable != null) {
            if (builder.style == Style.HEADER_WITH_TITLE) {
                Log.e("MaterialStyledDialog", "You can't set an icon to the HEADER_WITH_TITLE style.");
            } else {
                dialogPic.setImageDrawable(builder.iconDrawable);
            }
        }

        // Set dialog title
        if (builder.title != null && builder.title.length() != 0) {
            dialogTitle.setText(builder.title);
        } else {
            dialogTitle.setVisibility(View.GONE);
        }

        // Set dialog description
        if (builder.description != null && builder.description.length() != 0) {
            dialogDescription.setText(builder.description);

            // Set scrollable
            dialogDescription.setVerticalScrollBarEnabled(builder.isScrollable);
            if (builder.isScrollable) {
                dialogDescription.setMaxLines(builder.maxLines);
                dialogDescription.setMovementMethod(new ScrollingMovementMethod());
            } else {
                dialogDescription.setMaxLines(Integer.MAX_VALUE);
            }
        } else {
            dialogDescription.setVisibility(View.GONE);
        }

        // Set icon animation
        if (builder.isIconAnimation) {
            if (builder.style != Style.HEADER_WITH_TITLE) {
                UtilsAnimation.zoomInAndOutAnimation(builder.context, dialogPic);
            }
        }

        // Show dialog divider if enabled
        if (builder.isDialogDivider) {
            dialogDivider.setVisibility(View.VISIBLE);
        }

        return contentView;
    }

    public static class Builder implements IBuilder {
        protected Context context;

        // build() and show()
        protected MaterialDialog dialog;

        protected Style style; // setStyle()
        protected Duration duration; // withDialogAnimation()
        protected boolean isIconAnimation, isDialogAnimation, isDialogDivider, isCancelable, isScrollable, isDarkerOverlay, isAutoDismiss; // withIconAnimation(), withDialogAnimation(), withDivider(), setCancelable(), setScrollable(), withDarkerOverlay(), autoDismiss()
        protected Drawable headerDrawable, iconDrawable; // setHeaderDrawable(), setIconDrawable()
        protected Integer primaryColor, maxLines; // setHeaderColor(), setScrollable()
        protected CharSequence title, description; // setTitle(), setDescription()
        protected View customView; // setCustomView()
        protected int customViewPaddingLeft, customViewPaddingTop, customViewPaddingRight, customViewPaddingBottom;
        protected AppCompatImageView.ScaleType headerScaleType;

        // .setPositive(), setNegative() and setNeutral()
        protected CharSequence positive, negative, neutral;
        protected MaterialDialog.SingleButtonCallback positiveCallback, negativeCallback, neutralCallback;

        public Builder(Context context) {
            this.context = context;
            this.style = Style.HEADER_WITH_ICON;
            this.isIconAnimation = true;
            this.isDialogAnimation = false;
            this.isDialogDivider = false;
            this.isDarkerOverlay = false;
            this.duration = Duration.NORMAL;
            this.isCancelable = true;
            this.primaryColor = UtilsLibrary.getPrimaryColor(context);
            this.isScrollable = false;
            this.maxLines = 5;
            this.isAutoDismiss = true;
            this.headerScaleType = AppCompatImageView.ScaleType.CENTER_CROP;
        }

        @Override
        public Builder setCustomView(View customView) {
            this.customView = customView;
            this.customViewPaddingLeft = 0;
            this.customViewPaddingRight = 0;
            this.customViewPaddingTop = 0;
            this.customViewPaddingBottom = 0;
            return this;
        }

        @Override
        public Builder setCustomView(View customView, int left, int top, int right, int bottom) {
            this.customView = customView;
            this.customViewPaddingLeft = UtilsLibrary.dpToPixels(context, left);
            this.customViewPaddingRight = UtilsLibrary.dpToPixels(context, right);
            this.customViewPaddingTop = UtilsLibrary.dpToPixels(context, top);
            this.customViewPaddingBottom = UtilsLibrary.dpToPixels(context, bottom);
            return this;
        }

        @Override
        public Builder setStyle(Style style) {
            this.style = style;
            return this;
        }

        @Override
        public Builder withIconAnimation(Boolean withAnimation) {
            this.isIconAnimation = withAnimation;
            return this;
        }

        @Override
        public Builder withDialogAnimation(Boolean withAnimation) {
            this.isDialogAnimation = withAnimation;
            return this;
        }

        @Override
        public Builder withDialogAnimation(Boolean withAnimation, Duration duration) {
            this.isDialogAnimation = withAnimation;
            this.duration = duration;
            return this;
        }

        @Override
        public Builder withDivider(Boolean withDivider) {
            this.isDialogDivider = withDivider;
            return this;
        }

        @Override
        public Builder withDarkerOverlay(Boolean withDarkerOverlay) {
            this.isDarkerOverlay = withDarkerOverlay;
            return this;
        }

        @Override
        public Builder setIcon(@NonNull Drawable icon) {
            this.iconDrawable = icon;
            return this;
        }

        @Override
        public Builder setIcon(@DrawableRes Integer iconRes) {
            this.iconDrawable = ResourcesCompat.getDrawable(context.getResources(), iconRes, null);
            return this;
        }

        @Override
        public Builder setTitle(@StringRes int titleRes) {
            setTitle(this.context.getString(titleRes));
            return this;
        }

        @Override
        public Builder setTitle(@NonNull CharSequence title) {
            this.title = title;
            return this;
        }

        @Override
        public Builder setDescription(@StringRes int descriptionRes) {
            setDescription(this.context.getString(descriptionRes));
            return this;
        }

        @Override
        public Builder setDescription(@NonNull CharSequence description) {
            this.description = description;
            return this;
        }

        @Override
        public Builder setHeaderColor(@ColorRes int color) {
            this.primaryColor = UtilsLibrary.getColor(context, color);
            return this;
        }

        @Override
        public Builder setHeaderColorInt(@ColorInt int color) {
            this.primaryColor = color;
            return this;
        }

        @Override
        public Builder setHeaderDrawable(@NonNull Drawable drawable) {
            this.headerDrawable = drawable;
            return this;
        }

        @Override
        public Builder setHeaderDrawable(@DrawableRes Integer drawableRes) {
            this.headerDrawable = ResourcesCompat.getDrawable(context.getResources(), drawableRes, null);
            return this;
        }

        @Override
        @Deprecated
        public Builder setPositive(String text, MaterialDialog.SingleButtonCallback callback) {
            this.positive = text;
            this.positiveCallback = callback;
            return this;
        }

        @Override
        public Builder setPositiveText(@StringRes int buttonTextRes) {
            setPositiveText(this.context.getString(buttonTextRes));
            return this;
        }

        @Override
        public Builder setPositiveText(@NonNull CharSequence buttonText) {
            this.positive = buttonText;
            return this;
        }

        @Override
        public Builder onPositive(@NonNull MaterialDialog.SingleButtonCallback callback) {
            this.positiveCallback = callback;
            return this;
        }

        @Override
        @Deprecated
        public Builder setNegative(String text, MaterialDialog.SingleButtonCallback callback) {
            this.negative = text;
            this.negativeCallback = callback;
            return this;
        }

        @Override
        public Builder setNegativeText(@StringRes int buttonTextRes) {
            setNegativeText(this.context.getString(buttonTextRes));
            return this;
        }

        @Override
        public Builder setNegativeText(@NonNull CharSequence buttonText) {
            this.negative = buttonText;
            return this;
        }

        @Override
        public Builder onNegative(@NonNull MaterialDialog.SingleButtonCallback callback) {
            this.negativeCallback = callback;
            return this;
        }

        @Override
        @Deprecated
        public Builder setNeutral(String text, MaterialDialog.SingleButtonCallback callback) {
            this.neutral = text;
            this.neutralCallback = callback;
            return this;
        }

        @Override
        public Builder setNeutralText(@StringRes int buttonTextRes) {
            setNeutralText(this.context.getString(buttonTextRes));
            return this;
        }

        @Override
        public Builder setNeutralText(@NonNull CharSequence buttonText) {
            this.neutral = buttonText;
            return this;
        }

        @Override
        public Builder onNeutral(@NonNull MaterialDialog.SingleButtonCallback callback) {
            this.neutralCallback = callback;
            return this;
        }

        @Override
        public Builder setHeaderScaleType(AppCompatImageView.ScaleType scaleType) {
            this.headerScaleType = scaleType;
            return this;
        }

        @Override
        public Builder setCancelable(Boolean cancelable) {
            this.isCancelable = cancelable;
            return this;
        }

        @Override
        public Builder setScrollable(Boolean scrollable) {
            this.isScrollable = scrollable;
            return this;
        }

        @Override
        public Builder setScrollable(Boolean scrollable, Integer maxLines) {
            this.isScrollable = scrollable;
            this.maxLines = maxLines;
            return this;
        }

        @Override
        public Builder autoDismiss(Boolean dismiss) {
            this.isAutoDismiss = dismiss;
            return this;
        }

        @UiThread
        public MaterialStyledDialog build() {
            return new MaterialStyledDialog(this);
        }

        @UiThread
        public MaterialStyledDialog show() {
            MaterialStyledDialog materialStyledDialog = build();
            materialStyledDialog.show();
            return materialStyledDialog;
        }

    }

}
