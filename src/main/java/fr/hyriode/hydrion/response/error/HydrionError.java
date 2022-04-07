package fr.hyriode.hydrion.response.error;

import fr.hyriode.hydrion.object.HydrionObject;
import fr.hyriode.hydrion.response.HydrionResponse;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 29/03/2022 at 19:34
 */
public class HydrionError extends HydrionResponse {

    protected final String cause;

    public HydrionError(String cause) {
        super(false, new Cause(cause));
        this.cause = cause;
    }

    public String getCause() {
        return this.cause;
    }

    private static class Cause extends HydrionObject {

        private final String cause;

        public Cause(String cause) {
            this.cause = cause;
        }

    }
}
