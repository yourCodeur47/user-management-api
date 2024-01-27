package io.yorosoft.usermanagementapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserApp {
    Long id;
    List<Equipe> equipes;
}
