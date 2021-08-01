package github.tornaco.android.thanos.settings;

import android.app.Activity;

import java.util.Objects;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.BSD2ClauseLicense;
import de.psdev.licensesdialog.licenses.MITLicense;
import de.psdev.licensesdialog.licenses.MozillaPublicLicense20;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;

public class LicenseHelper {

    static void showLicenseDialog(Activity activity) {
        final Notices notices = new Notices();

        notices.addNotice(
                new Notice(
                        "Lombok",
                        "https://projectlombok.org/",
                        " Copyright © 2009-2018 The Project Lombok Authors",
                        new MITLicense()));

        notices.addNotice(
                new Notice(
                        "guava",
                        "https://github.com/google/guava",
                        null,
                        new MITLicense()));

        notices.addNotice(
                new Notice(
                        "retrofit",
                        "https://github.com/square/retrofit",
                        "Copyright 2013 Square, Inc.",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "RxJava",
                        "https://github.com/ReactiveX/RxJava",
                        "Copyright (c) 2016-present, RxJava Contributors.",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "RxAndroid",
                        "https://github.com/ReactiveX/RxAndroid",
                        "Copyright 2015 The RxAndroid authors",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "RecyclerView-FastScroll",
                        "https://github.com/timusus/RecyclerView-FastScroll",
                        null,
                        new MITLicense()));

        notices.addNotice(
                new Notice(
                        "glide",
                        "https://github.com/bumptech/glide",
                        null,
                        new GlideLicense()));

        notices.addNotice(
                new Notice(
                        "material-searchview",
                        "https://github.com/Shahroz16/material-searchview",
                        " Copyright (C) 2016 Tim Malseed",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "PatternLockView",
                        "https://github.com/aritraroy/PatternLockView",
                        null,
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "PinLockView",
                        "https://github.com/aritraroy/PinLockView",
                        null,
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "MPAndroidChart",
                        "https://github.com/PhilJay/MPAndroidChart",
                        "Copyright 2019 Philipp Jahoda",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "easy-rules",
                        "https://github.com/j-easy/easy-rules",
                        "Copyright (c) 2019 Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)",
                        new MITLicense()));

        notices.addNotice(
                new Notice(
                        "Timber",
                        "https://github.com/JakeWharton/timber",
                        "Copyright 2013 Jake Wharton",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "FireCrasher",
                        "https://github.com/osama-raddad/FireCrasher",
                        "Copyright 2019, Osama Raddad",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "Xposed",
                        "https://github.com/rovo89/Xposed",
                        null,
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "MaterialBadgeTextView",
                        "https://github.com/matrixxun/MaterialBadgeTextView",
                        null,
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "FuzzyDateFormatter",
                        "https://github.com/izacus/FuzzyDateFormatter",
                        "Copyright 2015 Jernej Virag",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "MaterialSearchView",
                        "https://github.com/MiguelCatalan/MaterialSearchView",
                        "Copyright 2015 Miguel Catalan Bañuls",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "zip4j",
                        "https://github.com/srikanth-lingala/zip4j",
                        null,
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "android_native_code_view",
                        "https://github.com/vic797/android_native_code_view",
                        "Copyright 2017 Victor Campos",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "NoNonsense-FilePicker",
                        "https://github.com/spacecowboy/NoNonsense-FilePicker",
                        null,
                        new MozillaPublicLicense20()));

        notices.addNotice(
                new Notice(
                        "Flipper",
                        "https://github.com/baldapps/Flipper",
                        null,
                        new MITLicense()));

        notices.addNotice(
                new Notice(
                        "android_native_code_view",
                        "https://github.com/vic797/android_native_code_view",
                        "Copyright 2017 Victor Campos",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "ApkBuilder",
                        "https://github.com/hyb1996/Auto.js-ApkBuilder",
                        null,
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "apk-parser",
                        "https://github.com/hsiafan/apk-parser",
                        null,
                        new BSD2ClauseLicense()));

        notices.addNotice(
                new Notice(
                        "XLog",
                        "https://github.com/elvishew/xLog",
                        "Copyright 2018 Elvis Hew",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "libsu",
                        "https://github.com/topjohnwu/libsu",
                        null,
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "MaterialProgressBar",
                        "https://github.com/zhanghai/MaterialProgressBar",
                        "Copyright 2015 Hai Zhang",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "MaterialStyledDialogs",
                        "https://github.com/javiersantos/MaterialStyledDialogs",
                        "Copyright 2016-2018 Javier Santos",
                        new ApacheSoftwareLicense20()));

        notices.addNotice(
                new Notice(
                        "time-duration-picker",
                        "https://github.com/svenwiegand/time-duration-picker",
                        null,
                        new MITLicense()));

        new LicensesDialog.Builder(Objects.requireNonNull(activity))
                .setNotices(notices)
                .setIncludeOwnLicense(true)
                .build()
                .show();
    }
}
