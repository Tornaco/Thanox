package github.tornaco.thanos.android.ops.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import github.tornaco.thanos.android.ops.R;

public class OpsTemplate {

    public github.tornaco.android.thanos.core.secure.ops.OpsTemplate legacy;

    @StringRes
    public final int titleRes;
    @StringRes
    public final int summaryRes;
    @DrawableRes
    public final int iconRes;

    public final int sort;

    public static final OpsTemplate THANOX_TEMPLATE = new OpsTemplate(
            github.tornaco.android.thanos.core.secure.ops.OpsTemplate.THANOX_TEMPLATE,
            R.string.module_ops_category_thanox,
            R.string.module_ops_category_thanox,
            R.drawable.module_ops_ic_shield_cross_line,
            -1
    );

    public static final OpsTemplate LOCATION_TEMPLATE = new OpsTemplate(
            github.tornaco.android.thanos.core.secure.ops.OpsTemplate.LOCATION_TEMPLATE,
            R.string.module_ops_category_location,
            R.string.module_ops_category_location,
            github.tornaco.android.thanos.module.common.R.drawable.module_common_ic_settings_fill,
            0
    );

    public static final OpsTemplate PERSONAL_TEMPLATE = new OpsTemplate(
            github.tornaco.android.thanos.core.secure.ops.OpsTemplate.PERSONAL_TEMPLATE,
            R.string.module_ops_category_personal,
            R.string.module_ops_category_personal,
            github.tornaco.android.thanos.module.common.R.drawable.module_common_ic_settings_fill,
            1
    );

    public static final OpsTemplate MESSAGING_TEMPLATE = new OpsTemplate(
            github.tornaco.android.thanos.core.secure.ops.OpsTemplate.MESSAGING_TEMPLATE,
            R.string.module_ops_category_message,
            R.string.module_ops_category_message,
            github.tornaco.android.thanos.module.common.R.drawable.module_common_ic_settings_fill,
            2
    );

    public static final OpsTemplate MEDIA_TEMPLATE = new OpsTemplate(
            github.tornaco.android.thanos.core.secure.ops.OpsTemplate.MEDIA_TEMPLATE,
            R.string.module_ops_category_media,
            R.string.module_ops_category_media,
            github.tornaco.android.thanos.module.common.R.drawable.module_common_ic_settings_fill,
            3
    );

    public static final OpsTemplate DEVICE_TEMPLATE = new OpsTemplate(
            github.tornaco.android.thanos.core.secure.ops.OpsTemplate.DEVICE_TEMPLATE,
            R.string.module_ops_category_device,
            R.string.module_ops_category_device,
            github.tornaco.android.thanos.module.common.R.drawable.module_common_ic_settings_fill,
            4
    );

    public static final OpsTemplate RUN_IN_BACKGROUND_TEMPLATE = new OpsTemplate(
            github.tornaco.android.thanos.core.secure.ops.OpsTemplate.RUN_IN_BACKGROUND_TEMPLATE,
            R.string.module_ops_category_bg,
            R.string.module_ops_category_bg,
            github.tornaco.android.thanos.module.common.R.drawable.module_common_ic_settings_fill,
            5
    );

    // this template should contain all ops which are not part of any other template in
    // ALL_TEMPLATES
    public static final OpsTemplate REMAINING_TEMPLATE = new OpsTemplate(
            github.tornaco.android.thanos.core.secure.ops.OpsTemplate.REMAINING_TEMPLATE,
            R.string.module_ops_category_remaining,
            R.string.module_ops_category_remaining,
            github.tornaco.android.thanos.module.common.R.drawable.module_common_ic_settings_fill,
            6
    );


    // this template contains all permissions grouped by templates
    public static final OpsTemplate[] ALL_PERMS_TEMPLATES = new OpsTemplate[]{
            THANOX_TEMPLATE,
            LOCATION_TEMPLATE,
            PERSONAL_TEMPLATE,
            MESSAGING_TEMPLATE,
            MEDIA_TEMPLATE,
            DEVICE_TEMPLATE,
            //  RUN_IN_BACKGROUND_TEMPLATE,
            REMAINING_TEMPLATE
    };

    public OpsTemplate(github.tornaco.android.thanos.core.secure.ops.OpsTemplate legacy, int titleRes, int summaryRes, int iconRes, int sort) {
        this.legacy = legacy;
        this.titleRes = titleRes;
        this.summaryRes = summaryRes;
        this.iconRes = iconRes;
        this.sort = sort;
    }
}
