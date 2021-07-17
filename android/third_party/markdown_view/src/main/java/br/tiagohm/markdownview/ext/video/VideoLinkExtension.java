package br.tiagohm.markdownview.ext.video;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataHolder;

import br.tiagohm.markdownview.ext.video.internal.VideoLinkNodePostProcessor;
import br.tiagohm.markdownview.ext.video.internal.VideoLinkNodeRenderer;

public class VideoLinkExtension implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension {
    private VideoLinkExtension() {
    }

    public static Extension create() {
        return new VideoLinkExtension();
    }

    @Override
    public void extend(Parser.Builder parserBuilder) {
        parserBuilder.postProcessorFactory(new VideoLinkNodePostProcessor.Factory(parserBuilder));
    }

    @Override
    public void rendererOptions(final MutableDataHolder options) {

    }

    @Override
    public void parserOptions(final MutableDataHolder options) {

    }

    @Override
    public void extend(HtmlRenderer.Builder rendererBuilder, String rendererType) {
        if (rendererType.equals("HTML")) {
            rendererBuilder.nodeRendererFactory(new VideoLinkNodeRenderer.Factory());
        }
    }
}
