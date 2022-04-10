package fr.hyriode.hydrion.api.response.error;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 29/03/2022 at 20:56
 */
public class ParameterError extends HydrionError {

    public ParameterError(String missingField) {
        super("Missing parameter field: [" + missingField + "]");
    }

}