package com.cinderybell.sellchange.managers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerAttributeManager {

    // 内存中存储玩家分数，实际生产建议使用数据库或 YAML 持久化
    private final Map<UUID, Integer> playerScores = new HashMap<>();

    public int getScore(Player player) {
        return playerScores.getOrDefault(player.getUniqueId(), 0);
    }

    public void setScore(Player player, int score) {
        playerScores.put(player.getUniqueId(), score);
    }
    
    public void setScore(OfflinePlayer player, int score) {
        playerScores.put(player.getUniqueId(), score);
    }

    // 模拟获取任务完成数量
    public int getQuestCount(Player player) {
        return getScore(player);
    }
}