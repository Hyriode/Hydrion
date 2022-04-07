package fr.hyriode.hydrion.client.exception;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/04/2022 at 21:13
 */
public class InvalidCodeException extends HydrionException {

    public InvalidCodeException(int statusCode, String cause) {
        super("Received an invalid response status code from request. Status code: " + statusCode + ". Cause: " + cause);
    }

}
