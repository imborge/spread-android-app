package no.boraj.spread.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by borgizzle on 03.07.2014.
 */
public class User {
    private long id;
    private String facebookId;
    private String username;

    @JsonProperty("oauth_token")
    private String oauthToken;

    @JsonProperty("response_status")
    private String responseStatus;

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOAuthToken() {
        return oauthToken;
    }

    public void setOAuthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }
}
