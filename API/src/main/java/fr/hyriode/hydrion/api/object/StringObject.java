package fr.hyriode.hydrion.api.object;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 30/03/2022 at 21:19
 */
public class StringObject extends HydrionObject {

    private final String message;

    public StringObject(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
