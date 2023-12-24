/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package now.fortuitous.thanos.power;

public class StandbyRule {
    private final String raw;

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
