package es.iespuertodelacruz.routinefights.shared.soap;

import org.springframework.security.access.annotation.Secured;

import es.iespuertodelacruz.routinefights.shared.dto.ChartData;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService(targetNamespace = "es.iespuertodelacruz.routinefights.shared.soap")
public interface GraphSOAPInterface {
    @WebMethod
    @Secured("ROLE_ADMIN")
    public ChartData getUserCreationChart();

    @WebMethod
    @Secured("ROLE_ADMIN")
    public ChartData getPostCreationChart();

    @WebMethod
    @Secured("ROLE_ADMIN")
    public ChartData getPostPointsChart();

    @WebMethod
    @Secured("ROLE_ADMIN")
    public ChartData getUserPointsChart();

}
