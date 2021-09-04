package fr.hyriode.hydrion.network;

import fr.hyriode.hydrion.Hydrion;
import fr.hyriode.hydrion.configuration.Configuration;
import fr.hyriode.hydrion.network.util.NettyGroupType;
import fr.hyriode.hydrion.network.util.NettyTransportType;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/09/2021 at 18:24
 */
public class NetworkManager {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private final Hydrion hydrion;

    public NetworkManager(Hydrion hydrion) {
        this.hydrion = hydrion;
    }

    public void start() {
        System.out.println("Starting network manager...");

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
                    .childHandler(new ServerInitializer());

            final Configuration configuration = this.hydrion.getConfiguration();
            final Channel channel = bootstrap.bind(configuration.getHost(), configuration.getPort()).sync().channel();

            System.out.println("Listening on " + channel.localAddress().toString());

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            System.err.println("Failed to bind to port!");
            System.err.println("Make sure that no other applications are using the port given in configuration.");
            System.err.println("Exception: " + e.getMessage());

            System.exit(1);
        }
    }

    public void shutdown() {
        System.out.println("Stopping network manager...");

        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
    }

}
