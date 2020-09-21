package me.hsgamer.hscore.config.path;

import me.hsgamer.hscore.config.ConfigPath;

public class BooleanConfigPath extends ConfigPath<Boolean> {

  /**
   * Create a config path
   *
   * @param path the path to the value
   * @param def  the default value if it's not found
   */
  public BooleanConfigPath(String path, Boolean def) {
    super(path, def, o -> Boolean.parseBoolean(String.valueOf(o)));
  }
}