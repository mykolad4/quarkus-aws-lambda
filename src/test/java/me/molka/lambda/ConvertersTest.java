package me.molka.lambda;

import me.molka.lambda.data.ModifierDto;
import me.molka.lambda.data.ModifierGroupDto;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ConvertersTest {

    @Test
    void convertItemsToGroupModifiers() {
        List<Map<String, AttributeValue>> items = List.of(
                Map.of(
                        "merchant", AttributeValue.builder().s("Komora").build(),
                        "id", AttributeValue.builder().s("Latte#Milk#Coconut milk").build(),
                        "atLeast", AttributeValue.builder().n("0").build(),
                        "atMost", AttributeValue.builder().n("1").build(),
                        "cost", AttributeValue.builder().n("20.0").build(),
                        "groupAtLeast", AttributeValue.builder().n("0").build(),
                        "groupAtMost", AttributeValue.builder().n("1").build(),
                        "isDefault", AttributeValue.builder().bool(false).build(),
                        "isHidden", AttributeValue.builder().bool(false).build(),
                        "nameCol", AttributeValue.builder().s("Coconut milk").build()
                ),
                Map.of(
                        "merchant", AttributeValue.builder().s("Komora").build(),
                        "id", AttributeValue.builder().s("Latte#Milk#Milk 2%").build(),
                        "atLeast", AttributeValue.builder().n("0").build(),
                        "atMost", AttributeValue.builder().n("1").build(),
                        "cost", AttributeValue.builder().n("0.0").build(),
                        "groupAtLeast", AttributeValue.builder().n("0").build(),
                        "groupAtMost", AttributeValue.builder().n("1").build(),
                        "isDefault", AttributeValue.builder().bool(true).build(),
                        "isHidden", AttributeValue.builder().bool(false).build(),
                        "nameCol", AttributeValue.builder().s("Milk 2%").build()
                ),
                Map.of(
                        "merchant", AttributeValue.builder().s("Komora").build(),
                        "id", AttributeValue.builder().s("Latte#Milk#Lactose-free milk").build(),
                        "atLeast", AttributeValue.builder().n("0").build(),
                        "atMost", AttributeValue.builder().n("1").build(),
                        "cost", AttributeValue.builder().n("55.0").build(),
                        "groupAtLeast", AttributeValue.builder().n("0").build(),
                        "groupAtMost", AttributeValue.builder().n("1").build(),
                        "isDefault", AttributeValue.builder().bool(false).build(),
                        "isHidden", AttributeValue.builder().bool(false).build(),
                        "nameCol", AttributeValue.builder().s("Lactose-free milk").build()
                )
                );

        ModifierGroupDto expectedModifierGroup = ModifierGroupDto.builder()
                .merchantName("Komora")
                .productName("Latte")
                .groupName("Milk")
                .atLeast(0)
                .atMost(1)
                .build();

        List<ModifierDto> expectedModifiers = List.of(
                ModifierDto.builder()
                        .name("Coconut milk")
                        .cost(20.0)
                        .atLeast(0)
                        .atMost(1)
                        .isHidden(false)
                        .isDefault(false)
                        .build(),
                ModifierDto.builder()
                        .name("Milk 2%")
                        .cost(0.0)
                        .atLeast(0)
                        .atMost(1)
                        .isDefault(true)
                        .isHidden(false)
                        .build(),
                ModifierDto.builder()
                        .name("Lactose-free milk")
                        .cost(55.0)
                        .atLeast(0)
                        .atMost(1)
                        .isDefault(false)
                        .isHidden(false)
                        .build()
        );

        Collection<ModifierGroupDto> modifiers = Converters.convertItemsToGroupModifiers(items, "Komora");

        assertThat(modifiers).contains(expectedModifierGroup);
        assertThat(modifiers.iterator().next().getModifiers()).containsExactlyElementsOf(expectedModifiers);
    }
}