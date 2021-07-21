package me.molka.lambda.service;

import me.molka.lambda.data.ModifierGroup;

import java.util.Collection;

public interface ModifierService {
    ModifierGroup addModifier(ModifierGroup modifierGroup);
    Collection<ModifierGroup> getModifiersById(String merchant, String id);
}
