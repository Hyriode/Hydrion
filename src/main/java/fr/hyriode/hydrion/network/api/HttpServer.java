package fr.hyriode.hydrion.network.api;

import fr.hyriode.hydrion.network.ServerInitializer;
import fr.hyriode.hydrion.network.api.util.NettyGroupType;
import fr.hyriode.hydrion.network.api.util.NettyTransportType;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 05/09/2021 at 11:31
 */
public class HttpServer {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private final String host;
    private final int port;
    private final Router router;

    public HttpServer(String host, int port, Router router) {
        this.host = host;
        this.port = port;
        this.router = router;
    }

    public void start() {
        System.out.println("Starting HTTP server...");
        try {
            final NettyTransportType transportType = NettyTransportType.getAvailableTransport();

            this.bossGroup = transportType.eventLoopGroup(NettyGroupType.BOSS);
            this.workerGroup = transportType.eventLoopGroup(NettyGroupType.WORKER);

            final ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(this.bossGroup, this.workerGroup)
                    .channel(transportType.getServerChannelClass())
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ServerInitializer(this.router));

            final Channel channel = bootstrap.bind(this.host, this.port).sync().channel();

            System.out.println("Listening on " + channel.localAddress().toString());

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            System.err.println("Failed to bind to port!");
            System.err.println("Make sure that no other applications are using the port given in configuration.");
            System.err.println("Exception: " + e.getMessage());

            System.exit(1);
        }
    }

    public void stop() {
        System.out.println("Stopping HTTP server...");

        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
    }

}
