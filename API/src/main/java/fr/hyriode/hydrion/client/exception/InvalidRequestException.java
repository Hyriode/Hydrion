package fr.hyriode.hydrion.client.exception;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/04/2022 at 21:13
 */
public class InvalidRequestException extends HydrionException {

    public InvalidRequestException(String cause) {
        super("Sent request is invalid! Cause: " + cause);
    }

}
