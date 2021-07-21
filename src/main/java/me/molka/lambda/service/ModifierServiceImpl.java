package me.molka.lambda.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.molka.lambda.config.DatabaseConfig;
import me.molka.lambda.data.ModifierDto;
import me.molka.lambda.data.ModifierGroupDto;
import org.jboss.logging.Logger;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static me.molka.lambda.Converters.convertItemsToGroupModifiers;
import static me.molka.lambda.data.ModifierColumns.*;

@Data
@ApplicationScoped
public class ModifierServiceImpl implements ModifierService {

    private final DatabaseConfig databaseConfig;
    private final DynamoDbClient dynamoDbClient;

    private static final Logger LOG = Logger.getLogger(ModifierServiceImpl.class);

    @Override
    public ModifierGroupDto addModifier(ModifierGroupDto modifierGroupDto) {
        List<Map<String, AttributeValue>> modifiers = buildModifierAttributes(modifierGroupDto);

        modifiers.forEach(modifier -> dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(databaseConfig.getTable())
                .item(modifier)
                .build()));

        return modifierGroupDto;
    }

    @Override
    public Collection<ModifierGroupDto> getModifiersByProduct(String merchant, String product) {
        QueryRequest queryRequest = QueryRequest.builder()
                .tableName(databaseConfig.getTable())
                .keyConditionExpression("merchant = :merchantId and begins_with(id, :productId)")
                .expressionAttributeValues(Map.of(
                        ":merchantId", AttributeValue.builder().s(merchant).build(),
                        ":productId", AttributeValue.builder().s(product).build()))
                .build();

        List<Map<String, AttributeValue>> items = dynamoDbClient.query(queryRequest).items();
        return convertItemsToGroupModifiers(items, merchant);
    }

    private List<Map<String, AttributeValue>> buildModifierAttributes(ModifierGroupDto group) {
        return group.getModifiers().stream()
                .map(modifierDto -> buildModifierAttribute(group, modifierDto))
                .collect(Collectors.toList());
    }

//        todo: think about using mapper!!! but mapper - its reflection
    private Map<String, AttributeValue> buildModifierAttribute(ModifierGroupDto group, ModifierDto modifier) {
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

    private Map<String, AttributeValue> buildGroupAttribute(ModifierGroupDto group) {
        return Map.of(
                MERCHANT_NAME_COL, AttributeValue.builder().s(group.getMerchantName()).build(),
                ID_COL, AttributeValue.builder().s(buildGroupId(group)).build(),
                NAME_COL, AttributeValue.builder().s(group.getGroupName()).build(),
                AT_LEAST_COL, AttributeValue.builder().n(String.valueOf(group.getAtLeast())).build(),
                AT_MOST_COL, AttributeValue.builder().n(String.valueOf(group.getAtMost())).build()
        );
    }

    private String buildGroupId(ModifierGroupDto dto) {
        return String.format("%s#%s",
                dto.getProductName(),
                dto.getGroupName());
    }

    private String buildModifierId(ModifierGroupDto dto, String modName) {
        return String.format("%s#%s#%s",
                dto.getProductName(),
                dto.getGroupName(),
                modName);
    }

//    @Override
//    public List<ModifierDto> getModifiers() {
//        return dynamoDbClient.scan(ScanRequest.builder()
//                .tableName(databaseConfig.getTable())
//                .attributesToGet(MOD_NAME_COL, MOD_COST_COL, MOD_AT_LEAST_COL,
//                        MOD_AT_MOST_COL, MOD_IS_DEFAULT_COL, MOD_IS_HIDDEN_COL)
//                .build())
//                .items()
//                .stream()
//                .map(ModifierDto::from)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public Optional<ModifierDto> getModifier(String modifierId) {
//        GetItemResponse response = dynamoDbClient.getItem(
//                GetItemRequest.builder()
//                        .tableName(databaseConfig.getTable())
//                        .key(Map.of(ID_COL, AttributeValue.builder().s(modifierId).build()))
//                        .build()
//        );
//
//        if (!response.hasItem()) {
//            LOG.debug("Could not find modifier with id ->" + modifierId);
//            return Optional.empty();
//        }
//        return Optional.of(ModifierDto.from(response.item()));
//    }
//
//    @Override
//    public void deleteModifier(String modifierId) {
//        dynamoDbClient.deleteItem(DeleteItemRequest.builder()
//                .tableName(databaseConfig.getTable())
//                .key(Map.of(ID_COL, AttributeValue.builder().s(modifierId).build()))
//                .build());
//    }
}
