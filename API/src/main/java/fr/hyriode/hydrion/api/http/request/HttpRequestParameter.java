package fr.hyriode.hydrion.api.http.request;

import java.util.List;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 25/03/2022 at 11:05
 */
public class HttpRequestParameter {

    private final String key;
    private final List<String> values;

    public HttpRequestParameter(String key, List<String> values) {
        this.key = key;
        this.values = values;
    }

    public String getKey() {
        return this.key;
    }

    public List<String> getValues() {
        return this.values;
    }

    public String getValue() {
        return this.values.get(0);
    }

}
