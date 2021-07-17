package br.tiagohm.markdownview.ext.label.internal;

import com.vladsch.flexmark.html.CustomNodeRenderer;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRendererFactory;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;
import com.vladsch.flexmark.util.options.DataHolder;

import java.util.HashSet;
import java.util.Set;

import br.tiagohm.markdownview.ext.label.Label;


public class LabelNodeRenderer implements NodeRenderer {

    public LabelNodeRenderer(DataHolder options) {
    }

    @Override
    public Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
        HashSet<NodeRenderingHandler<?>> set = new HashSet<>();
        set.add(new NodeRenderingHandler<>(Label.class, new CustomNodeRenderer<Label>() {
            @Override
            public void render(Label node, NodeRendererContext context, HtmlWriter html) {
                LabelNodeRenderer.this.render(node, context, html);
            }
        }));

        return set;
    }

    private void render(Label node, NodeRendererContext context, HtmlWriter html) {
        if (node.getType() == 3) html.attr("class", "lbl-success");
        else if (node.getType() == 4) html.attr("class", "lbl-warning");
        else if (node.getType() == 5) html.attr("class", "lbl-danger");
        html.withAttr().tag("lbl");
        context.renderChildren(node);
        html.tag("/lbl");
    }

    public static class Factory implements NodeRendererFactory {
        @Override
        public NodeRenderer create(final DataHolder options) {
            return new LabelNodeRenderer(options);
        }
    }
}
