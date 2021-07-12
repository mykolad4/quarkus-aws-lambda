package me.molka.lambda.service;

import me.molka.lambda.data.ModifierDto;

import java.util.List;
import java.util.Optional;

public interface ModifierService {
    ModifierDto addModifier(ModifierDto modifier);
    List<ModifierDto> getModifiers();
    Optional<ModifierDto> getModifier(String modifierId);
    void deleteModifier(String modifierId);
}
