package br.tiagohm.markdownview.ext.twitter.internal;

import android.os.ConditionVariable;
import android.text.TextUtils;

import com.vladsch.flexmark.html.CustomNodeRenderer;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.LinkType;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRendererFactory;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;
import com.vladsch.flexmark.util.options.DataHolder;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import br.tiagohm.markdownview.Utils;
import br.tiagohm.markdownview.ext.twitter.Twitter;

public class TwitterNodeRenderer implements NodeRenderer {
    private ConditionVariable mCondition = new ConditionVariable(false);
    private String mHtml = null;

    public TwitterNodeRenderer(DataHolder options) {
    }

    @Override
    public Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
        HashSet<NodeRenderingHandler<?>> set = new HashSet<>();
        set.add(new NodeRenderingHandler<>(Twitter.class, new CustomNodeRenderer<Twitter>() {
            @Override
            public void render(Twitter node, NodeRendererContext context, HtmlWriter html) {
                TwitterNodeRenderer.this.render(node, context, html);
            }
        }));
        return set;
    }

    private void render(final Twitter node, final NodeRendererContext context, final HtmlWriter html) {
        final String name = node.getText().toString();

        if (context.isDoNotRenderLinks()) {
            context.renderChildren(node);
        } else if (!TextUtils.isEmpty(name)) {
            String value, url;

            try {
                value = URLEncoder.encode(context.resolveLink(LinkType.LINK, node.getUrl().unescape(), null).getUrl(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                context.renderChildren(node);
                return;
            }

            if (name.equals("tweet")) {
                url = String.format("https://publish.twitter.com/oembed?url=https://twitter.com/twitter/status/%s", value);
            } else if (name.equals("tweet-hide-cards")) {
                url = String.format("https://publish.twitter.com/oembed?url=https://twitter.com/twitter/status/%s&hide_media=true", value);
            } else if (name.equals("follow")) {
                mHtml = String.format("<a href=\"https://twitter.com/%s\" data-size=\"large\" class=\"twitter-follow-button\" data-show-count=\"true\">Follow @%s</a><script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>",
                        value, value);
                html.srcPos(node.getChars()).append(mHtml);
                return;
            } else {
                context.renderChildren(node);
                return;
            }

            mHtml = null;
            new Thread(new LoadTweetRunnable(url)).start();
            mCondition.close();
            mCondition.block();

            if (mHtml == null) {
                context.renderChildren(node);
            } else {
                mHtml = mHtml.replaceAll("src=\"//", "src=\"https://");
                html.srcPos(node.getChars()).append(mHtml);
            }
        }
    }

    public static class Factory implements NodeRendererFactory {
        @Override
        public NodeRenderer create(final DataHolder options) {
            return new TwitterNodeRenderer(options);
        }
    }

    private class LoadTweetRunnable implements Runnable {
        private String mUrl;

        public LoadTweetRunnable(String url) {
            mUrl = url;
        }

        @Override
        public void run() {
            InputStream is = null;

            try {
                URLConnection connection = new URL(mUrl).openConnection();
                connection.setReadTimeout(5000);
                connection.setConnectTimeout(5000);
                connection.setRequestProperty("Accept-Charset", "UTF-8");

                String json = Utils.getStringFromInputStream(is = connection.getInputStream());

                if (!TextUtils.isEmpty(json)) {
                    JSONObject tweet = new JSONObject(json);
                    mHtml = tweet.getString("html");
                } else {
                    mHtml = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                mCondition.open();
            }
        }
    }
}
