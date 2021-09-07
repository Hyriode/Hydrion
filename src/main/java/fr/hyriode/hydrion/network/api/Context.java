package fr.hyriode.hydrion.network.api;

import fr.hyriode.hydrion.network.api.exception.AbortException;
import fr.hyriode.hydrion.util.References;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 05/09/2021 at 08:56
 */
public class Context {

    private final ChannelHandlerContext ctx;
    private final String contextRoot;

    public Context(ChannelHandlerContext ctx, String contextRoot) {
        this.ctx = ctx;
        this.contextRoot = contextRoot;
    }

    public void send(Object... responses) {
        for (Object response : responses) {
            this.ctx.write(response);
        }
        this.ctx.flush();
    }

    public void sendResponse(byte[] content, String contentType, int statusCode) {
        final ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();

        byteBuf.writeBytes(content);

        final FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(statusCode), byteBuf);

        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, String.format("%s; charset=UTF-8", contentType));
        httpResponse.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());

        this.ctx.writeAndFlush(httpResponse);
    }

    public void redirect(String location, boolean withinContext) {
        final FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);

        if (location.startsWith("/")) {
            location = withinContext ? Paths.get(this.contextRoot, location).toString() : location;
        }

        response.headers().add(HttpHeaderNames.LOCATION, location);

        this.ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    public void redirect(String location) {
        this.redirect(location, true);
    }

    public void text(String content, String contentType, int statusCode) {
        this.sendResponse(content.getBytes(StandardCharsets.UTF_8), contentType, statusCode);
    }

    public void abort(int code, String content) {
        throw new AbortException(HttpResponseStatus.valueOf(code), content);
    }

    public void html(String html, int statusCode) {
        this.text(html, HttpHeaderValues.TEXT_HTML.toString(), statusCode);
    }

    public void html(String html) {
        this.html(html, HttpResponseStatus.OK.code());
    }

    public void error(String msg, String contentType, int statusCode) {
        final ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        final byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);

        byteBuf.writeBytes(bytes);

        final FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(statusCode), byteBuf);

        response.headers().add(HttpHeaderNames.CONTENT_TYPE, String.format("%s; charset=UTF-8", contentType));

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    public void error(String msg, int statusCode) {
        this.error(msg, HttpHeaderValues.TEXT_PLAIN.toString(), statusCode);
    }

    public void error(int statusCode) {
        this.error(HttpResponseStatus.valueOf(statusCode).reasonPhrase(), statusCode);
    }

    public void json(Object object, int statusCode) {
        this.text(References.GSON.toJson(object), "application/json", statusCode);
    }

    public void json(Object object) {
        this.json(object, HttpResponseStatus.OK.code());
    }

    public void close() {
        this.ctx.close();
    }

}
