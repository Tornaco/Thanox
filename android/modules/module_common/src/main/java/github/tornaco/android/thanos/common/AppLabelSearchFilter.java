package github.tornaco.android.thanos.common;

import android.text.TextUtils;

import com.elvishew.xlog.XLog;

import java.util.Locale;
import java.util.stream.Collectors;

import lang3.StringUtils;
import lombok.val;
import util.HanziToPinyin;

public class AppLabelSearchFilter {

    public boolean matches(String keyword, String appLabel) {
        if (TextUtils.isEmpty(keyword) || TextUtils.isEmpty(appLabel)) {
            return false;
        }

        if (StringUtils.containsIgnoreCase(appLabel, keyword)) {
            return true;
        }

        val tokens = HanziToPinyin.getInstance().get(appLabel);
        if (tokens == null || tokens.isEmpty()) {
            return false;
        }

        // Match full pinyin
        String fullPinyin = tokens
                .stream()
                .map(token -> token.target)
                .collect(Collectors.joining())
                .toLowerCase(Locale.US);

        XLog.d("AppLabelSearchFilter fullPinyin: %s", fullPinyin);

        if (StringUtils.containsIgnoreCase(fullPinyin, keyword)) {
            return true;
        }

        // Match first letter of full pinyin
        String firstLetterOfFullPinyin = tokens
                .stream()
                .map(token -> String.valueOf(token.target.charAt(0)))
                .collect(Collectors.joining())
                .toLowerCase(Locale.US);
        XLog.d("AppLabelSearchFilter firstLetterOfFullPinyin: %s", firstLetterOfFullPinyin);
        return StringUtils.containsIgnoreCase(firstLetterOfFullPinyin, keyword);
    }
}
