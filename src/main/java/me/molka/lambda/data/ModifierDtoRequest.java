package me.molka.lambda.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ModifierDtoRequest extends ModifierDto {
    private String action;
}
