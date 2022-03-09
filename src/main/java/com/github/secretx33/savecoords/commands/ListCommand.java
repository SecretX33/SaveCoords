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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class ListCommand implements CommandExecutor, TabCompleter {

    private final CoordRepo coordRepo;

    public ListCommand(JavaPlugin plugin, CoordRepo coordRepo) {
        this.coordRepo = checkNotNull(coordRepo);
        plugin.getCommand("listcoords").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("The console doesn't have coordinates.");
            return true;
        }
        Player player = (Player) sender;

        List<Coordinate> coordinates = coordRepo.getAllOf(player).stream()
                .sorted(Comparator.comparing(Coordinate::getWorld).thenComparing(Coordinate::getName))
                .collect(Collectors.toList());
        if (coordinates.isEmpty()) {
            sender.sendMessage("You don't have any coordinates.");
            return true;
        }

        int i = 0;
        StringBuilder sb = new StringBuilder(ChatColor.DARK_PURPLE.toString()).append("Saved coordinates (").append(coordinates.size()).append("):\n");
        for (Coordinate coord : coordinates) {
            sb.append(ChatColor.BLUE);
            sb.append(++i);
            sb.append(". ");
            sb.append(ChatColor.GOLD);
            sb.append(coord.getName());
            sb.append(ChatColor.RESET);
            sb.append(": ");
            sb.append(coord.getDescription());
            sb.append(ChatColor.AQUA);
            sb.append(String.format(" (%d, %d, %d, %s)", coord.getX(), coord.getY(), coord.getZ(), coord.getWorld()));
            if (i < coordinates.size()) sb.append('\n');
        }
        player.sendMessage(sb.toString());
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
