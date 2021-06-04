package com.kendao.adblock.enumerable;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.kendao.libgdx.assets.CustomAssetManager;

public enum Assets {
  BAD_LOGIC("badlogic.jpg");

  private final String value;

  Assets(String value) {
    this.value = value;
  }

  public Texture getValueAsTexture() {
    return CustomAssetManager.getInstance().getTexture(this.value);
  }

  public Sound getValueAsSound() {
    return CustomAssetManager.getInstance().getSound(this.value);
  }

  public Music getValueAsMusic() {
    return CustomAssetManager.getInstance().getMusic(this.value);
  }
}
