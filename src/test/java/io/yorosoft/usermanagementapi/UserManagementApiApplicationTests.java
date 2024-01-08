package io.yorosoft.usermanagementapi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserManagementApiApplicationTests {

	@Test
	@Disabled(value = "not business logic")
	void contextLoads() {
		var test = 1L;
		Assertions.assertEquals(1L, test);
	}

}
