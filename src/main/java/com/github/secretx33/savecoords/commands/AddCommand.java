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

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class AddCommand implements CommandExecutor, TabCompleter {

    private final CoordRepo coordRepo;

    public AddCommand(JavaPlugin plugin, CoordRepo coordRepo) {
        this.coordRepo = checkNotNull(coordRepo);;
        plugin.getCommand("addcoords").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You may only add coordinates while logged in.");
            return true;
        }
        Player player = (Player) sender;

        if (strings.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /" + alias + " <name> <description>");
            return true;
        }

        String name = strings[0].trim();
        StringBuilder sb = new StringBuilder();
        for(int i=1; i < strings.length; i++){
            sb.append(strings[i]);
            if (i + 1 < strings.length) sb.append(" ");
        }
        Coordinate coordinate = new Coordinate(name, sb.toString(), player.getLocation());

        if (coordRepo.hasCoord(player, coordinate.getName())) {
            player.sendMessage(ChatColor.RED + "Coordinate with name '" + coordinate.getName() + "' already exists.");
            return true;
        }

        if (coordRepo.hasCoord(player, player.getLocation())) {
            player.sendMessage(ChatColor.RED + "There is already another coordinate at " + coordinate.getX() + " " + coordinate.getY() + " " + coordinate.getZ() + ".");
            return true;
        }

        coordRepo.addCoord(player, coordinate);
        player.sendMessage(ChatColor.GREEN + "Successfully added coordinates.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] strings) {
        if (!(sender instanceof Player)) return Collections.emptyList();
        if (strings.length == 1 && strings[0].length() == 0) return Collections.singletonList("<name>");
        if (strings.length == 2 && strings[1].length() == 0) return Collections.singletonList("<description>");
        return Collections.emptyList();
    }
}
