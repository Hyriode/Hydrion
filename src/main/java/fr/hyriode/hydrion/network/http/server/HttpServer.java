package fr.hyriode.hydrion.network.http.server;

import fr.hyriode.hydrion.network.http.HttpRouter;
import fr.hyriode.hydrion.network.util.NettyGroupType;
import fr.hyriode.hydrion.network.util.NettyTransportType;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 24/03/2022 at 17:26
 */
public class HttpServer {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private final HttpRouter router;

    private final String name;
    private final int port;

    public HttpServer(String name, int port) {
        this.name = name;
        this.port = port;
        this.router = new HttpRouter();
    }

    public void start() {
        System.out.println("Starting " + this.name + " HTTP server...");

        try {
            final NettyTransportType transportType = NettyTransportType.getAvailableTransport();

            System.out.println("Using " + transportType.getName() + " channel type.");

            this.bossGroup = transportType.createEventLoopGroup(NettyGroupType.BOSS);
            this.workerGroup = transportType.createEventLoopGroup(NettyGroupType.WORKER);

            final ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(this.bossGroup, this.workerGroup)
                    .channel(transportType.getServerChannelClass())
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new HttpServerInitializer(this.router));

            final Channel channel = bootstrap.bind("127.0.0.1", this.port).sync().channel();

            System.out.println("Listening on " + channel.localAddress().toString());

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            System.err.println("Failed to bind to port!");
            System.err.println("Make sure that no other applications are using the port given in the configuration.");
            System.err.println("Exception: " + e.getMessage());

            System.exit(-1);
        }
    }

    public void stop() {
        System.out.println("Stopping " + this.name + " HTTP server...");

        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
    }

    public HttpRouter getRouter() {
        return this.router;
    }

}
