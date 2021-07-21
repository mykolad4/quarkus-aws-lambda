package me.molka.lambda.converter;

import me.molka.lambda.data.Modifier;
import me.molka.lambda.data.ModifierGroup;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static me.molka.lambda.data.ModifierColumns.*;

public interface Converters {

    static Collection<ModifierGroup> fromItemsToDto(List<Map<String, AttributeValue>> items, String merchant) {
        return items.stream()
                .map(item -> {
                    Modifier modifier = Modifier.builder()
                            .name(item.get(NAME_COL).s())
                            .cost(Double.valueOf(item.get(COST_COL).n()))
                            .atLeast(Integer.valueOf(item.get(AT_LEAST_COL).n()))
                            .atMost(Integer.valueOf(item.get(AT_MOST_COL).n()))
                            .isDefault(item.get(IS_DEFAULT_COL).bool())
                            .isHidden(item.get(IS_HIDDEN_COL).bool())
                            .build();

                    String[] id = item.get(ID_COL).s().split("#");
                    return ModifierGroup.builder()
                            .merchantName(merchant)
                            .productName(id[0])
                            .groupName(id[1])
                            .atLeast(Integer.valueOf(item.get(GROUP_AT_LEAST_COL).n()))
                            .atMost(Integer.valueOf(item.get(GROUP_AT_MOST_COL).n()))
                            .modifiers(new ArrayList<>(){{add(modifier);}})
                            .build();
                })
                .collect(Collectors.toMap(ModifierGroup::getGroupName, Function.identity(), (ModifierGroup dto1, ModifierGroup dto2) -> {
                    dto1.getModifiers().add(dto2.getModifiers().get(0));
                    return dto1;
                })).values();
    }

    static List<Map<String, AttributeValue>> fromDtoToItems(ModifierGroup group) {
        return group.getModifiers().stream()
                .map(modifierDto -> buildItem(group, modifierDto))
                .collect(Collectors.toList());
    }

    static Map<String, AttributeValue> buildItem(ModifierGroup group, Modifier modifier) {
        return Map.of(
                MERCHANT_NAME_COL, AttributeValue.builder().s(group.getMerchantName()).build(),
                ID_COL, AttributeValue.builder().s(buildModifierId(group, modifier.getName())).build(),
                NAME_COL, AttributeValue.builder().s(modifier.getName()).build(),
                COST_COL, AttributeValue.builder().n(String.valueOf(modifier.getCost())).build(),
                GROUP_AT_LEAST_COL, AttributeValue.builder().n(String.valueOf(group.getAtLeast())).build(),
                GROUP_AT_MOST_COL, AttributeValue.builder().n(String.valueOf(group.getAtMost())).build(),
                AT_LEAST_COL, AttributeValue.builder().n(String.valueOf(modifier.getAtLeast())).build(),
                AT_MOST_COL, AttributeValue.builder().n(String.valueOf(modifier.getAtMost())).build(),
                IS_DEFAULT_COL, AttributeValue.builder().bool(modifier.getIsDefault()).build(),
                IS_HIDDEN_COL, AttributeValue.builder().bool(modifier.getIsHidden()).build()
        );
    }

    static String buildModifierId(ModifierGroup dto, String modName) {
        return String.format("%s#%s#%s",
                dto.getProductName(),
                dto.getGroupName(),
                modName);
    }
}
