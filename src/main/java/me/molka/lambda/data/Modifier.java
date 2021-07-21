package me.molka.lambda.data;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RegisterForReflection
public class Modifier {
    private String name;
    private Double cost;
    private Integer atLeast;
    private Integer atMost;
    private Boolean isDefault;
    private Boolean isHidden;
}
