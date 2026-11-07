package es.iespuertodelacruz.routinefights;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoutineFightsApplicationTests {

  @Test
  void testMain() {
    // Random free port: the test must not depend on 8080 being available.
    assertDoesNotThrow(() -> RoutineFightsApplication.main(new String[] { "--server.port=0" }));
  }

}
