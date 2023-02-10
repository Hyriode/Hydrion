package fr.hyriode.hydrion.http.server;

import fr.hyriode.hydrion.network.NetworkManager;
import fr.hyriode.hydrion.util.netty.NettyGroupType;
import fr.hyriode.hydrion.util.netty.NettyTransportType;
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

    private final NetworkManager networkManager;

    private final String name;
    private final int port;

    public HttpServer(NetworkManager networkManager, String name, int port) {
        this.networkManager = networkManager;
        this.name = name;
        this.port = port;
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
                    .childHandler(new HttpServerInitializer(this.networkManager));

            final Channel channel = bootstrap.bind("0.0.0.0", this.port).sync().channel();

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

}
