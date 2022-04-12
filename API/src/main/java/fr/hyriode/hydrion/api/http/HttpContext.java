package fr.hyriode.hydrion.api.http;

import fr.hyriode.hydrion.api.HydrionAPI;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 25/03/2022 at 12:00
 */
public class HttpContext {

    private final ChannelHandlerContext ctx;

    public HttpContext(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public void sendResponse(byte[] content, String contentType, HttpResponseStatus status) {
        final ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();

        byteBuf.writeBytes(content);

        final FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, byteBuf);

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, String.format("%s; charset=UTF-8", contentType))
                .setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

        this.ctx.writeAndFlush(response);
    }

    public void text(String content, String contentType, HttpResponseStatus status) {
        this.sendResponse(content.getBytes(StandardCharsets.UTF_8), contentType, status);
    }

    public void text(String content, HttpResponseStatus statusCode) {
        this.sendResponse(content.getBytes(StandardCharsets.UTF_8), HttpHeaderValues.TEXT_PLAIN.toString(), statusCode);
    }

    public void text(String content) {
        this.text(content, HttpResponseStatus.OK);
    }

    public void html(String html, HttpResponseStatus status) {
        this.text(html, HttpHeaderValues.TEXT_HTML.toString(), status);
    }

    public void html(String html) {
        this.html(html, HttpResponseStatus.OK);
    }

    public void error(String msg, String contentType, HttpResponseStatus status) {
        final ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        final byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);

        byteBuf.writeBytes(bytes);

        final FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, byteBuf);

        response.headers().add(HttpHeaderNames.CONTENT_TYPE, String.format("%s; charset=UTF-8", contentType));

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    public void error(String msg, HttpResponseStatus status) {
        this.error(msg, HttpHeaderValues.TEXT_PLAIN.toString(), status);
    }

    public void error(HttpResponseStatus status) {
        this.error(status.reasonPhrase(), status);
    }

    public void json(Object object, HttpResponseStatus status) {
        this.text(HydrionAPI.GSON.toJson(object), "application/json", status);
    }

    public void json(Object object) {
        this.json(object, HttpResponseStatus.OK);
    }

}
