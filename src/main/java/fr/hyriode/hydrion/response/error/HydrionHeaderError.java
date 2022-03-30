package fr.hyriode.hydrion.response.error;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 29/03/2022 at 19:36
 */
public class HydrionHeaderError extends HydrionError {

    public HydrionHeaderError(String missingField) {
        super("Missing header field: [" + missingField + "]");
    }

}
