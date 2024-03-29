package com.github.secretx33.savecoords.commands;

import com.github.secretx33.savecoords.repository.CoordRepo;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class RemoveCommand implements CommandExecutor, TabCompleter {

    private final CoordRepo coordRepo;

    public RemoveCommand(JavaPlugin plugin, CoordRepo coordRepo) {
        this.coordRepo = checkNotNull(coordRepo);
        plugin.getCommand("removecoords").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You may only remove coordinates while logged in.");
            return true;
        }
        Player player = (Player) sender;

        if (strings.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /" + alias + " <name>");
            return true;
        }

        String coordName = strings[0];
        if (!coordRepo.hasCoord(player, coordName)) {
            player.sendMessage(ChatColor.RED + "Coordinate with name " + coordName + " doesn't exist.");
            return true;
        }

        coordRepo.removeCoord(player, coordName);
        player.sendMessage(ChatColor.GREEN + "Successfully removed coordinate " + coordName + ".");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] strings) {
        if (!(sender instanceof Player) || strings.length != 1)
            return Collections.emptyList();

        List<String> coordinates = coordRepo.getNamesOfAll((Player) sender);
        if (coordinates.size() > 0) {
            List<String> filteredList = StringUtil.copyPartialMatches(strings[strings.length -1], coordinates, new ArrayList<>());
            Collections.sort(filteredList);
            return filteredList;
        }
        return Collections.singletonList("<name>");
    }
}
