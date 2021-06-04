package com.kendao.adblock.enumerable;

public enum Purchases {
  PURCHASE_ID("google_play_purchase_id");

  private final String value;

  Purchases(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
