package fr.hyriode.hydrion.response;

import fr.hyriode.hydrion.object.StringObject;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 30/03/2022 at 21:18
 */
public class HydrionPostRequestResponse extends HydrionResponse {

    public HydrionPostRequestResponse() {
        super(true, new StringObject("Data was successfully processed."));
    }

}
