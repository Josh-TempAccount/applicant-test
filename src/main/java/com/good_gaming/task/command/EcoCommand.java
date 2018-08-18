package com.good_gaming.task.command;

import com.good_gaming.task.Task;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Class to handle the execution of the /eco command.
 * The command should allow the giving of 'balance' to an online player.
 * Permissions are handled by the plugin.yml.
 *
 * Created by Josh (MacBook).
 */
public class EcoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(args.length != 3 || !args[0].equalsIgnoreCase("give")) {
            commandSender.sendMessage("Correct Usage:");
            commandSender.sendMessage("eco give <player> <amount>");
            return true;
        }

        Player targetPlayer = Bukkit.getPlayerExact(args[1]);
        if(targetPlayer == null) {
            commandSender.sendMessage("This player is not online! Please try again.");
            return true;
        }

        if(!StringUtils.isNumeric(args[2])) {
            commandSender.sendMessage("The give amount must be a whole, positive number!");
            return true;
        }
        int giveAmount = Integer.valueOf(args[2]);
        if(giveAmount < 1) {
            commandSender.sendMessage("The give amount must be above 0!");
            return true;
        }

        // All preconditions are now met, we can now proceed to modifying the player's balance.]
        // It will be updated in the database on logout.
        Task.getInstance().getBalanceManager().updateBalance(targetPlayer.getUniqueId(),
                Task.getInstance().getBalanceManager().getCachedBalance(targetPlayer.getUniqueId()) + giveAmount);
        commandSender.sendMessage("Balance adjusted successfully!");
        return true;
    }

}
