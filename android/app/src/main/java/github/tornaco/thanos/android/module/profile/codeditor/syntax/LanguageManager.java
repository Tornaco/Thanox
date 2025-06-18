package github.tornaco.thanos.android.module.profile.codeditor.syntax;

import android.content.Context;

import com.amrdeveloper.codeview.Code;
import com.amrdeveloper.codeview.CodeView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import github.tornaco.thanos.android.module.profile.codeditor.syntax.JsonLanguage;
import github.tornaco.thanos.android.module.profile.codeditor.syntax.LanguageName;
import github.tornaco.thanos.android.module.profile.codeditor.syntax.ThemeName;

public class LanguageManager {

    private final Context context;
    private final CodeView codeView;

    public LanguageManager(Context context, CodeView codeView) {
        this.context = context;
        this.codeView = codeView;
    }

    public void applyTheme(github.tornaco.thanos.android.module.profile.codeditor.syntax.LanguageName language, ThemeName theme) {
        switch (theme) {
            case DYNAMIC:
                applyDynamicTheme(language);
                break;
        }
    }

    public String[] getLanguageKeywords(github.tornaco.thanos.android.module.profile.codeditor.syntax.LanguageName language) {
        switch (language) {
            case JSON:
                return github.tornaco.thanos.android.module.profile.codeditor.syntax.JsonLanguage.getKeywords(context);
            default:
                return new String[]{};
        }
    }

    public List<Code> getLanguageCodeList(github.tornaco.thanos.android.module.profile.codeditor.syntax.LanguageName language) {
        switch (language) {
            case JSON:
                return github.tornaco.thanos.android.module.profile.codeditor.syntax.JsonLanguage.getCodeList(context);
            default:
                return new ArrayList<>();
        }
    }

    public Set<Character> getLanguageIndentationStarts(github.tornaco.thanos.android.module.profile.codeditor.syntax.LanguageName language) {
        switch (language) {
            case JSON:
                return github.tornaco.thanos.android.module.profile.codeditor.syntax.JsonLanguage.getIndentationStarts();
            default:
                return new HashSet<>();
        }
    }

    public Set<Character> getLanguageIndentationEnds(github.tornaco.thanos.android.module.profile.codeditor.syntax.LanguageName language) {
        switch (language) {
            case JSON:
                return github.tornaco.thanos.android.module.profile.codeditor.syntax.JsonLanguage.getIndentationEnds();
            default:
                return new HashSet<>();
        }
    }

    public String getCommentStart(github.tornaco.thanos.android.module.profile.codeditor.syntax.LanguageName language) {
        switch (language) {
            case JSON:
                return github.tornaco.thanos.android.module.profile.codeditor.syntax.JsonLanguage.getCommentStart();
            default:
                return "";
        }
    }

    public String getCommentEnd(github.tornaco.thanos.android.module.profile.codeditor.syntax.LanguageName language) {
        switch (language) {
            case JSON:
                return github.tornaco.thanos.android.module.profile.codeditor.syntax.JsonLanguage.getCommentEnd();
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
