package com.cinderybell.sellchange;

import com.cinderybell.sellchange.listeners.VillagerTradeListener;
import com.cinderybell.sellchange.managers.PlayerAttributeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SellChange extends JavaPlugin implements CommandExecutor, TabCompleter {

    private PlayerAttributeManager attributeManager;

    @Override
    public void onEnable() {
        // 保存默认配置
        saveDefaultConfig();

        // 版本检查
        String version = Bukkit.getBukkitVersion();
        if (!version.startsWith("1.14") && !version.startsWith("1.15") && !version.startsWith("1.16") &&
                !version.startsWith("1.17") && !version.startsWith("1.18") && !version.startsWith("1.19") &&
                !version.startsWith("1.20")) {
            getLogger().warning("检测到较旧或较新的版本，交易修改功能可能不稳定！建议 1.16.5 - 1.20.x");
        }

        attributeManager = new PlayerAttributeManager();

        // 注册监听器
        getServer().getPluginManager().registerEvents(new VillagerTradeListener(this), this);

        // 注册命令
        getCommand("sellchange").setExecutor(this);
        getCommand("sellchange").setTabCompleter(this);

        getLogger().info("SellChange 已启用！作者：cinderybell6765");
    }

    @Override
    public void onDisable() {
        getLogger().info("SellChange 已禁用。");
    }

    public PlayerAttributeManager getAttributeManager() {
        return attributeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("sellchange.admin")) {
            sender.sendMessage(ChatColor.RED + "你没有权限执行此命令。");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.YELLOW + "用法：/sellchange <setscore|reload> [玩家名] [分数]");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "配置文件已重载。");
            return true;
        }

        if (args[0].equalsIgnoreCase("setscore")) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "用法：/sellchange setscore <玩家名> <分数>");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "玩家不在线。");
                return true;
            }
            int score;
            try {
                score = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "分数必须是数字。");
                return true;
            }
            attributeManager.setScore(target, score);
            sender.sendMessage(ChatColor.GREEN + "已设置 " + target.getName() + " 的任务分数为：" + score);
            target.sendMessage(ChatColor.GREEN + "你的任务进度已更新，村民交易价格可能已变化！");
            return true;
        }

        sender.sendMessage(ChatColor.RED + "未知命令。");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("setscore", "reload");
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("setscore")) {
            List<String> names = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                names.add(p.getName());
            }
            return names;
        }
        return null;
    }
}