package br.tiagohm.markdownview.css.styles;

import br.tiagohm.markdownview.css.InternalStyleSheet;

//Bootstrap based CSS
public abstract class Bootstrap extends InternalStyleSheet {
    public Bootstrap() {
        addRule("*", "box-sizing: border-box");
        addRule("body", "font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif", "font-size: 14px", "line-height: 1.42857143", "color: #333", "background-color: #fff", "margin: 0");
        addRule("h1", "font-size: 36px", "margin-top: 20px", "margin-bottom: 10px", "font-weight: 500", "line-height: 1.1");
        addRule("h2", "font-size: 30px", "margin-top: 20px", "margin-bottom: 10px", "font-weight: 500", "line-height: 1.1");
        addRule("h3", "font-size: 24px", "margin-top: 20px", "margin-bottom: 10px", "margin-top: 20px", "margin-bottom: 10px", "font-weight: 500", "line-height: 1.1");
        addRule("h4", "font-size: 18px", "margin-top: 10px", "margin-bottom: 10px", "font-weight: 500", "line-height: 1.1");
        addRule("h5", "font-size: 14px", "margin-top: 10px", "margin-bottom: 10px", "font-weight: 500", "line-height: 1.1");
        addRule("h6", "font-size: 12px", "margin-top: 10px", "margin-bottom: 10px", "font-weight: 500", "line-height: 1.1");
        addRule("hr", "margin-top: 20px", "margin-bottom: 20px", "border: 0", "border-top: 1px solid #eee");
        addRule("p", "margin: 0 0 10px");
        addRule("strong", "font-weight: 700");
        addRule("em", "font-style: italic");
        addRule("a", "color: #337ab7", "text-decoration: none", "word-wrap: break-word");
        addRule("img", "vertical-align: middle", "border: 0", "max-width: 100%");
        addRule("code", "padding: 2px 4px", "font-size: 90%", "color: #c7254e", "background-color: #f9f2f4", "white-space: pre-wrap", "border-radius: 4px", "font-family: Menlo,Monaco,Consolas,\"Courier New\",monospace", "word-wrap: break-word");
        addRule("pre code", "padding: 0", "white-wrap: pre-wrap", "white-space: pre", "background-color: transparent", "border-radius: 0");
        addRule("pre", "display: block", "padding: 9.5px", "margin: 0 0 10px", "font-size: 13px", "line-height: 1.42857143", "color: #333", "background-color: #f5f5f5", "border: 1px solid #ccc", "border-radius: 4px", "font-family: Menlo,Monaco,Consolas,\"Courier New\",monospace", "overflow: auto");
        addRule("blockquote", "padding: 0px 20px", "margin: 0 0 20px", "font-size: 14px", "border-left: 5px solid #eee");
        addRule("ul", "margin-top: 0", "margin-bottom: 10px");
        addRule("ol", "margin-top: 0", "margin-bottom: 10px");
        addRule("ol ol", "margim-bottom: 0");
        addRule("ol ul", "margim-bottom: 0");
        addRule("ul ol", "margim-bottom: 0");
        addRule("ul ul", "margim-bottom: 0");
        addRule("li", "word-wrap: break-word");
        addRule("table", "width: 100%", "background-color: transparent", "border-spacing: 0", "border-collapse: collapse");
        addRule("td", "padding: 1px");
        addRule("th", "padding: 1px");
        addRule("th[align=left]", "text-align: left");
        addRule("th[align=center]", "text-align: center");
        addRule("th[align=right]", "text-align: right");
        addRule("td[align=left]", "text-align: left");
        addRule("td[align=center]", "text-align: center");
        addRule("td[align=right]", "text-align: right");
        addRule("abbr", "border-bottom: 1px dotted #777");
        addRule("mark", "padding: 0.2em", "background-color: #fcf8e3");
        addRule("sub", "position: relative", "font-size: 75%", "line-height: 0", "vertical-align: baseline", "bottom: -0.25em");
        addRule("sup", "position: relative", "font-size: 75%", "line-height: 0", "vertical-align: baseline", "top: -0.5em");
        addRule("kbd", "padding: 2px 4px", "font-size: 90%", "color: #fff", "background-color: #333", "border-radius: 3px", "box-shadow: inset 0 -1px 0 rgba(0,0,0,0.25)", "font-family: Menlo, Monaco, Consolas, \"Courier New\", monospace");
        addRule("span.math", "color: inherit");
        addRule("lbl", "display: inline-block", "padding: 0 10px", "background: #1e87f0", "line-height: 1.5",
                "font-size: 12px", "color: #fff", "vertical-align: middle", "white-space: nowrap", "border-radius: 2px", "text-transform: uppercase;");
        addRule("button", "margin: 0", "overflow: visible", "display: inline-block", "padding: 0 30px",
                "vertical-align: middle", "font-size: 14px", "line-height: 38px", "text-align: center",
                "color: #222", "border: 1px solid #e5e5e5", "background-color: transparent");

        addRule(".lbl-success", "background-color: #32d296");
        addRule(".lbl-warning", "background-color: #faa05a");
        addRule(".lbl-danger", "background-color: #f0506e");
        //Class
        addRule(".container", "padding-right: 15px", "padding-left: 15px", "margin-right: auto", "margin-left: auto");
        addRule(".task-list-item", "list-style-type: none");
        addRule(".task-list-item-checkbox", "vertical-align: middle", "margin: 0em 0.2em 0.25em -1.6em");
        addRule(".footnotes p", "margin: 0");
        addRule(".footnotes li", "margin-top: 2px", "margin-bottom: 2px");
        addRule(".player", "position: relative", "padding-bottom: 56.25%", "padding-top: 25px", "height: 0");
        addRule(".player iframe", "position: absolute", "top: 0", "left: 0", "width: 100%", "height: 100%");
        addRule(".twitter-follow-button", "vertical-align: middle");
        addRule(".scrollup", "background-color: #24292e");
        //IDs

        //Highlight.js
        addRule(".hljs", "display: block", "overflow-x: auto", "color: #4d4d4c", "padding: 0.5em");
        addRule(".hljs-type", "color: #880000");
        addRule(".hljs-params", "color: #880000");
        addRule(".hljs-string", "color: #880000");
        addRule(".hljs-number", "color: #880000");
        addRule(".hljs-selector-id", "color: #880000");
        addRule(".hljs-selector-class", "color: #880000");
        addRule(".hljs-comment", "color: #8e908c");
        addRule(".hljs-quote", "color: #8e908c");
        addRule(".hljs-template-tag", "color: #880000");
        addRule(".hljs-tag", "color: #880000");
        addRule(".hljs-name", "color: #880000");
        addRule(".hljs-deletion", "color: #880000");
        addRule(".hljs-title", "color: #880000", "font-weight: bold");
        addRule(".hljs-section", "color: #880000", "font-weight: bold");
        addRule(".hljs-regexp", "color: #BC6060");
        addRule(".hljs-symbol", "color: #BC6060");
        addRule(".hljs-variable", "color: #BC6060");
        addRule(".hljs-template-variable", "color: #BC6060");
        addRule(".hljs-link", "color: #BC6060");
        addRule(".hljs-attribute", "color: #eab700");
        addRule(".hljs-selector-attr", "color: #BC6060");
        addRule(".hljs-selector-pseudo", "color: #BC6060");
        addRule(".hljs-literal", "color: #78A960");
        addRule(".hljs-built_in", "color: #397300");
        addRule(".hljs-builtin-name", "color: #397300");
        addRule(".hljs-bullet", "color: #397300");
        addRule(".hljs-code", "color: #397300");
        addRule(".hljs-addition", "color: #397300");
        addRule(".hljs-meta", "color: #1f7199");
        addRule(".hljs-meta-string", "color: #4d99bf");
        addRule(".hljs-keyword", "color: #000000");
        addRule(".hljs-selector-tag", "color: #000000");
        addRule(".hljs-emphasis", "font-style: italic");
        addRule(".hljs-strong", "font-weight: bold");
    }
}
