package fr.hyriode.hydrion.http.server;

import fr.hyriode.hydrion.network.NetworkManager;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 24/03/2022 at 17:32
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private final NetworkManager networkManager;

    public HttpServerInitializer(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().
                addLast("codec", new HttpServerCodec())
                .addLast("aggregator", new HttpObjectAggregator(Integer.MAX_VALUE))
                .addLast("handler", new HttpServerHandler(this.networkManager));
    }

}
