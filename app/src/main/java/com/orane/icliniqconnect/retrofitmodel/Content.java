package com.orane.icliniqconnect.retrofitmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Content {

        @SerializedName("railmin")
        @Expose
        private Integer railmin;
        @SerializedName("displaytime")
        @Expose
        private String displaytime;

        public Integer getRailmin() {
            return railmin;
        }

        public void setRailmin(Integer railmin) {
            this.railmin = railmin;
        }

        public String getDisplaytime() {
            return displaytime;
        }

        public void setDisplaytime(String displaytime) {
            this.displaytime = displaytime;
        }

    }