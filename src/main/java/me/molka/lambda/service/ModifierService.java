package me.molka.lambda.service;

import me.molka.lambda.data.ModifierGroupDto;

import java.util.Collection;

public interface ModifierService {
    ModifierGroupDto addModifier(ModifierGroupDto modifierGroup);
    Collection<ModifierGroupDto> getModifiersByProduct(String merchant, String product);
//    List<ModifierGroupDto> getModifiers();
//    Optional<ModifierGroupDto> getModifier(String merchantId);
//    void deleteModifier(String merchantId);
}
