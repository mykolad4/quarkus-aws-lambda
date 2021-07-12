package me.molka.lambda;

import io.quarkus.amazon.lambda.test.LambdaClient;
import io.quarkus.test.junit.QuarkusTest;
import me.molka.lambda.config.TestDatabaseConfig;
import me.molka.lambda.data.ModifierDto;
import me.molka.lambda.data.ModifierDtoRequest;
import me.molka.lambda.service.ModifierService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static me.molka.lambda.data.ModifierColumns.MOD_ID_COL;
import static me.molka.lambda.data.REQUEST_TYPE.CREATE_MODIFIER;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@QuarkusTest
@Testcontainers
@TestInstance(PER_CLASS)
public class ModifierServiceTest {

    @Inject
    ModifierService modifierService;
    @ConfigProperty(name="quarkus.dynamodb.endpoint-override")
    String endpointUrl;
    @Inject
    DynamoDbClient dynamoDbClient;
    @Inject
    TestDatabaseConfig testDatabaseConfig;
    GenericContainer dynamodbContainer;
    private static final Logger LOG = Logger.getLogger(ModifierServiceTest.class);

    @BeforeAll
    public void setup() throws URISyntaxException {
        URI endpoint = new URI(endpointUrl);
        dynamodbContainer = new FixedHostPortGenericContainer(testDatabaseConfig.getDockerImage())
                .withFixedExposedPort(endpoint.getPort(), testDatabaseConfig.getContainerPort())
                .withCommand(testDatabaseConfig.getDockerCommand());
        dynamodbContainer.start();
        CreateTableResponse response = dynamoDbClient.createTable(CreateTableRequest.builder()
                .tableName(testDatabaseConfig.getTable())
                .attributeDefinitions(AttributeDefinition.builder()
                        .attributeName(MOD_ID_COL)
                        .attributeType(ScalarAttributeType.S)
                        .build())
                .keySchema(KeySchemaElement.builder()
                        .attributeName(MOD_ID_COL)
                        .keyType(KeyType.HASH)
                        .build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(testDatabaseConfig.getReadCapacityUnits())
                        .writeCapacityUnits(testDatabaseConfig.getWriteCapacityUnits())
                        .build())
                .build());
        LOG.debug("Table creation status : "+response.tableDescription().tableStatus().toString());
    }

    @AfterAll
    public void cleanup() {
        Optional.ofNullable(dynamodbContainer)
                .ifPresent(GenericContainer::stop);
    }

    @Test
    public void addModifierTest(){
        ModifierDto modifier = new ModifierDto();
        modifier.setName("Modifier Name");
        modifier.setCost(55.5);
        modifier.setAtLeast(0);
        modifier.setAtMost(1);
        modifier.setIsDefault(true);
        modifier.setIsHidden(false);
        ModifierDto addedModifier = modifierService.addModifier(modifier);
        assert addedModifier.getId() != null;
    }

    @Test
    public void getModifierTest(){
        ModifierDto modifier = new ModifierDto();
        modifier.setName("Modifier Name");
        modifier.setCost(55.5);
        modifier.setAtLeast(0);
        modifier.setAtMost(1);
        modifier.setIsDefault(true);
        modifier.setIsHidden(false);
        ModifierDto addedModifier = modifierService.addModifier(modifier);

        Optional<ModifierDto> lookupResult = modifierService.getModifier(addedModifier.getId());
        assert lookupResult.isPresent();
        assert lookupResult.get().getId().equals(addedModifier.getId());
        assert lookupResult.get().getName().equals(modifier.getName());
    }

    @Test
    public void deleteModifierTest(){
        ModifierDto modifier = new ModifierDto();
        modifier.setName("Modifier Name");
        modifier.setCost(55.5);
        modifier.setAtLeast(0);
        modifier.setAtMost(1);
        modifier.setIsDefault(true);
        modifier.setIsHidden(false);
        ModifierDto addedModifier = modifierService.addModifier(modifier);

        modifierService.deleteModifier(addedModifier.getId());

        Optional<ModifierDto> lookupResult = modifierService.getModifier(addedModifier.getId());
        assertFalse(lookupResult.isPresent());
    }

    @Test
    public void getAllModifiers(){
        modifierService.getModifiers()
                .stream()
                .map(ModifierDto::getId)
                .forEach(modifierService::deleteModifier);

        ModifierDto modifier = new ModifierDto();
        modifier.setName("Modifier Name1");
        modifier.setCost(55.5);
        modifier.setAtLeast(0);
        modifier.setAtMost(1);
        modifier.setIsDefault(true);
        modifier.setIsHidden(false);
        modifierService.addModifier(modifier);
        modifier.setName("Modifier Name2");
        modifier.setCost(55.5);
        modifier.setAtLeast(0);
        modifier.setAtMost(1);
        modifier.setIsDefault(true);
        modifier.setIsHidden(false);
        modifierService.addModifier(modifier);

        List<ModifierDto> allTasks = modifierService.getModifiers();
        assertTrue(allTasks.size() == 2);
    }

    @Test
    public void testCreateAction(){
        String modifierName = "New Modifier";
        ModifierDtoRequest request = new ModifierDtoRequest();
        request.setAction(CREATE_MODIFIER.toString());
        request.setName(modifierName);
        request.setCost(55.5);
        request.setAtLeast(0);
        request.setAtMost(1);
        request.setIsDefault(true);
        request.setIsHidden(false);
        String outputJson = LambdaClient.invoke(String.class, request);
        assertThatJson(outputJson)
                .inPath("$.name")
                .isEqualTo(modifierName);
    }
}
