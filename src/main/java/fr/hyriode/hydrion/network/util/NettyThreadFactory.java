package fr.hyriode.hydrion.network.util;

import io.netty.util.concurrent.FastThreadLocalThread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/09/2021 at 09:50
 */
public class NettyThreadFactory implements ThreadFactory {

    private final AtomicInteger threadNumber = new AtomicInteger();
    private final String threadNameFormat;

    public NettyThreadFactory(String threadNameFormat) {
        this.threadNameFormat = threadNameFormat;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return new FastThreadLocalThread(runnable, String.format(this.threadNameFormat, this.threadNumber.getAndIncrement()));
    }

    public static ThreadFactory createThreadFactory(String name, NettyGroupType groupType) {
        return new NettyThreadFactory("Netty -> Transport: " + name + "; Group: " + groupType.getName() + " #%d");
    }

}