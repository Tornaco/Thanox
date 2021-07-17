package com.github.javiersantos.materialstyleddialogs;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

interface IBuilder {

    /**
     * Set custom view for the dialog.
     *
     * @param customView to apply
     * @return this
     */
    MaterialStyledDialog.Builder setCustomView(View customView);

    /**
     * Set custom view for the dialog with optional padding in DP.
     *
     * @param customView to apply
     * @param left       padding left in DP
     * @param top        padding top in DP
     * @param right      padding right in DP
     * @param bottom     padding bottom in DP
     * @return this
     */
    MaterialStyledDialog.Builder setCustomView(View customView, int left, int top, int right, int bottom);

    /**
     * Set an style for the dialog. Default: Style.STYLE_HEADER.
     *
     * @param style to apply
     * @return this
     * @see com.github.javiersantos.materialstyleddialogs.enums.Style
     */
    MaterialStyledDialog.Builder setStyle(Style style);

    /**
     * Set if the header icon will be displayed with an initial animation. Default: true.
     *
     * @param withAnimation true to enable animation, false otherwise
     * @return this
     */
    MaterialStyledDialog.Builder withIconAnimation(Boolean withAnimation);

    /**
     * Set if the dialog will be displayed with an open and close animation. Default: false.
     *
     * @param withAnimation true to enable animation, false otherwise
     * @return this
     */
    MaterialStyledDialog.Builder withDialogAnimation(Boolean withAnimation);

    /**
     * Set if the dialog will be displayed with an open and close animation, with custom duration. Default: false, Duration.NORMAL.
     *
     * @param withAnimation true to enable animation, false otherwise
     * @return this
     * @see com.github.javiersantos.materialstyleddialogs.enums.Duration
     */
    MaterialStyledDialog.Builder withDialogAnimation(Boolean withAnimation, Duration duration);

    /**
     * Set if the divider will be displayed before the buttons and after the dialog content. Default: false.
     *
     * @param withDivider true to enable animation, false otherwise
     * @return this
     */
    MaterialStyledDialog.Builder withDivider(Boolean withDivider);

    /**
     * Set if the header will display a gray/darker overlay. Default: false.
     *
     * @param withDarkerOverlay true to apply a darker overlay, false otherwise
     * @return this
     */
    MaterialStyledDialog.Builder withDarkerOverlay(Boolean withDarkerOverlay);

    /**
     * Set an icon for the dialog header
     *
     * @param icon to show
     * @return this
     */
    MaterialStyledDialog.Builder setIcon(@NonNull Drawable icon);

    /**
     * Set an icon for the dialog header
     *
     * @param iconRes to show
     * @return this
     */
    MaterialStyledDialog.Builder setIcon(@DrawableRes Integer iconRes);

    /**
     * Set a title for the dialog
     *
     * @param titleRes to show
     * @return this
     */
    MaterialStyledDialog.Builder setTitle(@StringRes int titleRes);

    /**
     * Set a title for the dialog
     *
     * @param title to show
     * @return this
     */
    MaterialStyledDialog.Builder setTitle(@NonNull CharSequence title);

    /**
     * Set a description for the dialog
     *
     * @param descriptionRes to show
     * @return this
     */
    MaterialStyledDialog.Builder setDescription(@StringRes int descriptionRes);

    /**
     * Set a description for the dialog
     *
     * @param description to show
     * @return this
     */
    MaterialStyledDialog.Builder setDescription(@NonNull CharSequence description);

    /**
     * Set a color for the dialog header. Default: Theme primary color.
     *
     * @param color for the header
     * @return this
     */
    MaterialStyledDialog.Builder setHeaderColor(@ColorRes int color);

    /**
     * Set a color for the dialog header. Default: Theme primary color.
     *
     * @param color for the header
     * @return this
     */
    MaterialStyledDialog.Builder setHeaderColorInt(@ColorInt int color);

    /**
     * Set an image for the dialog header.
     *
     * @param drawable image for the header
     * @return this
     */
    MaterialStyledDialog.Builder setHeaderDrawable(@NonNull Drawable drawable);

    /**
     * Set an image for the dialog header
     *
     * @param drawableRes image for the header
     * @return this
     */
    MaterialStyledDialog.Builder setHeaderDrawable(@DrawableRes Integer drawableRes);

