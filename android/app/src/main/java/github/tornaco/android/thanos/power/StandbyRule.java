package github.tornaco.android.thanos.power;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class StandbyRule {
    private String raw;
}
