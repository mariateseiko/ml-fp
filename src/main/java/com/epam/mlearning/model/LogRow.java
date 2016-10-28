package com.epam.mlearning.model;

import java.io.Serializable;

/**
 * Created by Roman_Maier on 10/27/2016.
 */
public class LogRow implements Serializable {
    private String iPinYouId;
    private String userAgent;
    private Double region;
    private Double city;
    private String domain;
    private Double adSlotWidth;
    private Double adSlotHeight;
    private Double adSlotVisibility;
    private Double adSlotFormat;
    private String userTags;
    private Double streamId;

    public LogRow(String iPinYouId, String userAgent, Double region, Double city, String domain,
                  Double adSlotWidth, Double adSlotHeight, Double adSlotVisibility,
                  Double adSlotFormat, String user_tags, Double streamId) {
        this.iPinYouId = iPinYouId;
        this.userAgent = userAgent;
        this.region = region;
        this.city = city;
        this.domain = domain;
        this.adSlotWidth = adSlotWidth;
        this.adSlotHeight = adSlotHeight;
        this.adSlotVisibility = adSlotVisibility;
        this.adSlotFormat = adSlotFormat;
        this.userTags = user_tags;
        this.streamId = streamId;
    }

    public String getIPinYouId() {
        return iPinYouId;
    }

    public void setIPinYouId(String iPinYouId) {
        this.iPinYouId = iPinYouId;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Double getRegion() {
        return region;
    }

    public void setRegion(Double region) {
        this.region = region;
    }

    public Double getCity() {
        return city;
    }

    public void setCity(Double city) {
        this.city = city;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Double getAdSlotWidth() {
        return adSlotWidth;
    }

    public void setAdSlotWidth(Double adSlotWidth) {
        this.adSlotWidth = adSlotWidth;
    }

    public Double getAdSlotHeight() {
        return adSlotHeight;
    }

    public void setAdSlotHeight(Double adSlotHeight) {
        this.adSlotHeight = adSlotHeight;
    }

    public Double getAdSlotVisibility() {
        return adSlotVisibility;
    }

    public void setAdSlotVisibility(Double adSlotVisibility) {
        this.adSlotVisibility = adSlotVisibility;
    }

    public Double getAdSlotFormat() {
        return adSlotFormat;
    }

    public void setAdSlotFormat(Double adSlotFormat) {
        this.adSlotFormat = adSlotFormat;
    }

    public String getUserTags() {
        return userTags;
    }

    public void setUserTags(String userTags) {
        this.userTags = userTags;
    }

    public Double getStreamId() {
        return streamId;
    }

    public void setStreamId(Double streamId) {
        this.streamId = streamId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogRow row = (LogRow) o;

        if (userAgent != null ? !userAgent.equals(row.userAgent) : row.userAgent != null) return false;
        if (region != null ? !region.equals(row.region) : row.region != null) return false;
        if (city != null ? !city.equals(row.city) : row.city != null) return false;
        if (domain != null ? !domain.equals(row.domain) : row.domain != null) return false;
        if (adSlotWidth != null ? !adSlotWidth.equals(row.adSlotWidth) : row.adSlotWidth != null) return false;
        if (adSlotHeight != null ? !adSlotHeight.equals(row.adSlotHeight) : row.adSlotHeight != null)
            return false;
        if (adSlotVisibility != null ? !adSlotVisibility.equals(row.adSlotVisibility) : row.adSlotVisibility != null)
            return false;
        if (adSlotFormat != null ? !adSlotFormat.equals(row.adSlotFormat) : row.adSlotFormat != null)
            return false;
        if (userTags != null ? !userTags.equals(row.userTags) : row.userTags != null) return false;
        return streamId != null ? streamId.equals(row.streamId) : row.streamId == null;

    }

    @Override
    public int hashCode() {
        int result = userAgent != null ? userAgent.hashCode() : 0;
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        result = 31 * result + (adSlotWidth != null ? adSlotWidth.hashCode() : 0);
        result = 31 * result + (adSlotHeight != null ? adSlotHeight.hashCode() : 0);
        result = 31 * result + (adSlotVisibility != null ? adSlotVisibility.hashCode() : 0);
        result = 31 * result + (adSlotFormat != null ? adSlotFormat.hashCode() : 0);
        result = 31 * result + (userTags != null ? userTags.hashCode() : 0);
        result = 31 * result + (streamId != null ? streamId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LogRow{" +
                "userAgent='" + userAgent + '\'' +
                ", region=" + region +
                ", city=" + city +
                ", domain='" + domain + '\'' +
                ", adSlotWidth=" + adSlotWidth +
                ", adSlotHeight=" + adSlotHeight +
                ", adSlotVisibility=" + adSlotVisibility +
                ", adSlotFormat=" + adSlotFormat +
                ", userTags='" + userTags + '\'' +
                ", streamId=" + streamId +
                '}';
    }
}
