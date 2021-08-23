package github.tornaco.android.thanos.power;

public class StandbyRule {
    private String raw;

    public StandbyRule(String raw) {
        this.raw = raw;
    }

    public static StandbyRuleBuilder builder() {
        return new StandbyRuleBuilder();
    }

    public String getRaw() {
        return this.raw;
    }

    public String toString() {
        return "StandbyRule(raw=" + this.getRaw() + ")";
    }

    public static class StandbyRuleBuilder {
        private String raw;

        StandbyRuleBuilder() {
        }

        public StandbyRule.StandbyRuleBuilder raw(String raw) {
            this.raw = raw;
            return this;
        }

        public StandbyRule build() {
            return new StandbyRule(raw);
        }

        public String toString() {
            return "StandbyRule.StandbyRuleBuilder(raw=" + this.raw + ")";
        }
    }
}
