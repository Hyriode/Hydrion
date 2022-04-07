package fr.hyriode.hydrion.handler;

import fr.hyriode.hydrion.Hydrion;
import fr.hyriode.hydrion.handler.parameter.ParameterHandler;
import fr.hyriode.hydrion.network.http.HttpContext;
import fr.hyriode.hydrion.network.http.request.HttpRequest;
import fr.hyriode.hydrion.network.http.request.IHttpRequestHandler;
import fr.hyriode.hydrion.response.HydrionResponse;
import fr.hyriode.hydrion.response.error.*;
import fr.hyriode.hydrion.util.JsonUtil;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 29/03/2022 at 19:30
 */
public abstract class HydrionHandler implements IHttpRequestHandler {

    private static final String API_KEY_HEADER_KEY = "API-Key";
    private static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";

    protected final Set<HttpMethod> acceptedMethods;

    protected final List<ParameterHandler<?>> parameterHandlers;
    protected final Map<HttpMethod, BiFunction<HttpRequest, HttpContext, HydrionResponse>> methodHandlers;

    protected final Hydrion hydrion;

    public HydrionHandler(Hydrion hydrion) {
        this.hydrion = hydrion;
        this.acceptedMethods = new HashSet<>();
        this.parameterHandlers = new ArrayList<>();
        this.methodHandlers = new HashMap<>();
    }

    @Override
    public void onRequest(HttpRequest request, HttpContext ctx) {
        // TODO API Key system
        /*if (!request.getHeaders().contains(API_KEY_HEADER_KEY)) {
            ctx.text(new HydrionHeaderError(API_KEY_HEADER_KEY).toJson());
            return;
        }

        final String apiKey = request.getHeaders().get(API_KEY_HEADER_KEY);

        if (Verify key) {
            ctx.text(new HydrionError("Invalid API key").toJson());
            return;
        }*/

        HydrionResponse response = null;

        for (ParameterHandler<?> handler : this.parameterHandlers) {
            final String key = handler.getParameterKey();

            if (!request.hasParameter(key)) {
                ctx.text(new ParameterError(key).toJson(), HttpResponseStatus.BAD_REQUEST);
                return;
            }

            final HydrionError error = handler.validate(request.getParameter(key).getValue());

            if (error != null) {
                response = error;
                continue;
            }

            final Object object = handler.get(request);

            if (object != null) {
                final Map<Class<?>, List<Object>> parameterObjects = request.getParameterObjects();
                final Class<?> clazz = object.getClass();

                List<Object> objects = parameterObjects.get(clazz);
                if (objects == null) {
                    objects = new ArrayList<>();
                }

                objects.add(object);

                parameterObjects.put(clazz, objects);
            }
        }

        if (response == null) {
            final BiFunction<HttpRequest, HttpContext, HydrionResponse> methodHandler = this.methodHandlers.get(request.getMethod());

            if (methodHandler != null) {
                response = methodHandler.apply(request, ctx);
            }

            if (response == null) {
                response = this.handle(request, ctx);

                if (response == null) {
                    ctx.text(new ServerError().toJson(), HttpResponseStatus.INTERNAL_SERVER_ERROR);
                    return;
                }

                if (this.acceptedMethods.size() > 0 && !this.acceptedMethods.contains(request.getMethod())) {
                    ctx.text(new MethodError().toJson(), HttpResponseStatus.METHOD_NOT_ALLOWED);
                    return;
                }
            }
        }

        ctx.text(response.toJson());
    }

    public HydrionResponse handle(HttpRequest request, HttpContext ctx) {
        return null;
    }

    protected HydrionResponse handleJsonPost(HttpRequest request, HttpContext ctx, Function<String, HydrionResponse> handler) {
        if (request.getMethod() == HttpMethod.POST) {
            final HttpHeaders headers = request.getHeaders();

            if (headers.contains(CONTENT_TYPE_HEADER_KEY)) {
                final String contentType = headers.get(CONTENT_TYPE_HEADER_KEY);

                if (!contentType.equalsIgnoreCase("application/json; charset=UTF-8")) {
                    ctx.text(new PostError("bad " + CONTENT_TYPE_HEADER_KEY).toJson(), HttpResponseStatus.UNPROCESSABLE_ENTITY);
                    return null;
                }

                final String json = request.getContent().toString(StandardCharsets.UTF_8);

                if (json == null || !JsonUtil.isValid(json)) {
                    ctx.text(new PostError("bad json").toJson(), HttpResponseStatus.UNPROCESSABLE_ENTITY);
                    return null;
                }

                return handler.apply(json);
            }
        }
        return null;
    }

    protected void addAcceptedMethods(HttpMethod... methods) {
        this.acceptedMethods.addAll(Arrays.asList(methods));
    }

    protected void addParameterHandlers(ParameterHandler<?>... handlers) {
        this.parameterHandlers.addAll(Arrays.asList(handlers));
    }

    protected void addMethodHandler(HttpMethod method, BiFunction<HttpRequest, HttpContext, HydrionResponse> handler) {
        this.acceptedMethods.add(method);
        this.methodHandlers.put(method, handler);
    }

}
