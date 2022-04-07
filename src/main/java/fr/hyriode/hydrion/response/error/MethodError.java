package fr.hyriode.hydrion.response.error;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 30/03/2022 at 20:56
 */
public class MethodError extends HydrionError {

    public MethodError() {
        super("Invalid HTTP method");
    }

}
