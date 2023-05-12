
package com.example.jokevish;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Attachment {

    @SerializedName("fallback")
    @Expose
    private String fallback;
    @SerializedName("footer")
    @Expose
    private String footer;
    @SerializedName("text")
    @Expose
    private String text;

    public String getFallback() {
        return fallback;
    }

    public void setFallback(String fallback) {
        this.fallback = fallback;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
