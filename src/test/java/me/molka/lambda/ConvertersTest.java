package me.molka.lambda;

import me.molka.lambda.converter.Converters;
import me.molka.lambda.data.Modifier;
import me.molka.lambda.data.ModifierGroup;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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

        ModifierGroup expectedModifierGroup = ModifierGroup.builder()
                .merchantName("Komora")
                .productName("Latte")
                .groupName("Milk")
                .atLeast(0)
                .atMost(1)
                .build();

        List<Modifier> expectedModifiers = List.of(
                Modifier.builder()
                        .name("Coconut milk")
                        .cost(20.0)
                        .atLeast(0)
                        .atMost(1)
                        .isHidden(false)
                        .isDefault(false)
                        .build(),
                Modifier.builder()
                        .name("Milk 2%")
                        .cost(0.0)
                        .atLeast(0)
                        .atMost(1)
                        .isDefault(true)
                        .isHidden(false)
                        .build(),
                Modifier.builder()
                        .name("Lactose-free milk")
                        .cost(55.0)
                        .atLeast(0)
                        .atMost(1)
                        .isDefault(false)
                        .isHidden(false)
                        .build()
        );

        Collection<ModifierGroup> modifiers = Converters.fromItemsToDto(items, "Komora");

        assertThat(modifiers).contains(expectedModifierGroup);
        assertThat(modifiers.iterator().next().getModifiers()).containsExactlyElementsOf(expectedModifiers);
    }
}