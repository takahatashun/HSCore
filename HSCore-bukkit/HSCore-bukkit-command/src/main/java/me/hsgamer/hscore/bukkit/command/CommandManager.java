package me.hsgamer.hscore.bukkit.command;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The command manager
 */
public final class CommandManager {

  private final Map<String, Command> registered = new HashMap<>();
  private final JavaPlugin plugin;
  private final Field knownCommandsField;
  private final CommandMap bukkitCommandMap;
  private Method syncCommandsMethod;

  /**
   * Create a new command manager
   *
   * @param plugin the plugin
   */
  public CommandManager(JavaPlugin plugin) {
    this.plugin = plugin;
    try {
      Method commandMapMethod = Bukkit.getServer().getClass().getMethod("getCommandMap");
      bukkitCommandMap = (CommandMap) commandMapMethod.invoke(Bukkit.getServer());

      knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
      knownCommandsField.setAccessible(true);
    } catch (ReflectiveOperationException e) {
      throw new ExceptionInInitializerError(e);
    }

    try {
      Class<?> craftServer = Bukkit.getServer().getClass();
      syncCommandsMethod = craftServer.getDeclaredMethod("syncCommands");
    } catch (Exception e) {
      // Ignored
    }
    if (syncCommandsMethod != null) {
      syncCommandsMethod.setAccessible(true);
    }
  }

  /**
   * Register the command
   *
   * @param command the command object
   */
  public void register(Command command) {
    String name = command.getLabel();
    if (registered.containsKey(name)) {
      plugin.getLogger().log(Level.WARNING, "Duplicated \"{0}\" command ! Ignored", name);
      return;
    }

    bukkitCommandMap.register(plugin.getName(), command);
    registered.put(name, command);
  }

  /**
   * Unregister the command
   *
   * @param command the command object
   */
  public void unregister(Command command) {
    try {
      Map<?, ?> knownCommands = (Map<?, ?>) knownCommandsField.get(bukkitCommandMap);

      knownCommands.values().removeIf(command::equals);

      command.unregister(bukkitCommandMap);
      registered.remove(command.getLabel());
    } catch (ReflectiveOperationException e) {
      plugin.getLogger()
          .log(Level.WARNING, "Something wrong when unregister the command", e);
    }
  }

  /**
   * Unregister the command
   *
   * @param command the command label
   */
  public void unregister(String command) {
    if (registered.containsKey(command)) {
      unregister(registered.remove(command));
    }
  }

  /**
   * Sync the commands to the server. Mainly used to make tab completer work in 1.13+
   */
  public void syncCommand() {
    if (syncCommandsMethod == null) {
      return;
    }

    try {
      syncCommandsMethod.invoke(plugin.getServer());
    } catch (IllegalAccessException | InvocationTargetException e) {
      plugin.getLogger().log(Level.WARNING, "Error when syncing commands", e);
    }
  }

  /**
   * Get registered commands
   *
   * @return the map contains the name and the command object
   */
  public Map<String, Command> getRegistered() {
    return registered;
  }
}