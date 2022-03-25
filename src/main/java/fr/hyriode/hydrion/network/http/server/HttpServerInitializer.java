package fr.hyriode.hydrion.network.http.server;

import fr.hyriode.hydrion.network.http.HttpRouter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 24/03/2022 at 17:32
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private final HttpRouter router;

    public HttpServerInitializer(HttpRouter router) {
        this.router = router;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().
                addLast("decoder", new HttpRequestDecoder())
                .addLast("encoder", new HttpResponseEncoder())
                .addLast("handler", new HttpServerHandler(this.router));
    }

}
