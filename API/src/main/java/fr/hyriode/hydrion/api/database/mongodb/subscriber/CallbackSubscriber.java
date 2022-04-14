package fr.hyriode.hydrion.api.database.mongodb.subscriber;

import java.util.function.Consumer;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 14/04/2022 at 09:01
 */
public class CallbackSubscriber<T> extends OperationSubscriber<T> {

    private Consumer<T> onComplete;

    @Override
    public void onNext(T received) {
        super.onNext(received);

        if (this.onComplete != null) {
            this.onComplete.accept(received);
        }
    }

    public void whenComplete(Consumer<T> onComplete) {
        this.onComplete = onComplete;
    }

}
