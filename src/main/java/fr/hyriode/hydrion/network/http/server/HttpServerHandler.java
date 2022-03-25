package fr.hyriode.hydrion.network.http.server;

import fr.hyriode.hydrion.network.http.HttpResponse;
import fr.hyriode.hydrion.network.http.HttpRouter;
import fr.hyriode.hydrion.network.http.request.HttpRequestParameter;
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
 * on 24/03/2022 at 17:34
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private final HttpRouter router;

    public HttpServerHandler(HttpRouter router) {
        this.router = router;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        if (msg instanceof final HttpRequest request) {
            final String uri = request.uri();

            this.router.handleRequest(new fr.hyriode.hydrion.network.http.request.HttpRequest(request, this.getQueryParameters(uri)), new HttpResponse(ctx));
        }
    }


    private List<HttpRequestParameter> getQueryParameters(String uri) {
        final QueryStringDecoder queryDecoder = new QueryStringDecoder(uri, StandardCharsets.UTF_8);
        final Map<String, List<String>> uriParameters = queryDecoder.parameters();
        final List<HttpRequestParameter> queryParameters = new ArrayList<>();

        for (Map.Entry<String, List<String>> parameter : uriParameters.entrySet()) {
            queryParameters.add(new HttpRequestParameter(parameter.getKey(), parameter.getValue()));
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
