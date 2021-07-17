package me.molka.lambda.data;

import lombok.Data;

import java.util.List;

@Data
public class ModifierGroupDto {
//    todo: check names are not null
    private String merchantName;
    private String productName;
    private String groupName;
    private Integer atLeast;
    private Integer atMost;
    private List<ModifierDto> modifiers;
}
