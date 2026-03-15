package com.cinderybell.sellchange.listeners;

import com.cinderybell.sellchange.SellChange;
import com.cinderybell.sellchange.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class VillagerTradeListener implements Listener {

    private final SellChange plugin;

    public VillagerTradeListener(SellChange plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onVillagerAcquireTrade(VillagerAcquireTradeEvent event) {
        // 修改村民获取交易时的价格
        MerchantRecipe recipe = event.getRecipe();
        adjustRecipePrice(recipe);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = (Player) event.getPlayer();

        // 检查是否是村民交易界面
        if (event.getInventory().getHolder() instanceof Villager) {
            Villager villager = (Villager) event.getInventory().getHolder();

            // 添加自定义交易
            if (plugin.getConfig().getBoolean("enable-custom-trades", true)) {
                addCustomTrades(villager, player);
            }
        }
    }

    private void adjustRecipePrice(MerchantRecipe recipe) {
        List<ItemStack> ingredients = recipe.getIngredients();
        int playerScore = 0; // 需要从玩家获取分数

        int discountThreshold = plugin.getConfig().getInt("pricing.discount-per-points", 10);
        int minPrice = plugin.getConfig().getInt("pricing.min-price", 1);

        for (int i = 0; i < ingredients.size(); i++) {
            ItemStack ingredient = ingredients.get(i);
            if (ingredient != null && ingredient.getType().name().contains("EMERALD")) {
                int originalAmount = ingredient.getAmount();
                int discount = playerScore / discountThreshold;
                int newAmount = Math.max(minPrice, originalAmount - discount);
                ingredient.setAmount(newAmount);
                break;
            }
        }
    }

    private void addCustomTrades(Villager villager, Player player) {
        List<MerchantRecipe> recipes = villager.getRecipes();
        // 添加自定义交易逻辑
    }
}