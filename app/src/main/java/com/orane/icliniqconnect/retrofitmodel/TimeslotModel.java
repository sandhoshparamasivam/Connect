package com.orane.icliniqconnect.retrofitmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimeslotModel {

@SerializedName("isSlotAvail")
@Expose
private int isSlotAvail;
@SerializedName("slot_type")
@Expose
private String slotType;
@SerializedName("content")
@Expose
private List<Content> content = null;
@SerializedName("timezone")
@Expose
private String timezone;
@SerializedName("status")
@Expose
private int status;
@SerializedName("target_date")
@Expose
private String targetDate;

public int getIsSlotAvail() {
return isSlotAvail;
}

public void setIsSlotAvail(int isSlotAvail) {
this.isSlotAvail = isSlotAvail;
}

public String getSlotType() {
return slotType;
}

public void setSlotType(String slotType) {
this.slotType = slotType;
}

public List<Content> getContent() {
return content;
}

public void setContent(List<Content> content) {
this.content = content;
}

public String getTimezone() {
return timezone;
}

public void setTimezone(String timezone) {
this.timezone = timezone;
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


}