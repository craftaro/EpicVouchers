package com.craftaro.epicvouchers.libraries.inventory;

import com.craftaro.epicvouchers.libraries.inventory.icons.Icon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A fast API to easily create advanced GUI.
 * The project is on <a href="https://github.com/MrMicky-FR/FastInv">GitHub</a>
 *
 * @author MrMicky
 * @version 2.0.3 - Now supports async operations
 */
public class IconInv implements InventoryHolder {
    private static Plugin plugin = null;
    private boolean cancelTasksOnClose = true, cancelled = true;
    private final Set<IconInvCloseListener> closeListeners = new HashSet<>();
    private final Set<IconClickListener> clickListeners = new HashSet<>();
    private final Map<Integer, Icon> itemListeners = new HashMap<>();
    private final Set<BukkitTask> tasks = new HashSet<>();
    private Inventory inventory;

    /**
     * Create a new FastInv with a custom size.
     *
     * @param size The size of the menus.
     */
    public IconInv(int size) {
        this(size, InventoryType.CHEST.getDefaultTitle());
    }

    /**
     * Create a new FastInv with a custom size and title.
     *
     * @param size  The size of the menus.
     * @param title The title (name) of the menus.
     */
    public IconInv(int size, String title) {
        this(size, InventoryType.CHEST, title);
    }

    /**
     * Create a new FastInv with a custom type.
     *
     * @param type The type of the menus.
     */
    public IconInv(InventoryType type) {
        this(type, type.getDefaultTitle());
    }

    /**
     * Create a new FastInv with a custom type and title.
     *
     * @param type  The type of the menus.
     * @param title The title of the menus.
     * @throws IllegalStateException if FastInv is not init with FastInv.init(Plugin plugin)
     */
    public IconInv(InventoryType type, String title) {
        this(0, type, title);
    }

    private IconInv(int size, InventoryType type, String title) {
        if (plugin == null) {
            throw new IllegalStateException("FastInv is not initialised");
        }

        runSync(() -> {
            if (type == InventoryType.CHEST && size > 0) {
                this.inventory = Bukkit.createInventory(this, size, title);
            } else {
                this.inventory = Bukkit.createInventory(this, type, title);
            }
        });
    }

    /**
     * Register your FastInv instance.
     *
     * @param plugin The plugin that uses FastInv.
     */
    public static void init(Plugin plugin) {
        if (IconInv.plugin == null) {
            IconInv.plugin = plugin;
            Bukkit.getPluginManager().registerEvents(getListener(), plugin);
        }
    }

    /**
     * Add an {@link ItemStack} to the menus with a {@link IconClickListener} to handle clicks.
     *
     * @param icon The icon to add.
     * @return This FastInv instance, for chaining.
     */
    public IconInv addIcon(Icon icon) {
        runSync(() -> {
            int slot = this.inventory.firstEmpty();
            if (slot >= 0) {
                addIcon(slot, icon);
            }
        });
        return this;
    }

    /**
     * Add an {@link ItemStack} to the menus on specific slot with a {@link IconClickListener} to handle clicks.
     *
     * @param slot The slot of the item.
     * @param icon The icon to add.
     * @return This FastInv instance, for chaining.
     */
    public IconInv addIcon(int slot, Icon icon) {
        runSync(() -> {
            this.inventory.setItem(slot, icon.getItemStack());
            this.itemListeners.put(slot, icon);
        });

        return this;
    }

    /**
     * Add an {@link ItemStack} to the menus on a range of slots with a {@link IconClickListener} to handle clicks.
     *
     * @param slotFrom Starting slot to put the item in.
     * @param slotTo   Ending slot to put the item in.
     * @param icon     The icon to add.
     * @return This FastInv instance, for chaining.
     */
    public IconInv addIcon(int slotFrom, int slotTo, Icon icon) {
        for (int i = slotFrom; i <= slotTo; i++) {
            addIcon(i, icon);
        }
        return this;
    }

    /**
     * Add an {@link ItemStack} to the menus on the edges.
     *
     * @param icon The icon to add.
     * @return This FastInv instance, for chaining.
     */
    public IconInv edge(Icon icon) {
        int height = this.inventory.getSize() / 9;

        addIcon(0, 9, icon);
        addIcon(this.inventory.getSize() - 9, this.inventory.getSize() - 1, icon);

        for (int i = 0; i < height; i++) {
            addIcon(i * 9, icon);
            addIcon(i * 9 + 8, icon);
        }

        return this;
    }

