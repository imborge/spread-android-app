package no.boraj.spread.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by borgizzle on 10.07.2014.
 */
public class UploadMoment {
    @JsonProperty("oauth_token")
    private String oauthToken;

    @JsonProperty("photo")
    private String photo;

    @JsonProperty("text")
    private String text;

    @JsonProperty("status")
    private String status;

    public String getOAuthToken() {
        return oauthToken;
    }

    public void setOAuthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
