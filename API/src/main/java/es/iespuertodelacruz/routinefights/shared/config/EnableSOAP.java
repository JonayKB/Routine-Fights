package es.iespuertodelacruz.routinefights.shared.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:cxf-service.xml")
public class EnableSOAP {

}
