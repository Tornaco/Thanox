/*
 *    Copyright 2017 tiagohm
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package br.tiagohm.markdownview.ext.video.internal;

import android.text.TextUtils;

import com.vladsch.flexmark.html.CustomNodeRenderer;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.LinkType;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRendererFactory;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;
import com.vladsch.flexmark.html.renderer.ResolvedLink;
import com.vladsch.flexmark.util.options.DataHolder;

import java.util.HashSet;
import java.util.Set;

import br.tiagohm.markdownview.ext.video.VideoLink;

public class VideoLinkNodeRenderer implements NodeRenderer {

    public VideoLinkNodeRenderer(DataHolder options) {
    }

    @Override
    public Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
        HashSet<NodeRenderingHandler<?>> set = new HashSet<>();
        set.add(new NodeRenderingHandler<>(VideoLink.class, new CustomNodeRenderer<VideoLink>() {
            @Override
            public void render(VideoLink node, NodeRendererContext context, HtmlWriter html) {
                VideoLinkNodeRenderer.this.render(node, context, html);
            }
        }));
        return set;
    }

    private void render(final VideoLink node, final NodeRendererContext context, final HtmlWriter html) {
        final String name = node.getText().toString();

        if (context.isDoNotRenderLinks()) {
            context.renderChildren(node);
        } else if (!TextUtils.isEmpty(name)) {
            ResolvedLink resolvedLink = context.resolveLink(LinkType.LINK, node.getUrl().unescape(), null);
            if (name.equals("youtube") ||
                    name.equals("yt")) {
                html.attr("class", "player yt-player");
                html.withAttr().tag("div");
                html.attr("type", "text/html");
                html.attr("frameborder", "0");

                html.attr("allowfullscreen", "");
                html.attr("src", String.format("https://www.youtube.com/embed/%s", resolvedLink.getUrl()));
                html.srcPos(node.getChars()).withAttr(resolvedLink).tag("iframe");
                html.tag("/iframe");
                html.tag("/div");
            } else {
                context.renderChildren(node);
            }
        }
    }

    public static class Factory implements NodeRendererFactory {
        @Override
        public NodeRenderer create(final DataHolder options) {
            return new VideoLinkNodeRenderer(options);
        }
    }
}
