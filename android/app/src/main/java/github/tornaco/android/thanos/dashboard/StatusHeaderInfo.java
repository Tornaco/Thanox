package github.tornaco.android.thanos.dashboard;

public class StatusHeaderInfo {
    private int runningAppsCount;
    private int memUsagePercent;
    private String memAvailablePercentString;

    StatusHeaderInfo(int runningAppsCount, int memUsagePercent, String memAvailablePercentString) {
        this.runningAppsCount = runningAppsCount;
        this.memUsagePercent = memUsagePercent;
        this.memAvailablePercentString = memAvailablePercentString;
    }

    public static StatusHeaderInfoBuilder builder() {
        return new StatusHeaderInfoBuilder();
    }

    public int getRunningAppsCount() {
        return this.runningAppsCount;
    }

    public int getMemUsagePercent() {
        return this.memUsagePercent;
    }

    public String getMemAvailablePercentString() {
        return this.memAvailablePercentString;
    }

    public static class StatusHeaderInfoBuilder {
        private int runningAppsCount;
        private int memUsagePercent;
        private String memAvailablePercentString;

        StatusHeaderInfoBuilder() {
        }

        public StatusHeaderInfo.StatusHeaderInfoBuilder runningAppsCount(int runningAppsCount) {
            this.runningAppsCount = runningAppsCount;
            return this;
        }

        public StatusHeaderInfo.StatusHeaderInfoBuilder memUsagePercent(int memUsagePercent) {
            this.memUsagePercent = memUsagePercent;
            return this;
        }

        public StatusHeaderInfo.StatusHeaderInfoBuilder memAvailablePercentString(String memAvailablePercentString) {
            this.memAvailablePercentString = memAvailablePercentString;
            return this;
        }

        public StatusHeaderInfo build() {
            return new StatusHeaderInfo(runningAppsCount, memUsagePercent, memAvailablePercentString);
        }

        public String toString() {
            return "StatusHeaderInfo.StatusHeaderInfoBuilder(runningAppsCount=" + this.runningAppsCount + ", memUsagePercent=" + this.memUsagePercent + ", memAvailablePercentString=" + this.memAvailablePercentString + ")";
        }
    }
}
