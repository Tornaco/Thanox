package br.tiagohm.markdownview.ext.emoji;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.DataKey;
import com.vladsch.flexmark.util.options.MutableDataHolder;

import br.tiagohm.markdownview.ext.emoji.internal.EmojiDelimiterProcessor;
import br.tiagohm.markdownview.ext.emoji.internal.EmojiNodeRenderer;

/**
 * Extension for emoji shortcuts using EmojiOne.
 */
public class EmojiExtension implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension {
    public static final DataKey<String> ATTR_ALIGN = new DataKey<>("ATTR_ALIGN", "absmiddle");
    public static final DataKey<String> ATTR_IMAGE_SIZE = new DataKey<>("ATTR_IMAGE_SIZE", "20");
    public static final DataKey<String> ROOT_IMAGE_PATH = new DataKey<>("ROOT_IMAGE_PATH", "file:///android_asset/svg/");
    public static final DataKey<String> IMAGE_EXT = new DataKey<>("IMAGE_EXT", "svg");

    private EmojiExtension() {
    }

    public static Extension create() {
        return new EmojiExtension();
    }

    @Override
    public void rendererOptions(final MutableDataHolder options) {

    }

    @Override
    public void parserOptions(final MutableDataHolder options) {

    }

    @Override
    public void extend(Parser.Builder parserBuilder) {
        parserBuilder.customDelimiterProcessor(new EmojiDelimiterProcessor());
    }

    @Override
    public void extend(HtmlRenderer.Builder rendererBuilder, String rendererType) {
        if (rendererType.equals("HTML")) {
            rendererBuilder.nodeRendererFactory(new EmojiNodeRenderer.Factory());
        }
    }
}
