package github.tornaco.android.thanos.common;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AppLabelSearchFilterTestJ {

    private final AppLabelSearchFilter filter = new AppLabelSearchFilter();

    @Test
    public void givenKeywordAndLabelWithFullPinyin_whenMatches_thenReturnTrue() {
        String hanziLabel = "酷安";
        String keyword = "kuan";

        boolean match = filter.matches(hanziLabel, keyword);

        Assert.assertTrue(match);
    }
}
