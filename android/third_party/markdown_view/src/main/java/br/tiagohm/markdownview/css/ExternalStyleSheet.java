package br.tiagohm.markdownview.css;

import java.io.File;
import java.net.URL;

public class ExternalStyleSheet implements StyleSheet {
    private String mUrl;
    private String mMediaQuery;

    public ExternalStyleSheet(String url) {
        mUrl = url;
    }

    public ExternalStyleSheet(String url, String mediaQuery) {
        this(url);
        mMediaQuery = mediaQuery;
    }

    public static ExternalStyleSheet fromUrl(URL url, String mediaQuery) {
        return new ExternalStyleSheet(url.toString(), mediaQuery);
    }

    public static ExternalStyleSheet fromFile(File file, String mediaQuery) {
        return new ExternalStyleSheet(file.getAbsolutePath(), mediaQuery);
    }

    public static StyleSheet fromAsset(String path, String mediaQuery) {
        return new ExternalStyleSheet("file:///android_asset/" + path, mediaQuery);
    }

    public String getUrl() {
        return mUrl;
    }

    public String getMediaQuery() {
        return mMediaQuery;
    }

    @Override
    public String toString() {
        return getUrl();
    }

    @Override
    public String toHTML() {
        return String.format("<link rel=\"stylesheet\" type=\"text/css\" media=\"%s\" href=\"%s\" />\n",
                getMediaQuery() == null ? "" : getMediaQuery(),
                getUrl());
    }
}
