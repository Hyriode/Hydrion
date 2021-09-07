package fr.hyriode.hydrion.network;

import fr.hyriode.hydrion.network.api.Context;
import fr.hyriode.hydrion.network.api.Router;
import fr.hyriode.hydrion.network.api.request.QueryParameter;
import fr.hyriode.hydrion.network.api.request.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/09/2021 at 22:30
 */
public class ServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private static final String FAVICON_ICO = "favicon.ico";

    private final Router router;

    public ServerHandler(Router router) {
        this.router = router;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            final HttpRequest request = (HttpRequest) msg;

            final String uri = request.uri();
            final HttpMethod method = request.method();

            /*if (uri.equals("/" + FAVICON_ICO)) {
                final InputStream inputStream = Hydrion.class.getResourceAsStream("/" + FAVICON_ICO);

                if (inputStream != null) {
                    final byte[] bytes = ByteStreams.toByteArray(inputStream);

                    this.sendResponse(ctx, bytes);
                } else {
                    Hydrion.getLogger().warning("Cannot get " + FAVICON_ICO + " from resources!");
                }
                return;
            }*/

            this.router.handle(new Context(ctx, ""), new Request((FullHttpRequest) request, this.getQueryParameters(uri)));
        }
    }



    private List<QueryParameter> getQueryParameters(String uri) {
        final QueryStringDecoder queryDecoder = new QueryStringDecoder(uri, StandardCharsets.UTF_8);
        final Map<String, List<String>> uriParameters = queryDecoder.parameters();
        final List<QueryParameter> queryParameters = new ArrayList<>();

        for (Map.Entry<String, List<String>> parameter : uriParameters.entrySet()) {
           queryParameters.add(new QueryParameter(parameter.getKey(), parameter.getValue()));
        }

        return queryParameters;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
