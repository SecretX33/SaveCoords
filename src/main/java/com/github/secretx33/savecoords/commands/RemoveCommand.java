package com.github.secretx33.savecoords.commands;

import com.github.secretx33.savecoords.model.Coordinate;
import com.github.secretx33.savecoords.repository.CoordRepo;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@ParametersAreNonnullByDefault
public class RemoveCommand implements CommandExecutor, TabCompleter {

    private final CoordRepo coordRepo;

    public RemoveCommand(JavaPlugin plugin, CoordRepo coordRepo) {
        checkNotNull(plugin);
        checkNotNull(coordRepo);
        this.coordRepo = coordRepo;
        plugin.getCommand("removecoords").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("You may only add coordinates while logged in.");
            return true;
        }
        Player player = (Player) commandSender;

        if(strings == null || strings.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /" + s + " <name>");
            return true;
        }

        String coordName = strings[0];
        if(!coordRepo.hasCoord(player, coordName)) {
            player.sendMessage(ChatColor.RED + "Coordinate with name " + coordName + " doesn't exist.");
            return true;
        }

        coordRepo.removeCoord(player, coordName);
        player.sendMessage(ChatColor.GREEN + "Successfully removed coordinate " + coordName + ".");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings) {
        if(!(sender instanceof Player)) {
            return new ArrayList<>();
        }
        if(strings.length == 1) {
            List<String> coordinates = coordRepo.getAllOf((Player) sender);
            if(coordinates.size() > 0) return coordinates;
            return Collections.singletonList("<name>");
        }
        return new ArrayList<>();
    }
}
