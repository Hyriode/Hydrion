package fr.hyriode.hydrion.api.response.error;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 31/03/2022 at 21:20
 */
public class PostError extends HydrionError {

    public PostError(String reason) {
        super("Invalid POST request. Reason: " + reason);
    }

}
