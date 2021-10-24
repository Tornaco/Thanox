package github.tornaco.android.thanos.common;

public class CategoryIndex {
    public String pkgSetId;

    public CategoryIndex(String appSetId) {
        this.pkgSetId = appSetId;
    }

    public static CategoryIndex from(String id) {
        return new CategoryIndex(id);
    }
}
