package fr.hyriode.hydrion.api.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

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

    public ChannelFuture sendResponse(byte[] content, String contentType, HttpResponseStatus status) {
        final ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();

        byteBuf.writeBytes(content);

        final FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, byteBuf);

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, String.format("%s; charset=UTF-8", contentType));

        return this.ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
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

    private void error(String msg, String contentType, HttpResponseStatus status) {
        final HttpResponse response = new HttpResponse()
                .add("success", false)
                .add("code", status.code())
                .add("message", msg);

        this.sendResponse(response.toString().getBytes(StandardCharsets.UTF_8), contentType, status);
    }

    public void error(String msg, HttpResponseStatus status) {
        this.error(msg, HttpHeaderValues.APPLICATION_JSON.toString(), status);
    }

    public void json(Consumer<HttpResponse> responseConsumer, HttpResponseStatus status) {
        final HttpResponse response = new HttpResponse().add("success", true);

        responseConsumer.accept(response);

        this.text(response.toString(), "application/json", status);
    }

    public void json(Consumer<HttpResponse> responseConsumer) {
        this.json(responseConsumer, HttpResponseStatus.OK);
    }

    public void json(Object obj) {
        this.json(response -> response.add("response", obj), HttpResponseStatus.OK);
    }

}
