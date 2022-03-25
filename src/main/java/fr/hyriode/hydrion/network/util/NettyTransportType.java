package fr.hyriode.hydrion.network.util;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.ThreadFactory;
import java.util.function.Function;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/09/2021 at 09:54
 */
public enum NettyTransportType {

    NIO("nio", NioServerSocketChannel.class, threadFactory -> new NioEventLoopGroup(0, threadFactory)),
    EPOLL("epoll", EpollServerSocketChannel.class, threadFactory -> new EpollEventLoopGroup(0, threadFactory));

    private final String name;
    private final Class<? extends ServerSocketChannel> serverChannelClass;
    private final Function<ThreadFactory, EventLoopGroup> eventLoopGroupFactory;

    NettyTransportType(String name, Class<? extends ServerSocketChannel> serverChannelClass, Function<ThreadFactory, EventLoopGroup> eventLoopGroupFactory) {
        this.name = name;
        this.serverChannelClass = serverChannelClass;
        this.eventLoopGroupFactory = eventLoopGroupFactory;
    }

    public String getName() {
        return this.name;
    }

    public Class<? extends ServerSocketChannel> getServerChannelClass() {
        return this.serverChannelClass;
    }

    public EventLoopGroup createEventLoopGroup(NettyGroupType groupType) {
        return this.eventLoopGroupFactory.apply(NettyThreadFactory.createThreadFactory(this.name, groupType));
    }

    public static NettyTransportType getAvailableTransport() {
        return Epoll.isAvailable() ? EPOLL : NIO;
    }

}
