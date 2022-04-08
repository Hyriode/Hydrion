package fr.hyriode.hydrion.api.response.error;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/04/2022 at 20:10
 */
public class BadJsonError extends HydrionError {

    public BadJsonError(String missingField) {
        super("Bad json. Missing field: [" + missingField + "]");
    }

}
