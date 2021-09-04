package fr.hyriode.hydrion.configuration;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/09/2021 at 11:50
 */
public class Configuration {

    private String host;
    private int port;

    public Configuration(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
