package br.tiagohm.markdownview.ext.label;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataHolder;

import br.tiagohm.markdownview.ext.label.internal.LabelDelimiterProcessor;
import br.tiagohm.markdownview.ext.label.internal.LabelNodeRenderer;

public class LabelExtension implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension {
    private LabelExtension() {
    }

    public static Extension create() {
        return new LabelExtension();
    }

    @Override
    public void rendererOptions(final MutableDataHolder options) {

    }

    @Override
    public void parserOptions(final MutableDataHolder options) {

    }

    @Override
    public void extend(Parser.Builder parserBuilder) {
        parserBuilder.customDelimiterProcessor(new LabelDelimiterProcessor());
    }

    @Override
    public void extend(HtmlRenderer.Builder rendererBuilder, String rendererType) {
        switch (rendererType) {
            case "HTML":
                rendererBuilder.nodeRendererFactory(new LabelNodeRenderer.Factory());
                break;
        }
    }
}
