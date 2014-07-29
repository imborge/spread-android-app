package no.boraj.spread.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by borgizzle on 13.07.2014.
 */
public class QueueItem {
    @JsonProperty("id")
    private long id;

    @JsonProperty("from_user")
    private String fromUser;

    @JsonProperty("moment")
    private Moment moment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public Moment getMoment() {
        return moment;
    }

    public void setMoment(Moment moment) {
        this.moment = moment;
    }
}
