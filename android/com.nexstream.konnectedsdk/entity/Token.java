package com.nexstream.konnectedsdk.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("merchantId")
    @Expose
    private String merchantId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("maskCard")
    @Expose
    private String maskCard;
    @SerializedName("maskCardType")
    @Expose
    private String maskCardType;

    /**
     * @return The merchantId
     */
    public String getMerchantId() {
        return merchantId;
    }

    /**
     * @param merchantId The merchantId
     */
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    /**
     * @return The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId The userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return The token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token The token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return The maskCard
     */
    public String getMaskCard() {
        return maskCard;
    }

    /**
     * @param maskCard The maskCard
     */
    public void setMaskCard(String maskCard) {
        this.maskCard = maskCard;
    }

    /**
     * @return The maskCardType
     */
    public String getMaskCardType() {
        return maskCardType;
    }

    /**
     * @param maskCardType The maskCardType
     */
    public void setMaskCardType(String maskCardType) {
        this.maskCardType = maskCardType;
    }

}