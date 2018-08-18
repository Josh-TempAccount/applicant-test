package com.good_gaming.task;

import com.good_gaming.task.command.BalanceCommand;
import com.good_gaming.task.command.EcoCommand;
import com.good_gaming.task.listener.EntityListener;
import com.good_gaming.task.listener.PlayerListener;
import com.good_gaming.task.manager.BalanceManager;
import com.good_gaming.task.manager.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Main plugin class.
 * Created for Good Gaming, Inc - Minecraft as a Spigot MC Applicant Test.
 *
 * Created by Josh (MacBook).
 */
public class Task extends JavaPlugin {

    public static final String DATABASE = "Economy", PLAYER_TABLE = "Players";

    private static Task instance;

    private String serverVersion;

    private DatabaseManager databaseManager;
    private BalanceManager balanceManager;

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("Checking config exists..");
        if (!this.getDataFolder().exists() || !new File(this.getDataFolder(), "config.yml").exists()) {
            this.saveDefaultConfig();
            getLogger().info("It seems like you don't have a config file! We've copied one over for you, please modify it" +
                    " to reflect your database information then restart the server!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Config exists! Continuing with plugin enable..");
        this.getConfig().options().copyDefaults(true);

        // Fill all fields required for managers to be created.
        this.serverVersion = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf('.') + 1);

        // Create managers.
        this.databaseManager = new DatabaseManager(5);
        this.balanceManager = new BalanceManager();

        // Once managers and fields are ready, register listeners and commands.
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Ensure that all current queries are finished before exiting.
        getDatabaseManager().finish();

        instance = null;
    }

    /**
     * Get static instance of main class.
     * I know some people prefer to pass instances to each class, however I find this easier to work with and manage.
     *
     * @return static instance of main class, won't be null unless plugin hasn't enabled yet/is disabled.
     */
    public static Task getInstance() {
        return instance;
    }

    /**
     * Get the current server version.
     * Not used currently, but could be used if the custom NMS Entities were ever to be implemented in production.
     *
     * @return server version string.
     */
    public String getServerVersion() {
        return serverVersion;
    }

    /**
     * Get the current DatabaseManager instance.
     *
     * @return DatabaseManager instance, won't be null unless plugin hasn't enabled yet/is disabled.
     */
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    /**
     * Get the current BalanceManager instance.
     *
     * @return BalanceManager instance, won't be null unless plugin hasn't enabled yet/is disabled.
     */
    public BalanceManager getBalanceManager() {
        return balanceManager;
    }

    /**
     * Register all plugin listeners.
     */
    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityListener(), this);
    }

    /**
     * Register all plugin commands.
     */
    private void registerCommands() {
        getCommand("balance").setExecutor(new BalanceCommand());
        getCommand("eco").setExecutor(new EcoCommand());
    }

}
