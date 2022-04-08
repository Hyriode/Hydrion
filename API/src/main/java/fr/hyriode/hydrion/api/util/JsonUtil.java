package fr.hyriode.hydrion.api.util;

import fr.hyriode.hydrion.api.HydrionAPI;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 02/04/2022 at 16:37
 */
public class JsonUtil {

    public static boolean isValid(String json) {
        try {
            HydrionAPI.GSON.fromJson(json, Object.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
