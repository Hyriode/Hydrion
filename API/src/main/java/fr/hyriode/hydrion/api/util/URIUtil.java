package fr.hyriode.hydrion.api.util;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 05/09/2021 at 13:00
 */
public class URIUtil {

    public static String normalize(String uri) {
        uri = uri.trim();

        while (uri.endsWith("/") && !uri.equals("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }

        if (!uri.startsWith("/")) {
            uri = "/" + uri;
        }
        return uri;
    }

}
