package fr.hyriode.hydrion.cache;

import com.mongodb.BasicDBObject;

import java.util.concurrent.ScheduledFuture;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 06/04/2022 at 17:41
 */
public class CachedData {

    private ScheduledFuture<?> task;

    private final int expireTime;
    private final BasicDBObject dbObject;

    public CachedData(int expireTime, BasicDBObject dbObject) {
        this.expireTime = expireTime;
        this.dbObject = dbObject;
    }

    public int getExpireTime() {
        return this.expireTime;
    }

    public BasicDBObject getDBObject() {
        return this.dbObject;
    }

    ScheduledFuture<?> getTask() {
        return this.task;
    }

    void setTask(ScheduledFuture<?> task) {
        this.task = task;
    }

}
