package github.tornaco.android.thanos.common;

import android.text.TextUtils;

import com.elvishew.xlog.XLog;
import com.github.promeg.pinyinhelper.Pinyin;

import java.util.Arrays;
import java.util.stream.Collectors;

import lang3.StringUtils;

public class AppLabelSearchFilter {
    private static final String TOKEN_SPLITTER = "-";

    public boolean matches(String keyword, String appLabel) {
        if (TextUtils.isEmpty(keyword) || TextUtils.isEmpty(appLabel)) {
            return false;
        }

        if (StringUtils.containsIgnoreCase(appLabel, keyword)) {
            return true;
        }

        String fullPinyinWithToken = Pinyin.toPinyin(appLabel, TOKEN_SPLITTER);

        if (TextUtils.isEmpty(fullPinyinWithToken)) {
            return false;
        }

        String fullPinyin = fullPinyinWithToken.replace(TOKEN_SPLITTER, "");

        XLog.d("AppLabelSearchFilter fullPinyin: %s", fullPinyin);

        if (StringUtils.containsIgnoreCase(fullPinyin, keyword)) {
            return true;
        }

        // Match first letter of full pinyin
        String firstLetterOfFullPinyin = Arrays.stream(fullPinyinWithToken.split(TOKEN_SPLITTER))
                .map(s -> String.valueOf(s.charAt(0)))
                .collect(Collectors.joining());
        XLog.d("AppLabelSearchFilter firstLetterOfFullPinyin: %s", firstLetterOfFullPinyin);
        return StringUtils.containsIgnoreCase(firstLetterOfFullPinyin, keyword);
    }
}
