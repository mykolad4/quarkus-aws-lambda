package me.molka.lambda.data;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

import static me.molka.lambda.data.ModifierColumns.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RegisterForReflection
public class ModifierDto {
//    todo: check name is not null
    private String name;
    private Double cost;
    private Integer atLeast;
    private Integer atMost;
    private Boolean isDefault;
    private Boolean isHidden;
}
