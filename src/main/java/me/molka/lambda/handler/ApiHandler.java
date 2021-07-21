package me.molka.lambda.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import me.molka.lambda.data.ModifierGroupDto;
import me.molka.lambda.service.ModifierService;
import software.amazon.awssdk.http.HttpStatusCode;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;

public class ApiHandler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

    @Inject
    ModifierService modifierService;

    @Inject
    ObjectMapper mapper;

    @SneakyThrows
    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent input, Context context) {
        String functionName = context.getFunctionName();

        if (functionName.contains("add-modifier")) {
            ModifierGroupDto request = mapper.readValue(input.getBody(), ModifierGroupDto.class);
            ModifierGroupDto modifierGroupDto = modifierService.addModifier(request);
            return APIGatewayV2HTTPResponse.builder()
                    .withBody(modifierGroupDto.toString())
                    .withStatusCode(HttpStatusCode.CREATED)
                    .build();
        } else if (functionName.contains("get-modifier-by-product")) {
            Map<String, String> pathParameters = input.getPathParameters();
            String merchantId = pathParameters.get("merchantId");
            String productId = pathParameters.get("productId");

            Collection<ModifierGroupDto> modifiersByProduct = modifierService.getModifiersByProduct(merchantId, productId);

            return APIGatewayV2HTTPResponse.builder()
                    .withBody(mapper.writeValueAsString(modifiersByProduct))
                    .withStatusCode(HttpStatusCode.OK)
                    .build();
        }
        return APIGatewayV2HTTPResponse.builder()
                .withBody("Required method is not allowed")
                .withStatusCode(HttpStatusCode.METHOD_NOT_ALLOWED)
                .build();
    }
}
