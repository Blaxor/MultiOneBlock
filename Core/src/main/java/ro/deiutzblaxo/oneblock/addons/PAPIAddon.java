package ro.deiutzblaxo.oneblock.addons;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ro.deiutzblaxo.oneblock.OneBlock;
import ro.deiutzblaxo.oneblock.island.Island;
import ro.deiutzblaxo.oneblock.player.PlayerOB;

public class PAPIAddon extends PlaceholderExpansion {
    private OneBlock plugin;

    public PAPIAddon(OneBlock plugin) {
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        PlayerOB playerOB = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        if (playerOB == null) {
            return "";
        }
        Island island = playerOB.getIsland(false);
        if (island == null)
            return "";
        if (params.equalsIgnoreCase("level"))
            return "" + island.getLevel();
        if (params.equalsIgnoreCase("tier"))
            return island.getMeta().getRadiusTire() + "";
        if (params.equalsIgnoreCase("count"))
            return island.getMeta().getCount() + "";
        if (params.equalsIgnoreCase("phase"))
            return ChatColor.translateAlternateColorCodes('&', island.getPhase().getPhaseName());
        if(params.equalsIgnoreCase("server"))
            return OneBlock.SERVER.replace("-"," ");
        return null;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "oneblock";
    }

    @Override
    public @NotNull String getAuthor() {
        return "JDeiutz";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
