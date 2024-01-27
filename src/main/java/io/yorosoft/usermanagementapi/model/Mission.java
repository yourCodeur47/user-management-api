package io.yorosoft.usermanagementapi.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Mission {
    Long id;
    Equipe equipe;

    public String getEquipeName() {
        return equipe.getName();
    }
}