    /**
     * Set a positive button for the dialog
     *
     * @param text     for the button
     * @param callback action to do
     * @return this
     * @deprecated use {{@link #setPositiveText(CharSequence)}, {@link #setPositiveText(int)} and {@link #onPositive(MaterialDialog.SingleButtonCallback)}} instead
     */
    MaterialStyledDialog.Builder setPositive(String text, MaterialDialog.SingleButtonCallback callback);

    /**
     * Set a positive button text for the dialog. E.g.: R.string.accept
     *
     * @param buttonTextRes for the button
     * @return this
     */
    MaterialStyledDialog.Builder setPositiveText(@StringRes int buttonTextRes);

    /**
     * Set a positive button text for the dialog. E.g.: "Accept"
     *
     * @param buttonText for the button
     * @return this
     */
    MaterialStyledDialog.Builder setPositiveText(@NonNull CharSequence buttonText);

    /**
     * Set a positive button action for the dialog
     *
     * @param callback for the button
     * @return this
     */
    MaterialStyledDialog.Builder onPositive(@NonNull MaterialDialog.SingleButtonCallback callback);

    /**
     * Set a negative button for the dialog
     *
     * @param text     for the button
     * @param callback action to do
     * @return this
     * @deprecated use {{@link #setNegativeText(CharSequence)}, {@link #setNegativeText(int)} and {@link #onNegative(MaterialDialog.SingleButtonCallback)}} instead
     */
    MaterialStyledDialog.Builder setNegative(String text, MaterialDialog.SingleButtonCallback callback);

    /**
     * Set a negative button text for the dialog. E.g.: R.string.cancel
     *
     * @param buttonTextRes for the button
     * @return this
     */
    MaterialStyledDialog.Builder setNegativeText(@StringRes int buttonTextRes);

    /**
     * Set a negative button text for the dialog. E.g.: "Decline"
     *
     * @param buttonText for the button
     * @return this
     */
    MaterialStyledDialog.Builder setNegativeText(@NonNull CharSequence buttonText);

    /**
     * Set a negative button action for the dialog
     *
     * @param callback for the button
     * @return this
     */
    MaterialStyledDialog.Builder onNegative(@NonNull MaterialDialog.SingleButtonCallback callback);

    /**
     * Set a neutral button for the dialog
     *
     * @param text     for the button
     * @param callback action to do
     * @return this
     * @deprecated use {{@link #setNeutralText(CharSequence)}, {@link #setNeutralText(int)} and {@link #onNeutral(MaterialDialog.SingleButtonCallback)}} instead
     */
    MaterialStyledDialog.Builder setNeutral(String text, MaterialDialog.SingleButtonCallback callback);

    /**
     * Set a neutral button text for the dialog. E.g.: R.string.later
     *
     * @param buttonTextRes for the button
     * @return this
     */
    MaterialStyledDialog.Builder setNeutralText(@StringRes int buttonTextRes);

    /**
     * Set a neutral button text for the dialog. E.g.: "Maybe later"
     *
     * @param buttonText for the button
     * @return this
     */
    MaterialStyledDialog.Builder setNeutralText(@NonNull CharSequence buttonText);

    /**
     * Set a neutral button action for the dialog
     *
     * @param callback for the button
     * @return this
     */
    MaterialStyledDialog.Builder onNeutral(@NonNull MaterialDialog.SingleButtonCallback callback);

    /**
     * Set the scale type for the header ImageView.
     *
     * @param scaleType the scale type for the header ImageView
     * @return this
     */
    MaterialStyledDialog.Builder setHeaderScaleType(ImageView.ScaleType scaleType);

    /**
     * Set if the dialog will be hidden when touching outside
     *
     * @param cancelable true to enable, false otherwise
     * @return this
     */
    MaterialStyledDialog.Builder setCancelable(Boolean cancelable);

    /**
     * Set if the description will be isScrollable. Default: false.
     *
     * @param scrollable true to enable isScrollable description, false otherwise
     * @return this
     */
    MaterialStyledDialog.Builder setScrollable(Boolean scrollable);

    /**
     * Set if the description will be isScrollable, with custom maximum lines. Default: false, 5.
     *
     * @param scrollable true to enable isScrollable description, false otherwise
     * @return this
     */
    MaterialStyledDialog.Builder setScrollable(Boolean scrollable, Integer maxLines);

    /**
     * Set if the dialog will be dismissed when a button is pressed. Default: true.
     *
     * @param dismiss true to dismiss dialog when a button is pressed
     * @return this
     */
    MaterialStyledDialog.Builder autoDismiss(Boolean dismiss);

}
