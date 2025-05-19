package es.iespuertodelacruz.routinefights.shared.soap;

import es.iespuertodelacruz.routinefights.shared.dto.UserDTOAuth;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

@WebService(targetNamespace = "es.iespuertodelacruz.routinefights.shared.soap")
public interface AuthSOAPInterface {
    @WebMethod
    @WebResult(name = "user")
    public UserDTOAuth register(@WebParam(name = "user") UserDTOAuth userDTOAuth);

    @WebMethod
    @WebResult(name = "token")
    public String login(@WebParam(name = "email") String email, @WebParam(name = "password") String password);

    @WebMethod
    public String verify(@WebParam(name = "email") String email, @WebParam(name = "token") String token);
}
