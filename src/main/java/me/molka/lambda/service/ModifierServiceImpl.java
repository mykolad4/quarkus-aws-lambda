package me.molka.lambda.service;

import lombok.Data;
import me.molka.lambda.config.DatabaseConfig;
import me.molka.lambda.data.ModifierGroup;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.utils.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

import static me.molka.lambda.converter.Converters.fromDtoToItems;
import static me.molka.lambda.converter.Converters.fromItemsToDto;

@Data
@ApplicationScoped
public class ModifierServiceImpl implements ModifierService {

    private final DatabaseConfig databaseConfig;
    private final DynamoDbClient dynamoDbClient;

    @Override
    public ModifierGroup addModifier(ModifierGroup modifierGroup) {
        List<Map<String, AttributeValue>> modifiers = fromDtoToItems(modifierGroup);

        modifiers.forEach(modifier -> dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(databaseConfig.getTable())
                .item(modifier)
                .build()));

        return modifierGroup;
    }

    @Override
    public Collection<ModifierGroup> getModifiersById(String merchant, String id) {
        Map<String, AttributeValue> attrValues = new HashMap<>() {{
            put(":merchantId", AttributeValue.builder().s(merchant).build());
            if (StringUtils.isNotBlank(id)) put(":id", AttributeValue.builder().s(id).build());
        }};
        String condition = StringUtils.isBlank(id)
                ? "merchant = :merchantId"
                : "merchant = :merchantId and begins_with(id, :id)";
        QueryRequest queryRequest = QueryRequest.builder()
                .tableName(databaseConfig.getTable())
                .keyConditionExpression(condition)
                .expressionAttributeValues(attrValues)
                .build();

        List<Map<String, AttributeValue>> items = dynamoDbClient.query(queryRequest).items();
        return fromItemsToDto(items, merchant);
    }
}
