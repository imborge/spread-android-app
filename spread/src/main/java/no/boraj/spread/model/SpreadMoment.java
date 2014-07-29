package no.boraj.spread.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by borgizzle on 13.07.2014.
 */
public class SpreadMoment {
    @JsonProperty("oauth_token")
    private String oauthToken;

    @JsonProperty("queue_item_id")
    private long queueItemId;

    public String getOAuthToken() {
        return oauthToken;
    }

    public void setOAuthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public long getQueueItemId() {
        return queueItemId;
    }

    public void setQueueItemId(long queueItemId) {
        this.queueItemId = queueItemId;
    }
}
