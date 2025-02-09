package es.iespuertodelacruz.routinefights.routinefights;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoutineFightsApplicationTests {

  @Test
  void testMain() {
    assertDoesNotThrow(() -> RoutineFightsApplication.main(new String[] {}));
  }

}
