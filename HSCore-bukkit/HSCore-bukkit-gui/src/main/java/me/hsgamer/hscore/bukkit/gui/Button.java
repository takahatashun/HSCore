package me.hsgamer.hscore.bukkit.gui;

import me.hsgamer.hscore.bukkit.gui.button.DummyButton;
import me.hsgamer.hscore.ui.property.Initializable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * An interface for all button
 */
public interface Button extends Initializable {

  /**
   * The empty button
   */
  Button EMPTY = new DummyButton(null);

  /**
   * Get the item stack for the unique id
   *
   * @param uuid the unique id
   *
   * @return the item stack
   */
  ItemStack getItemStack(UUID uuid);

  /**
   * Handle action for the unique id
   *
   * @param uuid  the unique id
   * @param event the click event
   */
  void handleAction(UUID uuid, InventoryClickEvent event);
}
