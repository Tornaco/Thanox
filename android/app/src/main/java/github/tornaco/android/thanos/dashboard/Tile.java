package github.tornaco.android.thanos.dashboard;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

public class Tile {
    private final int id;
    private final String title;
    private final String summary;
    private final String warning;
    private final String category;
    @DrawableRes
    private final int iconRes;
    @ColorRes
    private final int themeColor;
    private final boolean disabled;
    private final Object payload;
    private final boolean checkable;
    private boolean checked;
    private final String badge1;
    private final String badge2;

    private final String requiredFeature;

    public Tile(int id, String title, String summary, String warning,
                String category, int iconRes, int themeColor, boolean disabled,
                Object payload, boolean checkable, boolean checked, String badge1, String badge2, String requiredFeature) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.warning = warning;
        this.category = category;
        this.iconRes = iconRes;
        this.themeColor = themeColor;
        this.disabled = disabled;
        this.payload = payload;
        this.checkable = checkable;
        this.checked = checked;
        this.badge1 = badge1;
        this.badge2 = badge2;
        this.requiredFeature = requiredFeature;
    }

    public static TileBuilder builder() {
        return new TileBuilder();
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getSummary() {
        return this.summary;
    }

    public String getWarning() {
        return warning;
    }

    public String getCategory() {
        return this.category;
    }

    public int getIconRes() {
        return this.iconRes;
    }

    public int getThemeColor() {
        return this.themeColor;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public Object getPayload() {
        return this.payload;
    }

    public boolean isCheckable() {
        return this.checkable;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public String getBadge1() {
        return this.badge1;
    }

    public String getBadge2() {
        return this.badge2;
    }

    public String getRequiredFeature() {
        return this.requiredFeature;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public static class TileBuilder {
        private int id;
        private String title;
        private String summary;
        private String warning;
        private String category;
        private int iconRes;
        private int themeColor;
        private boolean disabled;
        private Object payload;
        private boolean checkable;
        private boolean checked;
        private String badge1;
        private String badge2;
        private String requiredFeature;

        TileBuilder() {
        }

        public Tile.TileBuilder id(int id) {
            this.id = id;
            return this;
        }

        public Tile.TileBuilder title(String title) {
            this.title = title;
            return this;
        }

        public Tile.TileBuilder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public Tile.TileBuilder warning(String warning) {
            this.warning = warning;
            return this;
        }

        public Tile.TileBuilder category(String category) {
            this.category = category;
            return this;
        }

        public Tile.TileBuilder iconRes(int iconRes) {
            this.iconRes = iconRes;
            return this;
        }

        public Tile.TileBuilder themeColor(int themeColor) {
            this.themeColor = themeColor;
            return this;
        }

        public Tile.TileBuilder disabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }

        public Tile.TileBuilder payload(Object payload) {
            this.payload = payload;
            return this;
        }

        public Tile.TileBuilder checkable(boolean checkable) {
            this.checkable = checkable;
            return this;
        }

        public Tile.TileBuilder checked(boolean checked) {
            this.checked = checked;
            return this;
        }

        public Tile.TileBuilder badge1(String badge1) {
            this.badge1 = badge1;
            return this;
        }

        public Tile.TileBuilder badge2(String badge2) {
            this.badge2 = badge2;
            return this;
        }

        public Tile.TileBuilder requiredFeature(String requiredFeature) {
            this.requiredFeature = requiredFeature;
            return this;
        }

        public Tile build() {
            return new Tile(id, title, summary, warning, category, iconRes,
                    themeColor, disabled, payload, checkable, checked,
                    badge1, badge2, requiredFeature);
        }
    }
}
