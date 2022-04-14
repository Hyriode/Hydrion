package fr.hyriode.hydrion.api.cache;

import java.util.concurrent.ScheduledFuture;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 06/04/2022 at 17:41
 */
public class CachedData<T> {

    public static final int EXPIRATION_TIME = 600;

    private ScheduledFuture<?> task;

    private final int expirationTime;
    private final T value;

    public CachedData(int expirationTime, T value) {
        this.expirationTime = expirationTime;
        this.value = value;
    }

    public CachedData(T value) {
        this(EXPIRATION_TIME, value);
    }

    public int getExpirationTime() {
        return this.expirationTime;
    }

    public T getValue() {
        return value;
    }

    public ScheduledFuture<?> getTask() {
        return this.task;
    }

    public void setTask(ScheduledFuture<?> task) {
        this.task = task;
    }

}
