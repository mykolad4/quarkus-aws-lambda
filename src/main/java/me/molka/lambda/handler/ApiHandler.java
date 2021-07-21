package me.molka.lambda.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import me.molka.lambda.data.ModifierGroup;
import me.molka.lambda.service.ModifierService;
import software.amazon.awssdk.http.HttpStatusCode;
import software.amazon.awssdk.utils.StringUtils;

import javax.inject.Inject;
import java.util.Collection;

public class ApiHandler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

    public static final String ADD_MODIFIER = "add-modifier";
    public static final String GET_MODIFIER_BY_ID = "get-modifier-by-id";
    public static final String GET_MODIFIERS_BY_GROUP = "get-modifiers-by-group";
    public static final String GET_MODIFIERS_BY_PRODUCT = "get-modifiers-by-product";
    public static final String GET_MODIFIERS_BY_MERCHANT = "get-modifiers-by-merchant";

    @Inject
    ModifierService modifierService;

    @Inject
    ObjectMapper mapper;

    @SneakyThrows
    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent input, Context context) {
        String functionName = context.getFunctionName();

        if (functionName.contains(ADD_MODIFIER)) {
            return addModifier(input);
        } else if (functionName.contains(GET_MODIFIERS_BY_PRODUCT)
                || functionName.contains(GET_MODIFIERS_BY_MERCHANT)
                || functionName.contains(GET_MODIFIERS_BY_GROUP)
                || functionName.contains(GET_MODIFIER_BY_ID)) {
            return getModifierById(input);
        } else {
            return APIGatewayV2HTTPResponse.builder()
                    .withBody("Required method is not allowed")
                    .withStatusCode(HttpStatusCode.METHOD_NOT_ALLOWED)
                    .build();
        }
    }

    @SneakyThrows
    private APIGatewayV2HTTPResponse addModifier(APIGatewayV2HTTPEvent input) {
        ModifierGroup request = mapper.readValue(input.getBody(), ModifierGroup.class);
        ModifierGroup modifierGroup = modifierService.addModifier(request);

        return APIGatewayV2HTTPResponse.builder()
                .withBody(mapper.writeValueAsString(modifierGroup))
                .withStatusCode(HttpStatusCode.CREATED)
                .build();
    }

    @SneakyThrows
    private APIGatewayV2HTTPResponse getModifierById(APIGatewayV2HTTPEvent input) {
        String id = buildKey(input);
        String merchantId = input.getPathParameters().get("merchantId");
        Collection<ModifierGroup> modifiersByProduct = modifierService.getModifiersById(merchantId, id);

        return APIGatewayV2HTTPResponse.builder()
                .withBody(mapper.writeValueAsString(modifiersByProduct))
                .withStatusCode(HttpStatusCode.OK)
                .build();

    }

    private String buildKey(APIGatewayV2HTTPEvent input) {
        String productId = input.getPathParameters().get("productId");
        String groupId = input.getPathParameters().get("groupId");
        String modifierId = input.getPathParameters().get("modifierId");

        StringBuilder key = new StringBuilder();
        if (StringUtils.isNotBlank(productId)) {
            key.append(productId);
            if (StringUtils.isNotBlank(groupId)) {
                key.append("#").append(groupId);
                if (StringUtils.isNotBlank(modifierId)) {
                    key.append("#").append(modifierId);
                }
            }
        }
        return key.toString();
    }
}
