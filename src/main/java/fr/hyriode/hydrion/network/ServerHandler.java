package fr.hyriode.hydrion.network;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 03/09/2021 at 22:30
 */
public class ServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private HttpRequest request;
    private FullHttpRequest fullRequest;
    private HttpHeaders headers;

    private static final AsciiString CONTENT_TYPE = AsciiString.cached("Content-Type");
    private static final AsciiString CONTENT_LENGTH = AsciiString.cached("Content-Length");
    private static final AsciiString CONNECTION = AsciiString.cached("Connection");
    private static final AsciiString KEEP_ALIVE = AsciiString.cached("keep-alive");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        final Response response = new Response("AstFaster");

        if (msg instanceof HttpRequest) {
            this.request = (HttpRequest) msg;
            this.headers = this.request.headers();

            final String uri = this.request.uri();
            final HttpMethod method = this.request.method();

            response.setMethod(method.name());

            System.out.println("HTTP URI: " + uri);

            System.out.println(method);

            if (method.equals(HttpMethod.GET)) {
                final QueryStringDecoder queryDecoder = new QueryStringDecoder(uri, StandardCharsets.UTF_8);
                final Map<String, List<String>> uriParameters = queryDecoder.parameters();

                for (Map.Entry<String, List<String>> parameter : uriParameters.entrySet()) {
                    for (String parameterValue : parameter.getValue()) {
                        System.out.println(parameter.getKey() + "=" + parameterValue);
                    }
                }
            } else if (method.equals(HttpMethod.POST)) {
                this.fullRequest = (FullHttpRequest) msg;

                this.dealWithContentType();
            }
        }

        final String json = new Gson().toJson(response);
        final byte[] content = json.getBytes(StandardCharsets.UTF_8);

        FullHttpResponse httpResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(content));
        httpResponse.headers().set(CONTENT_TYPE, "text/plain");
        httpResponse.headers().setInt(CONTENT_LENGTH, httpResponse.content().readableBytes());

        boolean keepAlive = HttpUtil.isKeepAlive(request);
        if (!keepAlive) {
            ctx.write(httpResponse).addListener(ChannelFutureListener.CLOSE);
        } else {
            httpResponse.headers().set(CONNECTION, KEEP_ALIVE);
            ctx.write(httpResponse);
        }
    }

    private void dealWithContentType() {
        final String contentType = this.getContentType();

        if (contentType.equals("application/json")) {
            final String json = this.fullRequest.content().toString(StandardCharsets.UTF_8);
            final JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

            for (Map.Entry<String, JsonElement> item : jsonObject.entrySet()) {
                System.out.println(item.getKey() + "=" + item.getValue().toString());
            }
        } else if (contentType.equals("application/x-www-form-urlencoded")) {
            final String json = fullRequest.content().toString(StandardCharsets.UTF_8);
            final QueryStringDecoder queryDecoder = new QueryStringDecoder(json, false);
            final Map<String, List<String>> uriParameters = queryDecoder.parameters();

            for (Map.Entry<String, List<String>> parameter : uriParameters.entrySet()) {
                for (String value : parameter.getValue()) {
                    System.out.println(parameter.getKey() + "=" + value);
                }
            }
        }
    }

    private String getContentType() {
        final String contentType = this.headers.get("Content-Type");

        if (contentType != null) {
            final String[] list = contentType.split(";");

            return list[0];
        }
        return "";
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
