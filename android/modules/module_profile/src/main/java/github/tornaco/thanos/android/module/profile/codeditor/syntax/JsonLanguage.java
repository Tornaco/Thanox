package github.tornaco.thanos.android.module.profile.codeditor.syntax;

import android.content.Context;
import android.content.res.Resources;

import com.amrdeveloper.codeview.Code;
import com.amrdeveloper.codeview.CodeView;
import com.amrdeveloper.codeview.Keyword;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import github.tornaco.thanos.android.module.profile.R;

public class JsonLanguage {

    // Language Keywords
    private static final Pattern PATTERN_KEYWORDS = Pattern.compile("\\b(abstract|boolean|break|byte|case|catch" +
            "|char|class|continue|default|do|double|else" +
            "|enum|extends|final|finally|float|for|if" +
            "|implements|import|instanceof|int|interface" +
            "|long|native|new|null|package|private|protected" +
            "|public|return|short|static|strictfp|super|switch" +
            "|synchronized|this|throw|transient|try|void|volatile|while)\\b");

    private static final Pattern PATTERN_HANDLE = Pattern.compile("\\b(ui|thanox|thanos|context|log|audio|killer|activity|power" +
            "|task|data|input|hw|io|ringtone|sh|su|pkg|battery|actor)\\b");

    private static final Pattern PATTERN_BUILTINS = Pattern.compile("[,:;[->]{}()]");
    private static final Pattern PATTERN_SINGLE_LINE_COMMENT = Pattern.compile("//[^\\n]*");
    private static final Pattern PATTERN_MULTI_LINE_COMMENT = Pattern.compile("/\\*[^*]*\\*+(?:[^/*][^*]*\\*+)*/");
    private static final Pattern PATTERN_ATTRIBUTE = Pattern.compile("\\.[a-zA-Z0-9_]+");
    private static final Pattern PATTERN_OPERATION = Pattern.compile(":|==|>|<|!=|>=|<=|->|=|>|<|%|-|-=|%=|\\+|\\-|\\-=|\\+=|\\^|\\&|\\|::|\\?|\\*");
    private static final Pattern PATTERN_GENERIC = Pattern.compile("<[a-zA-Z0-9,<>]+>");
    private static final Pattern PATTERN_ANNOTATION = Pattern.compile("@.[a-zA-Z0-9]+");
    private static final Pattern PATTERN_TODO_COMMENT = Pattern.compile("//TODO[^\n]*");
    private static final Pattern PATTERN_NUMBERS = Pattern.compile("\\b(\\d*[.]?\\d+)\\b");
    private static final Pattern PATTERN_CHAR = Pattern.compile("['](.*?)[']");
    private static final Pattern PATTERN_STRING = Pattern.compile("[\"](.*?)[\"]");
    private static final Pattern PATTERN_HEX = Pattern.compile("0x[0-9a-fA-F]+");
    private static final Pattern PATTERN_JSON_KEY = Pattern.compile("[a-zA-Z0-9\"_-]+?(?=:)");

    public static void applyDynamicTheme(Context context, CodeView codeView) {
        codeView.resetSyntaxPatternList();
        codeView.resetHighlighter();

        Resources resources = context.getResources();
        // Syntax Colors
        codeView.addSyntaxPattern(PATTERN_HEX, resources.getColor(github.tornaco.android.thanos.module.common.R.color.md_teal_600, null));
        codeView.addSyntaxPattern(PATTERN_NUMBERS, resources.getColor(github.tornaco.android.thanos.module.common.R.color.md_teal_600, null));
        codeView.addSyntaxPattern(PATTERN_KEYWORDS, resources.getColor(github.tornaco.android.thanos.module.common.R.color.md_green_700, null));
        codeView.addSyntaxPattern(PATTERN_HANDLE, resources.getColor(github.tornaco.android.thanos.module.common.R.color.md_red_600, null));
        codeView.addSyntaxPattern(PATTERN_BUILTINS, resources.getColor(github.tornaco.android.thanos.module.common.R.color.md_indigo_300, null));
        codeView.addSyntaxPattern(PATTERN_ATTRIBUTE, resources.getColor(github.tornaco.android.thanos.module.common.R.color.md_indigo_300, null));
        codeView.addSyntaxPattern(PATTERN_OPERATION, resources.getColor(github.tornaco.android.thanos.module.common.R.color.md_cyan_800, null));
        codeView.addSyntaxPattern(PATTERN_JSON_KEY, resources.getColor(github.tornaco.android.thanos.module.common.R.color.md_amber_700, null));

        // Default Color
        codeView.setTextColor(resources.getColor(github.tornaco.android.thanos.module.common.R.color.md_indigo_300));
        codeView.reHighlightSyntax();
    }

    public static String[] getKeywords(Context context) {
        return context.getResources().getStringArray(R.array.java_keywords);
    }

    public static List<Code> getCodeList(Context context) {
        List<Code> codeList = new ArrayList<>();
        String[] keywords = getKeywords(context);
        for (String keyword : keywords) {
            codeList.add(new Keyword(keyword));
        }
        return codeList;
    }

    public static Set<Character> getIndentationStarts() {
        Set<Character> characterSet = new HashSet<>();
        characterSet.add('{');
        return characterSet;
    }

    public static Set<Character> getIndentationEnds() {
        Set<Character> characterSet = new HashSet<>();
        characterSet.add('}');
        return characterSet;
    }

    public static String getCommentStart() {
        return "//";
    }

    public static String getCommentEnd() {
        return "";
    }
}
