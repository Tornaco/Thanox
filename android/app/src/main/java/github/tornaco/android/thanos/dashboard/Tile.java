package github.tornaco.android.thanos.dashboard;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@ToString
@Getter
public class Tile {
    private int id;
    private String title;
    private String summary;
    private String category;
    @DrawableRes
    private int iconRes;
    @ColorRes
    private int themeColor;
    private boolean disabled;
    private Object payload;
    private boolean checkable;
    @Setter
    private boolean checked;
    private String badge1, badge2;

    private String requiredFeature;
}
