package me.molka.lambda;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

//@QuarkusTest
//@Testcontainers
//@TestInstance(PER_CLASS)
public class ModifierServiceTest {

//    @Inject
//    ModifierService modifierService;
//    @ConfigProperty(name="quarkus.dynamodb.endpoint-override")
//    String endpointUrl;
//    @Inject
//    DynamoDbClient dynamoDbClient;
//    @Inject
//    TestDatabaseConfig testDatabaseConfig;
//    GenericContainer dynamodbContainer;
//    private static final Logger LOG = Logger.getLogger(ModifierServiceTest.class);

//    @BeforeAll
//    public void setup() throws URISyntaxException {
//        URI endpoint = new URI(endpointUrl);
//        dynamodbContainer = new FixedHostPortGenericContainer(testDatabaseConfig.getDockerImage())
//                .withFixedExposedPort(endpoint.getPort(), testDatabaseConfig.getContainerPort())
//                .withCommand(testDatabaseConfig.getDockerCommand());
//        dynamodbContainer.start();
//        CreateTableResponse response = dynamoDbClient.createTable(CreateTableRequest.builder()
//                .tableName(testDatabaseConfig.getTable())
//                .attributeDefinitions(AttributeDefinition.builder()
//                        .attributeName(ID_COL)
//                        .attributeType(ScalarAttributeType.S)
//                        .build())
//                .keySchema(KeySchemaElement.builder()
//                        .attributeName(ID_COL)
//                        .keyType(KeyType.HASH)
//                        .build())
//                .provisionedThroughput(ProvisionedThroughput.builder()
//                        .readCapacityUnits(testDatabaseConfig.getReadCapacityUnits())
//                        .writeCapacityUnits(testDatabaseConfig.getWriteCapacityUnits())
//                        .build())
//                .build());
//        LOG.debug("Table creation status : "+response.tableDescription().tableStatus().toString());
//    }

//    @AfterAll
//    public void cleanup() {
//        Optional.ofNullable(dynamodbContainer)
//                .ifPresent(GenericContainer::stop);
//    }

//    @Test
//    public void addModifierTest(){
//        ModifierDto modifier = new ModifierDto();
//        modifier.setName("Modifier Name");
//        modifier.setCost(55.5);
//        modifier.setAtLeast(0);
//        modifier.setAtMost(1);
//        modifier.setIsDefault(true);
//        modifier.setIsHidden(false);
//        ModifierDto addedModifier = modifierService.addModifier(modifier);
//        assert addedModifier.getId() != null;
//    }

//    @Test
//    public void getModifierTest(){
//        ModifierDto modifier = new ModifierDto();
//        modifier.setName("Modifier Name");
//        modifier.setCost(55.5);
//        modifier.setAtLeast(0);
//        modifier.setAtMost(1);
//        modifier.setIsDefault(true);
//        modifier.setIsHidden(false);
//        ModifierDto addedModifier = modifierService.addModifier(modifier);
//
//        Optional<ModifierDto> lookupResult = modifierService.getModifier(addedModifier.getId());
//        assert lookupResult.isPresent();
//        assert lookupResult.get().getId().equals(addedModifier.getId());
//        assert lookupResult.get().getName().equals(modifier.getName());
//    }
//
//    @Test
//    public void deleteModifierTest(){
//        ModifierDto modifier = new ModifierDto();
//        modifier.setName("Modifier Name");
//        modifier.setCost(55.5);
//        modifier.setAtLeast(0);
//        modifier.setAtMost(1);
//        modifier.setIsDefault(true);
//        modifier.setIsHidden(false);
//        ModifierDto addedModifier = modifierService.addModifier(modifier);
//
//        modifierService.deleteModifier(addedModifier.getId());
//
//        Optional<ModifierDto> lookupResult = modifierService.getModifier(addedModifier.getId());
//        assertFalse(lookupResult.isPresent());
//    }
//
//    @Test
//    public void getAllModifiers(){
//        modifierService.getModifiers()
//                .stream()
//                .map(ModifierDto::getId)
//                .forEach(modifierService::deleteModifier);
//
//        ModifierDto modifier = new ModifierDto();
//        modifier.setName("Modifier Name1");
//        modifier.setCost(55.5);
//        modifier.setAtLeast(0);
//        modifier.setAtMost(1);
//        modifier.setIsDefault(true);
//        modifier.setIsHidden(false);
//        modifierService.addModifier(modifier);
//        modifier.setName("Modifier Name2");
//        modifier.setCost(55.5);
//        modifier.setAtLeast(0);
//        modifier.setAtMost(1);
//        modifier.setIsDefault(true);
//        modifier.setIsHidden(false);
//        modifierService.addModifier(modifier);
//
//        List<ModifierDto> allTasks = modifierService.getModifiers();
//        assertTrue(allTasks.size() == 2);
//    }

//    @Test
//    public void testCreateAction(){
//        String modifierName = "New Modifier";
//        ModifierDtoRequest request = new ModifierDtoRequest();
//        request.setAction(CREATE_MODIFIER.toString());
//        request.setName(modifierName);
//        request.setCost(55.5);
//        request.setAtLeast(0);
//        request.setAtMost(1);
//        request.setIsDefault(true);
//        request.setIsHidden(false);
//        String outputJson = LambdaClient.invoke(String.class, request);
//        assertThatJson(outputJson)
//                .inPath("$.name")
//                .isEqualTo(modifierName);
//    }
}
