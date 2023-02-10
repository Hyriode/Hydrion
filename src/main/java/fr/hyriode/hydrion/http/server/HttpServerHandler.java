package fr.hyriode.hydrion.http.server;

import fr.hyriode.hydrion.api.http.HttpContext;
import fr.hyriode.hydrion.api.http.request.HttpRequest;
import fr.hyriode.hydrion.api.http.request.HttpRequestParameter;
import fr.hyriode.hydrion.network.NetworkManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 24/03/2022 at 17:34
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final NetworkManager networkManager;

    public HttpServerHandler(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
        final HttpContext context = new HttpContext(ctx);

        try {
            this.networkManager.dispatch(new HttpRequest(msg, context, this.getQueryParameters(msg.uri())), context);
        } catch (Exception e) {
            context.error("Internal Server Error", HttpResponseStatus.INTERNAL_SERVER_ERROR);
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
        if (!(cause instanceof SocketException)) {
            cause.printStackTrace();
        }

        ctx.close();
    }

}