    /**
     * Add an {@link ItemStack} to the menus on multiples slots with a {@link IconClickListener} to handle click.
     *
     * @param slots The slots to place the item.
     * @param icon  The icon to add.
     * @return This FastInv instance, for chaining.
     */
    public IconInv addIcon(int[] slots, Icon icon) {
        for (int slot : slots) {
            addIcon(slot, icon);
        }
        return this;
    }

    public IconInv fill(Icon icon) {
        runSync(() -> {
            for (int i = 0; i < this.inventory.getSize(); i++) {
                if (this.inventory.getItem(i) == null) {
                    addIcon(i, icon);
                }
            }
        });
        return this;
    }

    public IconInv addIcon(int slot, ItemStack itemStack) {
        return addIcon(slot, new Icon(itemStack));
    }

    public IconInv addIcon(ItemStack itemStack, Consumer<IconClickEvent> event) {
        return addIcon(new Icon(itemStack, event));
    }

    public IconInv addIcon(int slot, ItemStack itemStack, Consumer<IconClickEvent> event) {
        return addIcon(slot, new Icon(itemStack, event));
    }

    /**
     * Add a {@link IconInvCloseListener} to listen on menus close.
     *
     * @param listener The {@link IconInvCloseListener} to add.
     * @return This FastInv instance, for chaining.
     */
    public IconInv onClose(IconInvCloseListener listener) {
        this.closeListeners.add(listener);
        return this;
    }

    /**
     * Add a {@link IconClickListener} to listen on menus click.
     *
     * @param listener The {@link IconClickListener} to add.
     * @return This FastInv instance, for chaining.
     */
    public IconInv onClick(IconClickListener listener) {
        this.clickListeners.add(listener);
        return this;
    }

    /**
     * Schedule a task to run.
     *
     * @param period   Delay between each run.
     * @param runnable The {@link Runnable} task to run.
     * @return This FastInv instance, for chaining.
     */
    public IconInv onUpdate(long period, Runnable runnable) {
        return onUpdate(period, period, runnable);
    }

