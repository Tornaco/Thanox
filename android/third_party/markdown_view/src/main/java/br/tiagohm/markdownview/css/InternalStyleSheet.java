package br.tiagohm.markdownview.css;

import android.text.TextUtils;
import android.util.Log;

import java.util.LinkedHashMap;
import java.util.Map;

public class InternalStyleSheet implements StyleSheet {
    private static final String NO_MEDIA_QUERY = "NO_MEDIA_QUERY";
    private Map<String, Map<String, Map<String, String>>> mRules = new LinkedHashMap<>();
    private Map<String, String> mFontFaces = new LinkedHashMap<>();
    private String currentMediaQuery;

    public InternalStyleSheet() {
        currentMediaQuery = NO_MEDIA_QUERY;
        mRules.put(currentMediaQuery, new LinkedHashMap<String, Map<String, String>>());
        //Estilos padrões.
        //Alinhamento de Texto
        addRule("p", "text-align: left");
        addRule(".text-left", "text-align: left");
        addRule(".text-right", "text-align: right");
        addRule(".text-center", "text-align: center");
        addRule(".text-justify", "text-align: justify");
        //Cores.
        addRule("red, .red", "color: #f44336");
        addRule("pink, .pink", "color: #E91E63");
        addRule("purple, .purple", "color: #9C27B0");
        addRule("deeppurple, .deeppurple", "color: #673AB7");
        addRule("indigo, .indigo", "color: #3F51B5");
        addRule("blue, .blue", "color: #2196F3");
        addRule("lightblue, .lightblue", "color: #03A9F4");
        addRule("cyan, .cyan", "color: #00BCD4");
        addRule("teal, .teal", "color: #009688");
        addRule("green, .green", "color: #4CAF50");
        addRule("lightgreen, .lightgreen", "color: #8BC34A");
        addRule("lime, .lime", "color: #CDDC39");
        addRule("yellow, .yellow", "color: #FFEB3B");
        addRule("amber, .amber", "color: #FFC107");
        addRule("orange, .orange", "color: #FF9800");
        addRule("deeporange, .deeporange", "color: #FF5722");
        addRule("brown, .brown", "color: #795548");
        addRule("grey, .grey", "color: #9E9E9E");
        addRule("bluegrey, .bluegrey", "color: #607D8B");
        //Tamanho de texto.
        addRule("smaller, .text-smaller", "font-size: smaller");
        addRule("small, .text-small", "font-size: small");
        addRule("medium, .text-medium", "font-size: medium");
        addRule("large, .text-large", "font-size: large");
        addRule("larger, .text-larger", "font-size: larger");
        addRule("x-small, .text-x-small", "font-size: x-small");
        addRule("x-large, .text-x-large", "font-size: x-large");
        addRule("xx-small, .text-xx-small", "font-size: xx-small");
        addRule("xx-large, .text-xx-large", "font-size: xx-large");
        //Botão voltar ao topo.
        addRule("body", "margin-bottom: 50px !important");
        addRule(".scrollup", "width: 55px", "height: 55px", "position: fixed", "bottom: 15px", "right: 15px", "visibility: hidden", "display: flex", "align-items: center", "justify-content: center", "margin: 0 !important", "line-height: 70px", "box-shadow: 0 0 4px rgba(0, 0, 0, 0.14), 0 4px 8px rgba(0, 0, 0, 0.28)", "border-radius: 50%", "color: #fff", "padding: 5px");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> faces : mFontFaces.entrySet()) {
            sb.append("@font-face {");
            sb.append(faces.getValue());
            sb.append("}\n");
        }

        for (Map.Entry<String, Map<String, Map<String, String>>> q : mRules.entrySet()) {
            if (!q.getKey().equals(NO_MEDIA_QUERY)) {
                sb.append("@media ");
                sb.append(q.getKey());
                sb.append(" {\n");
            }

            for (Map.Entry<String, Map<String, String>> e : q.getValue().entrySet()) {
                sb.append(e.getKey());
                sb.append(" {");
                for (Map.Entry<String, String> declaration : e.getValue().entrySet()) {
                    sb.append(declaration.getKey());
                    sb.append(":");
                    sb.append(declaration.getValue());
                    sb.append(";");
                }
                sb.append("}\n");
            }

            if (!q.getKey().equals(NO_MEDIA_QUERY)) {
                sb.append("}\n");
            }
        }

        return sb.toString();
    }

    @Override
    public String toHTML() {
        return "<style>\n" + toString() + "\n</style>\n";
    }

    private Map<String, Map<String, String>> getCurrentMediaQuery() {
        return mRules.get(currentMediaQuery);
    }

    public void addMedia(String mediaQuery) {
        if (mediaQuery != null && mediaQuery.trim().length() > 0) {
            mediaQuery = mediaQuery.trim();

            if (!mRules.containsKey(mediaQuery)) {
                mRules.put(mediaQuery, new LinkedHashMap<String, Map<String, String>>());
                currentMediaQuery = mediaQuery;
            }
        }
    }

    public void addFontFace(String fontFamily, String fontStretch, String fontStyle, String fontWeight,
                            String... src) {
        if (!TextUtils.isEmpty(fontFamily) && src.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("font-family:").append(fontFamily).append(";");
            sb.append("font-stretch:").append(TextUtils.isEmpty(fontStretch) ? "normal" : fontStretch).append(";");
            sb.append("font-style:").append(TextUtils.isEmpty(fontStyle) ? "normal" : fontStyle).append(";");
            sb.append("font-weight:").append(TextUtils.isEmpty(fontWeight) ? "normal" : fontWeight).append(";");
            sb.append("src:");
            for (int i = 0; i < src.length; i++) {
                sb.append(src[i]);
                if (i < src.length - 1) sb.append(",");
            }
            sb.append(";");
            mFontFaces.put(fontFamily.trim(), sb.toString());
        }
    }

    public void endMedia() {
        currentMediaQuery = NO_MEDIA_QUERY;
    }

    public void addRule(String selector, String... declarations) {
        if (selector != null && selector.trim().length() > 0 && declarations.length > 0) {
            selector = selector.trim();

            if (!getCurrentMediaQuery().containsKey(selector)) {
                getCurrentMediaQuery().put(selector, new LinkedHashMap<String, String>());
            }

            for (String declaration : declarations) {
                //String vazia.
                if (declaration == null || declaration.trim().length() <= 0) continue;

                String[] nameAndValue = declaration.trim().split(":");

                if (nameAndValue.length == 2) {
                    String name = nameAndValue[0].trim();
                    String value = nameAndValue[1].trim();
                    getCurrentMediaQuery().get(selector).put(name, value);
                } else {
                    Log.e("Markdown", "invalid css: '" + declaration + "' in selector: " + selector);
                }
            }
        }
    }

    public void removeRule(String selector) {
        getCurrentMediaQuery().remove(selector);
    }

    public void removeDeclaration(String selector, String declarationName) {
        if (!TextUtils.isEmpty(selector) && getCurrentMediaQuery().containsKey(selector)) {
            getCurrentMediaQuery().get(selector).remove(declarationName);
        }
    }

    public void replaceDeclaration(String selector, String declarationName, String newDeclarationValue) {
        if (!TextUtils.isEmpty(selector) && !TextUtils.isEmpty(declarationName)) {
            if (getCurrentMediaQuery().containsKey(selector) && getCurrentMediaQuery().get(selector).containsKey(declarationName)) {
                getCurrentMediaQuery().get(selector).put(declarationName, newDeclarationValue);
            }
        }
    }
}
