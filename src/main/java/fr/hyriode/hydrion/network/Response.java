package fr.hyriode.hydrion.network;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/09/2021 at 10:14
 */
public class Response {

    private String string;
    private String method;

    public Response(String string) {
        this.string = string;
    }

    public String getString() {
        return this.string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}
