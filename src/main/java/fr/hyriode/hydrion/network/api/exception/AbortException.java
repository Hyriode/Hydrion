package fr.hyriode.hydrion.network.api.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 05/09/2021 at 09:10
 */
public class AbortException extends RuntimeException {

    private final HttpResponseStatus status;
    private String content;

    public AbortException(int statusCode) {
        this(HttpResponseStatus.valueOf(statusCode));
    }

    public AbortException(HttpResponseStatus status) {
        this(status, null, null);
    }

    public AbortException(HttpResponseStatus status, Throwable t) {
        this(status, null, null);
    }

    public AbortException(HttpResponseStatus status, String content) {
        this(status, content, null);
    }

    public AbortException(HttpResponseStatus status, String content, Throwable t) {
        super(t);
        this.status = status;
        this.content = status.reasonPhrase();
        if (content != null) {
            this.content = content;
        }
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
