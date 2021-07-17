package me.molka.lambda.data;

import lombok.Data;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

import static me.molka.lambda.data.ModifierColumns.*;

//  todo should we use @Builder?
@Data
public class ModifierDto {
//    todo: check name is not null
    private String name;
    private Double cost;
    private Integer atLeast;
    private Integer atMost;
    private Boolean isDefault;
    private Boolean isHidden;

//    public static ModifierDto from(Map<String, AttributeValue> attributes) {
//        ModifierDto modifier = new ModifierDto();
//        modifier.setName(attributes.get(MOD_NAME_COL).s());
//        modifier.setCost(Double.valueOf(attributes.get(MOD_COST_COL).n()));
//        modifier.setAtLeast(Integer.valueOf(attributes.get(MOD_AT_LEAST_COL).n()));
//        modifier.setAtMost(Integer.valueOf(attributes.get(MOD_AT_MOST_COL).n()));
//        modifier.setIsDefault(attributes.get(MOD_IS_DEFAULT_COL).bool());
//        modifier.setIsHidden(attributes.get(MOD_IS_HIDDEN_COL).bool());
//        return modifier;
//    }
}
