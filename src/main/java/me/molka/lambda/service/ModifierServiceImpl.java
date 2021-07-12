package me.molka.lambda.service;

import lombok.Data;
import me.molka.lambda.config.DatabaseConfig;
import me.molka.lambda.data.ModifierDto;
import org.jboss.logging.Logger;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.stream.Collectors;

import static me.molka.lambda.data.ModifierColumns.*;

@Data
@ApplicationScoped
public class ModifierServiceImpl implements ModifierService {

    private final DatabaseConfig databaseConfig;
    private final DynamoDbClient dynamoDbClient;

    private static final Logger LOG = Logger.getLogger(ModifierServiceImpl.class);

    @Override
    public ModifierDto addModifier(ModifierDto modifier) {
//        todo: how to generate unique id in DynamoDb table?
        modifier.setId(UUID.randomUUID().toString());

//        todo: will ORM be faster or slower?
        Map<String, AttributeValue> item = Map.of(
                MOD_ID_COL, AttributeValue.builder().s(modifier.getId()).build(),
                MOD_NAME_COL, AttributeValue.builder().s(modifier.getName()).build(),
                MOD_COST_COL, AttributeValue.builder().n(String.valueOf(modifier.getCost())).build(),
                MOD_AT_LEAST_COL, AttributeValue.builder().n(String.valueOf(modifier.getAtLeast())).build(),
                MOD_AT_MOST_COL, AttributeValue.builder().n(String.valueOf(modifier.getAtMost())).build(),
                MOD_IS_DEFAULT_COL, AttributeValue.builder().bool(modifier.getIsDefault()).build(),
                MOD_IS_HIDDEN_COL, AttributeValue.builder().bool(modifier.getIsHidden()).build());

        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(databaseConfig.getTable())
                .item(item)
                .build());

        return modifier;
    }

    @Override
    public List<ModifierDto> getModifiers() {
        return dynamoDbClient.scan(ScanRequest.builder()
                .tableName(databaseConfig.getTable())
                .attributesToGet(MOD_ID_COL, MOD_NAME_COL, MOD_COST_COL, MOD_AT_LEAST_COL,
                        MOD_AT_MOST_COL, MOD_IS_DEFAULT_COL, MOD_IS_HIDDEN_COL)
                .build())
                .items()
                .stream()
                .map(ModifierDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ModifierDto> getModifier(String modifierId) {
        GetItemResponse response = dynamoDbClient.getItem(
                GetItemRequest.builder()
                        .tableName(databaseConfig.getTable())
                        .key(Map.of(MOD_ID_COL, AttributeValue.builder().s(modifierId).build()))
                        .build()
        );

        if (!response.hasItem()) {
            LOG.debug("Could not find modifier with id ->" + modifierId);
            return Optional.empty();
        }
        return Optional.of(ModifierDto.from(response.item()));
    }

    @Override
    public void deleteModifier(String modifierId) {
        dynamoDbClient.deleteItem(DeleteItemRequest.builder()
                .tableName(databaseConfig.getTable())
                .key(Map.of(MOD_ID_COL, AttributeValue.builder().s(modifierId).build()))
                .build());
    }
}
