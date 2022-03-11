package github.tornaco.android.thanos.core.plus;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class RSManager {
    IRS rs;

    @SneakyThrows
    public void bc(String code, String deviceId, ICallback cb) {
        rs.bc(code, deviceId, cb);
    }

    @SneakyThrows
    public void vb(String code, String deviceId, ICallback cb) {
        rs.vb(code, deviceId, cb);
    }

    @SneakyThrows
    public void sid(String deviceId) {
        rs.sid(deviceId);
    }
}
