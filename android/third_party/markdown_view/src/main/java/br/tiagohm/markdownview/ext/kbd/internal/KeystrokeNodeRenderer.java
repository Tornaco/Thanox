package br.tiagohm.markdownview.ext.kbd.internal;

import com.vladsch.flexmark.html.CustomNodeRenderer;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRendererFactory;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;
import com.vladsch.flexmark.util.options.DataHolder;

import java.util.HashSet;
import java.util.Set;

import br.tiagohm.markdownview.ext.kbd.Keystroke;

public class KeystrokeNodeRenderer implements NodeRenderer {

    public KeystrokeNodeRenderer(DataHolder options) {
    }

    @Override
    public Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
        HashSet<NodeRenderingHandler<?>> set = new HashSet<>();
        set.add(new NodeRenderingHandler<>(Keystroke.class, new CustomNodeRenderer<Keystroke>() {
            @Override
            public void render(Keystroke node, NodeRendererContext context, HtmlWriter html) {
                KeystrokeNodeRenderer.this.render(node, context, html);
            }
        }));

        return set;
    }

    private void render(Keystroke node, NodeRendererContext context, HtmlWriter html) {
        html.withAttr().tag("kbd");
        html.append(node.getText().trim());
        html.tag("/kbd");
    }

    public static class Factory implements NodeRendererFactory {
        @Override
        public NodeRenderer create(final DataHolder options) {
            return new KeystrokeNodeRenderer(options);
        }
    }
}
