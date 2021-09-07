package fr.hyriode.hydrion.network;

import fr.hyriode.hydrion.network.api.Router;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/09/2021 at 18:30
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private final Router router;

    public ServerInitializer(Router router) {
        this.router = router;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline()
                .addLast(new HttpServerCodec())
                .addLast(new HttpObjectAggregator(Integer.MAX_VALUE))
                .addLast(new HttpServerExpectContinueHandler())
                .addLast(new ServerHandler(this.router));
    }

}