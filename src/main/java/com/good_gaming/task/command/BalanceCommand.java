package com.good_gaming.task.command;

import com.good_gaming.task.Task;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Class to handle the execution of the /balance command.
 * The command should send the player their current balance (which should've been cached on login).
 *
 * Created by Josh (MacBook).
 */
public class BalanceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("Sorry, player command only!");
            return true;
        }

        Player player = (Player) commandSender;

        int playerBalance = Task.getInstance().getBalanceManager().getCachedBalance(player.getUniqueId());
        if(playerBalance == -1) {
            // No data appears to be cached for this player - database issue?
            player.sendMessage(ChatColor.RED + "Sorry, it doesn't look like your data has loaded properly!");
            player.sendMessage(ChatColor.RED + "Please try again and/or let the server management team know.");
            return true;
        }
        player.sendMessage(ChatColor.DARK_GREEN + "Balance: " + ChatColor.GREEN + playerBalance);

        // Log action.
        Task.getInstance().getLogger().info(player.getName() + " has queried their balance!");

        return true;
    }

}
