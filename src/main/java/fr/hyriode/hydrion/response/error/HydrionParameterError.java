package fr.hyriode.hydrion.response.error;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 29/03/2022 at 20:56
 */
public class HydrionParameterError extends HydrionError {

    public HydrionParameterError(String missingField) {
        super("Missing parameter field: [" + missingField + "]");
    }

}
