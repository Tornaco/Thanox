package github.tornaco.android.thanos.core.process;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

import util.ObjectsUtils;

public class ProcessRecord implements Parcelable {

    private final String packageName;
    private final String processName;
    private final long pid;
    private final int uid;
    boolean notResponding;
    boolean crashing;

    protected ProcessRecord(Parcel in) {
        packageName = in.readString();
        processName = in.readString();
        pid = in.readLong();
        uid = in.readInt();
        notResponding = in.readInt() != 0;
        crashing = in.readInt() != 0;
    }

    public static final Creator<ProcessRecord> CREATOR = new Creator<ProcessRecord>() {
        @Override
        public ProcessRecord createFromParcel(Parcel in) {
            return new ProcessRecord(in);
        }

        @Override
        public ProcessRecord[] newArray(int size) {
            return new ProcessRecord[size];
        }
    };

    public ProcessRecord(String packageName, String processName, long pid, int uid, boolean notResponding, boolean crashing) {
        this.packageName = packageName;
        this.processName = processName;
        this.pid = pid;
        this.uid = uid;
        this.notResponding = notResponding;
        this.crashing = crashing;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(packageName);
        parcel.writeString(processName);
        parcel.writeLong(pid);
        parcel.writeInt(uid);
        parcel.writeInt(notResponding ? 1 : 0);
        parcel.writeInt(crashing ? 1 : 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProcessRecord that = (ProcessRecord) o;
        return ObjectsUtils.equals(packageName, that.packageName) &&
                ObjectsUtils.equals(processName, that.processName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packageName, processName);
    }

    public boolean isMainProcess() {
        return ObjectsUtils.equals(packageName, processName);
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getProcessName() {
        return this.processName;
    }

    public long getPid() {
        return this.pid;
    }

    public int getUid() {
        return this.uid;
    }

    public boolean isNotResponding() {
        return this.notResponding;
    }

    public boolean isCrashing() {
        return this.crashing;
    }

    @Override
    public String toString() {
        return "ProcessRecord{" +
                "packageName='" + packageName + '\'' +
                ", processName='" + processName + '\'' +
                ", pid=" + pid +
                ", uid=" + uid +
                '}';
    }
}
