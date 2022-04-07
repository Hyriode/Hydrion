package fr.hyriode.hydrion.handler.parameter;

import fr.hyriode.hydrion.network.http.request.HttpRequest;
import fr.hyriode.hydrion.response.error.HydrionError;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/04/2022 at 16:40
 */
public class StringHandler extends ParameterHandler<String> {

    public StringHandler(String parameterKey) {
        super(parameterKey);
    }

    @Override
    public HydrionError validate(String parameter) {
        return null;
    }

    @Override
    public String get(HttpRequest request) {
        return request.getParameter(this.parameterKey).getValue();
    }

}
