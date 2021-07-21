package me.molka.lambda.data;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "modifiers")
@RegisterForReflection
public class ModifierGroupDto {
//    todo: check names are not null
    private String merchantName;
    private String productName;
    private String groupName;
    private Integer atLeast;
    private Integer atMost;
    @Builder.Default
    private List<ModifierDto> modifiers = new ArrayList<>();
}
