package io.yorosoft.usermanagementapi;

import io.yorosoft.usermanagementapi.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@SpringBootApplication
public class UserManagementApiApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(UserManagementApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		var equipe1 = new Equipe(1L, "EASYSTACK");
		var equipe2 = new Equipe(2L, "GEC");
		var equipe3 = new Equipe(3L, "LINKY");

		var mission1 = new Mission(1L, equipe1);
		var mission2 = new Mission(2L, equipe2);
		var mission3 = new Mission(3L, equipe3);

		var dispo1 = new Dispositif(1L,"AAA", List.of(mission1, mission2, mission3));
		var dispo2 = new Dispositif(2L,"BBB", List.of(mission1, mission3));

		var cra1 = new Cra(1L, 1L);
		var cra2 = new Cra(2L, 2L);

		List<Cra> cras = List.of(cra1, cra2);


		var userApp = new UserApp(1L, List.of(equipe1, equipe2));

		Map<Long, Dispositif> dispoMap = new HashMap<>();
		dispoMap.put(dispo1.getId(), dispo1);
		dispoMap.put(dispo2.getId(), dispo2);


		var craInvalides = cras.stream()
			.flatMap(cra -> mapWidthCraDispo(cra, dispoMap, userApp).stream())
			.collect(Collectors.groupingBy(CraInvalide::getEquipName, Collectors.counting()))
			.entrySet().stream()
			.map(entry -> new CraInvalide(entry.getKey(), entry.getValue()))
			.sorted(Comparator.comparing(CraInvalide::getEquipName, String.CASE_INSENSITIVE_ORDER))
			.toList();


		log.info("DEBUT========================>");
		log.info(craInvalides.toString());
		log.info("FIN========================>");
	}

	private List<CraInvalide> mapWidthCraDispo(Cra cra, Map<Long, Dispositif> dispoMap, UserApp userApp) {
    var dispo = dispoMap.get(cra.getDispositifId());

	return userApp.getEquipes().stream()
			.filter(equipe -> dispo.getMissions().stream()
					.anyMatch(mission -> mission.getEquipe().getName().equals(equipe.getName())))
			.map(equipe -> new CraInvalide(equipe.getName(), dispo.getId()))
			.toList();
	}
}
