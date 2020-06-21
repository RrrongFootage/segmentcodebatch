package com.learnbatch.segmentcodebatch.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SegmentCode {
  private String sk;
  private String code;
  private String description;
  private int rank;
  private boolean requiresMemo;
  private int DefaultDuration;
  private int Color;
  private int FontColor;
  private UpdatedBy UpdatedBy;
  private String UpdatedOn;

  @JsonIgnore
  @JsonProperty(value = "SK")
  public String getSk() {
    return sk;
  }

  public void setSk(String sk) {
    this.sk = sk;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @JsonIgnore
  @JsonProperty(value = "Rank")
  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }


  public boolean isRequiresMemo() {
    return requiresMemo;
  }

  public void setRequiresMemo(boolean requiresMemo) {
    this.requiresMemo = requiresMemo;
  }

  @JsonIgnore
  @JsonProperty(value = "Color")
  public int getColor() {
    return Color;
  }

  public void setColor(int color) {
    this.Color = color;
  }

  @JsonIgnore
  @JsonProperty(value = "FontColor")
  public int getFontColor() {
    return FontColor;
  }

  public void setFontColor(int fontColor) {
    this.FontColor = fontColor;
  }

  @JsonIgnore
  @JsonProperty(value = "DefaultDuration")
  public int getDefaultDuration() {
    return DefaultDuration;
  }

  public void setDefaultDuration(int defaultDuration) {
    this.DefaultDuration = defaultDuration;
  }

  @JsonIgnore
  @JsonProperty(value = "UpdatedBy")
  public UpdatedBy getUpdatedBy() {
    return UpdatedBy;
  }

  public void setUpdatedBy(UpdatedBy updatedBy) {
    this.UpdatedBy = updatedBy;
  }

  @JsonIgnore
  @JsonProperty(value = "UpdatedOn")
  public String getUpdatedOn() {
    return UpdatedOn;
  }

  public void setUpdatedOn(String updatedOn) {
    this.UpdatedOn = updatedOn;
  }
}
