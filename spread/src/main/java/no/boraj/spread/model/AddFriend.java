package no.boraj.spread.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by borgizzle on 08.07.2014.
 */
public class AddFriend {
    @JsonProperty("oauth_token")
    private String oauthToken;

    @JsonProperty("username")
    private String username;

    @JsonProperty("response_status")
    private String responseStatus;

    public String getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }
}
