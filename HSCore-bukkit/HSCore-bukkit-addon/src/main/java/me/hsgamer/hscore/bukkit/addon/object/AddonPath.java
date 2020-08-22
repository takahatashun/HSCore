package me.hsgamer.hscore.bukkit.addon.object;

import me.hsgamer.hscore.bukkit.addon.exception.RequiredAddonPathException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * A path to get value from addon.yml
 *
 * @param <T> the type of the final value
 */
public abstract class AddonPath<T> {

  private final String path;
  private final boolean required;

  /**
   * Create an addon path
   *
   * @param path     the path to the value
   * @param required is it required to be in addon.yml
   */
  public AddonPath(String path, boolean required) {
    this.path = path;
    this.required = required;
  }

  /**
   * Check if the path is required to be in addon.yml
   *
   * @return is it required to be in addon.yml
   */
  public boolean isRequired() {
    return required;
  }

  /**
   * Get the path to the value
   *
   * @return the path
   */
  public String getPath() {
    return path;
  }

  /**
   * Convert the type of the value from addon.yml
   *
   * @param object the raw value from addon.yml
   * @return the converted value
   */
  public abstract T convertType(Object object);

  /**
   * Get the value from addon.yml
   *
   * @param addon the addon
   * @return the value
   * @throws RequiredAddonPathException if the path is required but is not found in addon.yml
   */
  public T get(Addon addon) {
    YamlConfiguration configuration = addon.getDescription().getConfiguration();
    if (required && !configuration.isSet(path)) {
      throw new RequiredAddonPathException(
          path + " is not found in the addon '" + addon.getDescription().getName() + "'");
    }

    Object value = configuration.get(path);
    return value != null ? convertType(value) : null;
  }
}