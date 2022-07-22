package github.tornaco.thanos.android.module.profile.codeditor.syntax;

import android.content.Context;

import com.amrdeveloper.codeview.Code;
import com.amrdeveloper.codeview.CodeView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LanguageManager {

    private final Context context;
    private final CodeView codeView;

    public LanguageManager(Context context, CodeView codeView) {
        this.context = context;
        this.codeView = codeView;
    }

    public void applyTheme(LanguageName language, ThemeName theme) {
        switch (theme) {
            case DYNAMIC:
                applyDynamicTheme(language);
                break;
        }
    }

    public String[] getLanguageKeywords(LanguageName language) {
        switch (language) {
            case JSON:
                return JsonLanguage.getKeywords(context);
            default:
                return new String[]{};
        }
    }

    public List<Code> getLanguageCodeList(LanguageName language) {
        switch (language) {
            case JSON:
                return JsonLanguage.getCodeList(context);
            default:
                return new ArrayList<>();
        }
    }

    public Set<Character> getLanguageIndentationStarts(LanguageName language) {
        switch (language) {
            case JSON:
                return JsonLanguage.getIndentationStarts();
            default:
                return new HashSet<>();
        }
    }

    public Set<Character> getLanguageIndentationEnds(LanguageName language) {
        switch (language) {
            case JSON:
                return JsonLanguage.getIndentationEnds();
            default:
                return new HashSet<>();
        }
    }

    public String getCommentStart(LanguageName language) {
        switch (language) {
            case JSON:
                return JsonLanguage.getCommentStart();
            default:
                return "";
        }
    }

    public String getCommentEnd(LanguageName language) {
        switch (language) {
            case JSON:
                return JsonLanguage.getCommentEnd();
            default:
                return "";
        }
    }

    private void applyDynamicTheme(LanguageName language) {
        switch (language) {
            case JSON:
                JsonLanguage.applyDynamicTheme(context, codeView);
                break;
        }
    }
}
