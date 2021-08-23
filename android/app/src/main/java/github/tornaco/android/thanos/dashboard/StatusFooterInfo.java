package github.tornaco.android.thanos.dashboard;

public class StatusFooterInfo {
    private String footerString1;

    StatusFooterInfo(String footerString1) {
        this.footerString1 = footerString1;
    }

    public static StatusFooterInfoBuilder builder() {
        return new StatusFooterInfoBuilder();
    }

    public String getFooterString1() {
        return this.footerString1;
    }

    public static class StatusFooterInfoBuilder {
        private String footerString1;

        StatusFooterInfoBuilder() {
        }

        public StatusFooterInfo.StatusFooterInfoBuilder footerString1(String footerString1) {
            this.footerString1 = footerString1;
            return this;
        }

        public StatusFooterInfo build() {
            return new StatusFooterInfo(footerString1);
        }

        public String toString() {
            return "StatusFooterInfo.StatusFooterInfoBuilder(footerString1=" + this.footerString1 + ")";
        }
    }
}
