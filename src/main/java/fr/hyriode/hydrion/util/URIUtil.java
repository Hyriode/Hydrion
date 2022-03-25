package fr.hyriode.hydrion.util;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 05/09/2021 at 13:00
 */
public class URIUtil {

    public static String normalize(String uri) {
        uri = uri.trim();

        while (uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }

        while (uri.startsWith("/")) {
            uri = uri.substring(1);
        }

        if (!uri.startsWith("/")) {
            uri = "/" + uri;
        }
        return uri;
    }

    public static String getURIPrefix(String uri) {
        String path = uri;

        for (int i = 0; i < uri.length(); i++) {
            if (uri.charAt(i) == '?') {
                path = uri.substring(0, i);
            }
        }

        if (path.endsWith("/")) {
            return path.substring(0, path.length() - 1);
        }
        return path;
    }

}
