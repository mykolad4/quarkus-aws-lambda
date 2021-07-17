package me.molka.lambda.service;

import me.molka.lambda.data.ModifierDto;
import me.molka.lambda.data.ModifierGroupDto;

import java.util.List;
import java.util.Optional;

public interface ModifierService {
    ModifierGroupDto addModifier(ModifierGroupDto modifierGroup);
//    List<ModifierGroupDto> getModifiers();
//    Optional<ModifierGroupDto> getModifier(String merchantId);
//    void deleteModifier(String merchantId);
}
