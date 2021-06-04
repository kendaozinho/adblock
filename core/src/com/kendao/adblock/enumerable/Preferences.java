package com.kendao.adblock.enumerable;

import com.kendao.libgdx.storage.CustomPreferences;

public enum Preferences {
  PURCHASES_RESTORED("purchases-restored", "false");

  private final String key;
  private final String defaultValue;

  Preferences(String key, String defaultValue) {
    this.key = key;
    this.defaultValue = defaultValue;
  }

  public String getPropertyAsString() {
    return CustomPreferences.getInstance().getPropertyAsString(this.key, this.defaultValue);
  }

  public void setPropertyAsString(String value) {
    CustomPreferences.getInstance().setPropertyAsString(this.key, value);
  }

  public Integer getPropertyAsInteger() {
    return CustomPreferences.getInstance().getPropertyAsInteger(this.key, Integer.parseInt(this.defaultValue));
  }

  public void setPropertyAsInteger(Integer value) {
    CustomPreferences.getInstance().setPropertyAsInteger(this.key, value);
  }

  public Long getPropertyAsLong() {
    return CustomPreferences.getInstance().getPropertyAsLong(this.key, Long.parseLong(this.defaultValue));
  }

  public void setPropertyAsLong(Long value) {
    CustomPreferences.getInstance().setPropertyAsLong(this.key, value);
  }

  public Float getPropertyAsFloat() {
    return CustomPreferences.getInstance().getPropertyAsFloat(this.key, Float.parseFloat(this.defaultValue));
  }

  public void setPropertyAsFloat(Float value) {
    CustomPreferences.getInstance().setPropertyAsFloat(this.key, value);
  }

  public Boolean getPropertyAsBoolean() {
    return CustomPreferences.getInstance().getPropertyAsBoolean(this.key, Boolean.parseBoolean(this.defaultValue));
  }

  public void setPropertyAsBoolean(Boolean value) {
    CustomPreferences.getInstance().setPropertyAsBoolean(this.key, value);
  }
}
