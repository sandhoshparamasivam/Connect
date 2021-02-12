package com.orane.icliniqconnect.retrofitmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SlotTypeModel {
    @SerializedName("content")
    @Expose
    private List<Content> content = null;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("target_date")
    @Expose
    private String targetDate;
    @SerializedName("slot_type")
    @Expose
    private String slotType;

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }

    public String getSlotType() {
        return slotType;
    }

    public void setSlotType(String slotType) {
        this.slotType = slotType;
    }
}