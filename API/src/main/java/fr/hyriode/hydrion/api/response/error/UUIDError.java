package fr.hyriode.hydrion.api.response.error;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 29/03/2022 at 21:01
 */
public class UUIDError extends HydrionError {

    public UUIDError() {
        super("Malformed UUID");
    }

}
