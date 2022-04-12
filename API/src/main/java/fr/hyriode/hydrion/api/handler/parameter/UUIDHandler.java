package fr.hyriode.hydrion.api.handler.parameter;

import fr.hyriode.hydrion.api.http.request.HttpRequest;
import fr.hyriode.hydrion.api.response.error.HydrionError;
import fr.hyriode.hydrion.api.response.error.UUIDError;
import fr.hyriode.hydrion.api.util.UUIDUtil;

import java.util.UUID;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/04/2022 at 16:23
 */
public class UUIDHandler extends ParameterHandler<UUID> {

    public UUIDHandler() {
        super(ParameterKeys.UUID);
    }

    @Override
    public HydrionError validate(String parameter) {
        return !UUIDUtil.isUUID(parameter) ? new UUIDError() : null;
    }

    @Override
    public UUID get(HttpRequest request) {
        return UUID.fromString(request.getParameter(this.parameterKey).getValue());
    }

}