    /**
     * Schedule a task to run with a delay before starting.
     *
     * @param delay    Ticks to wait before starting the task.
     * @param period   Delay between each run.
     * @param runnable The {@link Runnable} task to run.
     * @return This FastInv instance, for chaining
     */
    public IconInv onUpdate(long delay, long period, Runnable runnable) {
        this.tasks.add(Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period));
        return this;
    }

    /**
     * Open the menus to a player.
     *
     * @param player The player to open the menu.
     */
    public void open(Player player) {
        Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(this.inventory));
    }

    /**
     * Open the menus to players.
     *
     * @param players The players to open the menu.
     */
    public void open(Player... players) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            for (Player p : players) {
                p.openInventory(this.inventory);
            }
        });
    }

    /**
     * Cancel all tasks.
     */
    public void cancelTasks() {
        this.tasks.forEach(BukkitTask::cancel);
        this.tasks.clear();
    }

    /**
     * Run a task on the server primary thread.
     *
     * @param runnable The runnable to run on the main thread
     */
    public void runSync(Runnable runnable) {
        if (Bukkit.isPrimaryThread()) {
            runnable.run();
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }

    /**
     * Set if the tasks will be cancel on menus close.
     *
     * @param cancelTasksOnClose Set if the tasks will be cancel
     * @return This FastInv instance, for chaining.
     */
    public IconInv setCancelTasksOnClose(boolean cancelTasksOnClose) {
        this.cancelTasksOnClose = cancelTasksOnClose;
        return this;
    }

    public interface IconClickListener {
        void onClick(IconClickEvent event);
    }

    public interface IconInvCloseListener {
        void onClose(IconInvCloseEvent event);
    }

    public abstract static class IconEvent {
        private final Player player;
        private final IconInv inventory;
        private boolean cancelled;

        IconEvent(Player player, IconInv inventory, boolean cancelled) {
            this.player = player;
            this.inventory = inventory;
            this.cancelled = cancelled;
        }

        /**
         * Get the FastInv menus.
         *
         * @return This associated FastInv instance.
         */
        public IconInv getInventory() {
            return this.inventory;
        }

        /**
         * Get the {@link Player} who clicked.
         *
         * @return the player who clicked.
         */
        public Player getPlayer() {
            return this.player;
        }

        /**
         * Get if the event is cancelled or not.
         *
         * @return Whether the event was cancelled.
         */
        public boolean isCancelled() {
            return this.cancelled;
        }

        /**
         * Set if the event will be cancel or not.
         *
         * @param cancel Whether the event should be cancelled.
         */
        public void setCancelled(boolean cancel) {
            this.cancelled = cancel;
        }
    }

    public static class IconClickEvent extends IconEvent {
        private final int slot;
        private final ItemStack item;
        private final InventoryAction action;
        private final ClickType clickType;

        private IconClickEvent(Player player, IconInv inventory, int slot, ItemStack item,
                               boolean cancelled, InventoryAction action, ClickType clickType) {
            super(player, inventory, cancelled);
            this.slot = slot;
            this.item = item;
            this.action = action;
            this.clickType = clickType;
        }

        /**
         * Gets the {@link InventoryAction}
         *
         * @return The action of the event
         */
        public InventoryAction getAction() {
            return this.action;
        }

        /**
         * Gets the {@link ClickType} of the event.
         *
         * @return The click type
         */
        public ClickType getClickType() {
            return this.clickType;
        }

        /**
         * Get the clicked {@link ItemStack}
         *
         * @return The clicked item
         */
        public ItemStack getItem() {
            return this.item;
        }

        /**
         * Get the number of the clicked slot
         *
         * @return The slot number
         */
        public int getSlot() {
            return this.slot;
        }
    }

    public static class IconInvCloseEvent extends IconEvent {
        private IconInvCloseEvent(Player player, IconInv inventory, boolean cancelled) {
            super(player, inventory, cancelled);
        }
    }

    public boolean getDefaultCancel() {
        return this.cancelled;
    }

    public IconInv setDefaultCancel(boolean value) {
        this.cancelled = value;
        return this;
    }

    /**
     * Get the Bukkit menus associated with this FastInv instance.
     *
     * @return The Bukkit {@link Inventory}.
     */
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    private static Listener getListener() {
        return new Listener() {

            @EventHandler
            public void onClick(InventoryClickEvent event) {
                if (event.getInventory().getHolder() instanceof IconInv && event.getWhoClicked() instanceof Player) {
                    int slot = event.getRawSlot();
                    IconInv inv = (IconInv) event.getInventory().getHolder();

                    IconClickEvent clickEvent = new IconClickEvent((Player) event.getWhoClicked(), inv, slot,
                            event.getCurrentItem(), inv.cancelled, event.getAction(), event.getClick());

                    if (inv.itemListeners.containsKey(slot)) {
                        inv.itemListeners.get(slot).run(clickEvent);
                    }

                    inv.clickListeners.forEach(listener -> listener.onClick(clickEvent));

                    if (clickEvent.isCancelled()) {
                        event.setCancelled(true);
                    }
                }
            }

            @EventHandler
            public void onClose(InventoryCloseEvent event) {
                if (event.getInventory().getHolder() instanceof IconInv && event.getPlayer() instanceof Player) {
                    Player player = (Player) event.getPlayer();
                    IconInv inv = (IconInv) event.getInventory().getHolder();

                    IconInvCloseEvent closeEvent = new IconInvCloseEvent(player, inv, false);
                    inv.closeListeners.forEach(listener -> listener.onClose(closeEvent));

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        // Tiny delay to prevent errors.
                        if (closeEvent.isCancelled() && player.isOnline()) {
                            player.openInventory(inv.getInventory());
                        } else if (inv.getInventory().getViewers().isEmpty() && inv.cancelTasksOnClose) {
                            inv.cancelTasks();
                        }
                    });
                }
            }

            @EventHandler
            public void onDisable(PluginDisableEvent event) {
                if (event.getPlugin().equals(plugin)) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.getOpenInventory().getTopInventory().getHolder() instanceof IconInv) {
                            player.closeInventory();
                        }
                    }
                }
            }
        };
    }
}
