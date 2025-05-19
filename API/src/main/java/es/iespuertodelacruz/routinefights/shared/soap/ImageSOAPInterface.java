package es.iespuertodelacruz.routinefights.shared.soap;

import org.springframework.core.io.UrlResource;
import org.springframework.security.access.annotation.Secured;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

@WebService(targetNamespace = "es.iespuertodelacruz.routinefights.shared.soap")
public interface ImageSOAPInterface {
    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @WebMethod
    @WebResult(name = "image")
    /**
     * Retrieves an image from the server.
     *
     * @param imageName The name of the image to retrieve.
     * @return The image as a Base64 encoded string.
     */
    UrlResource getImage(@WebParam(name = "imageName") String imageName);

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @WebMethod
    @WebResult(name = "uploadResponse")
    /**
     * Uploads an image to the server.
     *
     * @param file The image file to upload.
     * @return A response message indicating the result of the upload.
     */
    String uploadFile(@WebParam(name = "bytes") byte[] file, @WebParam(name = "filename") String fileName,
            @WebParam(name = "contentType") String contentType);
}
