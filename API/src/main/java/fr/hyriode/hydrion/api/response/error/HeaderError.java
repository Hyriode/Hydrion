package fr.hyriode.hydrion.api.response.error;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 29/03/2022 at 19:36
 */
public class HeaderError extends HydrionError {

    public HeaderError(String missingField) {
        super("Missing header field: [" + missingField + "]");
    }

}
