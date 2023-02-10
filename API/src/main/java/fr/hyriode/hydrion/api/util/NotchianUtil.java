package fr.hyriode.hydrion.api.util;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by AstFaster
 * on 21/09/2022 at 07:23
 */
public class NotchianUtil {

    private static final Pattern NOTCHIAN_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,16}$");

    public static boolean isNameValid(String name) {
        return NOTCHIAN_NAME_PATTERN.matcher(name).find();
    }

    public static UUID parseUUID(String input) {
        try {
            if (!input.contains("-")) {
                input = input.substring(0, 8) + "-" +
                        input.substring(8, 12) + "-" +
                        input.substring(12, 16) + "-" +
                        input.substring(16, 20) + "-" +
                        input.substring(20, 32);
            }

            return UUID.fromString(input);
        } catch (Exception e) {
            return null;
        }
    }

}
