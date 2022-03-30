package fr.hyriode.hydrion.util;

import java.util.regex.Pattern;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 29/03/2022 at 20:59
 */
public class UUIDUtil {

    private static final Pattern PATTERN = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    public static boolean isUUID(String input) {
        return PATTERN.matcher(input).matches();
    }

}
