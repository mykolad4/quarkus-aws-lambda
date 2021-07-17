package me.molka.lambda.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import me.molka.lambda.data.ModifierDto;
import me.molka.lambda.data.ModifierDtoRequest;
import me.molka.lambda.data.ModifierGroupDto;
import me.molka.lambda.data.REQUEST_TYPE;
import me.molka.lambda.service.ModifierService;

import javax.inject.Inject;
import java.lang.management.OperatingSystemMXBean;
import java.util.Optional;

public class ModifierHandler implements RequestHandler<ModifierDtoRequest, String> {

    @Inject
    ModifierService modifierService;

    @Inject
    ObjectMapper mapper;

//    todo: replace by several handlers
    @Override
    @SneakyThrows
    public String handleRequest(ModifierDtoRequest modifier, Context context) {
        switch (getAction(modifier.getAction())) {
            case CREATE_MODIFIER:
                return addModifier(modifier);
//            case GET_MODIFIER:
//                return getModifier(modifier);
//            case DELETE_MODIFIER:
//                return deleteModifier(modifier);
//            case LIST:
//                return getAllModifiers();
            case UNKNOWN:
                return null;
        }
        return null;
    }

    private String addModifier(ModifierDtoRequest request) throws JsonProcessingException {
        ModifierGroupDto modifier = modifierService.addModifier(request);
        return mapper.writeValueAsString(modifier);
    }

//    private String getModifier(ModifierDtoRequest request) throws JsonProcessingException {
//        Optional<ModifierDto> modifier = modifierService.getModifier(request.getId());
//        return mapper.writeValueAsString(modifier);
//    }
//
//    private String deleteModifier(ModifierDtoRequest request) throws JsonProcessingException {
//        modifierService.deleteModifier(request.getId());
//        return mapper.writeValueAsString("Deleted "+ request.getId());
//    }
//
//    private String getAllModifiers() throws JsonProcessingException {
//        return mapper.writeValueAsString(modifierService.getModifiers());
//    }

    private REQUEST_TYPE getAction(String requestType) {
        try {
            return REQUEST_TYPE.valueOf(requestType);
        } catch (Exception e) {
            return REQUEST_TYPE.UNKNOWN;
        }
    }
}
