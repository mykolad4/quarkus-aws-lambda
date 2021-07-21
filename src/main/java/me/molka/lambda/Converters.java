package me.molka.lambda;

import me.molka.lambda.data.ModifierDto;
import me.molka.lambda.data.ModifierGroupDto;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static me.molka.lambda.data.ModifierColumns.*;

public interface Converters {

    static Collection<ModifierGroupDto> convertItemsToGroupModifiers(List<Map<String, AttributeValue>> items, String merchant) {
        return items.stream()
                .map(item -> {
                    ModifierDto modifierDto = ModifierDto.builder()
                            .name(item.get(NAME_COL).s())
                            .cost(Double.valueOf(item.get(COST_COL).n()))
                            .atLeast(Integer.valueOf(item.get(AT_LEAST_COL).n()))
                            .atMost(Integer.valueOf(item.get(AT_MOST_COL).n()))
                            .isDefault(item.get(IS_DEFAULT_COL).bool())
                            .isHidden(item.get(IS_HIDDEN_COL).bool())
                            .build();

                    String[] id = item.get(ID_COL).s().split("#");
                    return ModifierGroupDto.builder()
                            .merchantName(merchant)
                            .productName(id[0])
                            .groupName(id[1])
                            .atLeast(Integer.valueOf(item.get(GROUP_AT_LEAST_COL).n()))
                            .atMost(Integer.valueOf(item.get(GROUP_AT_MOST_COL).n()))
                            .modifiers(new ArrayList<>(){{add(modifierDto);}})
                            .build();
                })
                .collect(Collectors.toMap(ModifierGroupDto::getGroupName, Function.identity(), (ModifierGroupDto dto1, ModifierGroupDto dto2) -> {
                    dto1.getModifiers().add(dto2.getModifiers().get(0));
                    return dto1;
                })).values();
    }
}
