package fr.hyriode.hydrion.network.api.request;

import java.util.List;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 05/09/2021 at 08:49
 */
public class QueryParameter {

    private final String key;
    private final List<String> values;

    public QueryParameter(String key, List<String> values) {
        this.key = key;
        this.values = values;
    }

    public String getKey() {
        return this.key;
    }

    public List<String> getValues() {
        return this.values;
    }

}
