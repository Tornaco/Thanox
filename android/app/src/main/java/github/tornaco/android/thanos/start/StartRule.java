package github.tornaco.android.thanos.start;

public class StartRule {
    private String raw;

    public StartRule(String raw) {
        this.raw = raw;
    }

    public static StartRuleBuilder builder() {
        return new StartRuleBuilder();
    }

    public String getRaw() {
        return this.raw;
    }

    public String toString() {
        return "StartRule(raw=" + this.getRaw() + ")";
    }

    public static class StartRuleBuilder {
        private String raw;

        StartRuleBuilder() {
        }

        public StartRule.StartRuleBuilder raw(String raw) {
            this.raw = raw;
            return this;
        }

        public StartRule build() {
            return new StartRule(raw);
        }

        public String toString() {
            return "StartRule.StartRuleBuilder(raw=" + this.raw + ")";
        }
    }
}
