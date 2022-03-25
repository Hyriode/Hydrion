package fr.hyriode.hydrion.network.http;

import fr.hyriode.hydrion.Hydrion;
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
 * on 25/03/2022 at 12:00
 */
public class HttpResponse {

    private final ChannelHandlerContext ctx;

    public HttpResponse(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public void sendResponse(byte[] content, String contentType, int statusCode) {
        final ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();

        byteBuf.writeBytes(content);

        final FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(statusCode), byteBuf);

        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, String.format("%s; charset=UTF-8", contentType));
        httpResponse.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());

        this.ctx.writeAndFlush(httpResponse);
    }

    public void text(String content, String contentType, int statusCode) {
        this.sendResponse(content.getBytes(StandardCharsets.UTF_8), contentType, statusCode);
    }

    public void text(String content, int statusCode) {
        this.sendResponse(content.getBytes(StandardCharsets.UTF_8), HttpHeaderValues.TEXT_PLAIN.toString(), statusCode);
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
        this.text(Hydrion.GSON.toJson(object), "application/json", statusCode);
    }

    public void json(Object object) {
        this.json(object, HttpResponseStatus.OK.code());
    }

}
