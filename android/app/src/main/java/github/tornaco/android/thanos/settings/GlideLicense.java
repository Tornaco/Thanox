package github.tornaco.android.thanos.settings;

import android.content.Context;

import de.psdev.licensesdialog.licenses.License;

public class GlideLicense extends License {
    @Override
    public String getName() {
        return "Glide LICENSE";
    }

    @Override
    public String readSummaryTextFromResources(Context context) {
        return "https://github.com/bumptech/glide/blob/master/LICENSE";
    }

    @Override
    public String readFullTextFromResources(Context context) {
        return "https://github.com/bumptech/glide/blob/master/LICENSE";
    }

    @Override
    public String getVersion() {
        return "";
    }

    @Override
    public String getUrl() {
        return "https://github.com/bumptech/glide/blob/master/LICENSE";
    }
}
