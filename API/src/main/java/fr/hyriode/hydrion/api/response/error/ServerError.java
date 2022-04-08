package fr.hyriode.hydrion.api.response.error;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 29/03/2022 at 20:48
 */
public class ServerError extends HydrionError {

    public ServerError() {
        super("An error occurred while responding.");
    }

}
