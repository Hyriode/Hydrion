package fr.hyriode.hydrion.handler.parameter;

import fr.hyriode.hydrion.network.http.request.HttpRequest;
import fr.hyriode.hydrion.response.error.HydrionError;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/04/2022 at 16:21
 */
public abstract class ParameterHandler<T> {

    protected final String parameterKey;

    public ParameterHandler(String parameterKey) {
        this.parameterKey = parameterKey;
    }

    public abstract HydrionError validate(String parameter);

    public abstract T get(HttpRequest request);

    public String getParameterKey() {
        return this.parameterKey;
    }

}
