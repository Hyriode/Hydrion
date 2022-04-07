package fr.hyriode.hydrion.module.network;

import com.mongodb.BasicDBObject;
import fr.hyriode.hydrion.Hydrion;
import fr.hyriode.hydrion.handler.HydrionHandler;
import fr.hyriode.hydrion.network.http.HttpContext;
import fr.hyriode.hydrion.network.http.request.HttpRequest;
import fr.hyriode.hydrion.response.HydrionPostRequestResponse;
import fr.hyriode.hydrion.response.HydrionResponse;
import io.netty.handler.codec.http.HttpMethod;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 29/03/2022 at 19:38
 */
public class NetworkHandler extends HydrionHandler {

    private final NetworkModule networkModule;

    public NetworkHandler(Hydrion hydrion, NetworkModule networkModule) {
        super(hydrion);
        this.networkModule = networkModule;

        this.addMethodHandler(HttpMethod.GET, (request, ctx) -> this.get());
        this.addMethodHandler(HttpMethod.POST, this::post);
    }

    private HydrionResponse get() {
        return new HydrionResponse(true, new NetworkObject(this.networkModule.getNetwork()));
    }

    private HydrionResponse post(HttpRequest request, HttpContext ctx) {
        return this.handleJsonPost(request, ctx, json -> {
            final BasicDBObject dbObject = BasicDBObject.parse(json);

            this.networkModule.setNetwork(dbObject);

            return new HydrionPostRequestResponse();
        });
    }

}
