package de.lachcrafter.lachshield.features;

import de.lachcrafter.lachshield.ConfigManager;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class IPAccountManager {
    private final Map<String, Integer> ipAccountCount = new HashMap<>();
    private final ConfigManager configManager;
    private final FileConfiguration config;

    public IPAccountManager(ConfigManager configManager, FileConfiguration config) {
        this.configManager = configManager;
        this.config = config;
    }

    public boolean handlePlayerJoin(Player player) {
        String ip = player.getAddress().getAddress().getHostAddress();
        int accountCount = ipAccountCount.getOrDefault(ip, 0);
        int maxAccountsPerIP = configManager.getMaxAccountsPerIP();

        if (!config.getBoolean("join_messages.enabled", false)) {
            return true;
        }

        if (accountCount >= maxAccountsPerIP) {
            Component kickComponent = configManager.getKickMessage();
            player.kick(kickComponent);
            return false;
        }

        ipAccountCount.put(ip, accountCount + 1);
        return true;
    }

    public void handlePlayerQuit(Player player) {
        String ip = player.getAddress().getAddress().getHostAddress();
        int accountCount = ipAccountCount.getOrDefault(ip, 0);

        if (accountCount > 0) {
            ipAccountCount.put(ip, accountCount - 1);
        }
    }
}