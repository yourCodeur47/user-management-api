package io.yorosoft.usermanagementapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Dispositif {
    Long id;
    String identifiant;
    List<Mission> missions;

}
