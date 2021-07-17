package br.tiagohm.markdownview.js;

public class ExternalScript implements JavaScript {
    private final String mSrc;
    private final boolean mIsAync;
    private final boolean mIsDefer;
    private final String mType;

    public ExternalScript(String src, boolean isAync, boolean isDefer, String type) {
        mSrc = src;
        mIsAync = isAync;
        mIsDefer = isDefer;
        mType = type;
    }

    public ExternalScript(String url, boolean isAync, boolean isDefer) {
        this(url, isAync, isDefer, "text/javascript");
    }

    public String getSrc() {
        return mSrc;
    }

    public String getType() {
        return mType;
    }

    public boolean isAync() {
        return mIsAync;
    }

    public boolean isDefer() {
        return mIsDefer;
    }

    @Override
    public String toHTML() {
        return String.format("<script %s%ssrc='%s' type='%s'></script>\n",
                isAync() ? "async " : "",
                isDefer() ? "defer " : "",
                getSrc(),
                getType());
    }
}
