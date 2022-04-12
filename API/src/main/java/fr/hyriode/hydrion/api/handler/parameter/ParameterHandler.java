package fr.hyriode.hydrion.api.handler.parameter;

import fr.hyriode.hydrion.api.http.request.HttpRequest;
import fr.hyriode.hydrion.api.response.error.HydrionError;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/04/2022 at 16:21
 */
public abstract class ParameterHandler<T> {

    protected final String parameterKey;
    private boolean optional;

    public ParameterHandler(String parameterKey) {
        this.parameterKey = parameterKey;
    }

    public abstract HydrionError validate(String parameter);

    public abstract T get(HttpRequest request);

    public String getParameterKey() {
        return this.parameterKey;
    }

    public boolean isOptional() {
        return this.optional;
    }

    public ParameterHandler<T> optional() {
        this.optional = true;
        return this;
    }

}
