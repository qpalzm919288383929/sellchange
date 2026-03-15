package com.cinderybell.sellchange.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    public static ItemStack buildFromConfig(ConfigurationSection section) {
        if (section == null) return new ItemStack(Material.STONE);

        Material material = Material.getMaterial(section.getString("material", "STONE").toUpperCase());
        if (material == null) material = Material.STONE;

        int amount = section.getInt("amount", 1);
        ItemStack item = new ItemStack(material, amount);

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (section.contains("name")) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', section.getString("name")));
            }
            if (section.contains("lore")) {
                List<String> lore = section.getStringList("lore");
                List<String> coloredLore = new ArrayList<>();
                for (String line : lore) {
                    coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));
                }
                meta.setLore(coloredLore);
            }
            if (section.contains("custom-model-data")) {
                meta.setCustomModelData(section.getInt("custom-model-data"));
            }
            item.setItemMeta(meta);
        }
        return item;
    }
}