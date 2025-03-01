package com.mbfsrv.antispam;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class AntiSpam extends JavaPlugin implements Listener {

    private final Map<UUID, ChatInfo> chatLogs = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("プラグインが起動しました");

        // 5分ごとにデータをリセットするタスクをスケジュール
        new BukkitRunnable() {
            @Override
            public void run() {
                resetChatLogs();
            }
        }.runTaskTimer(this, 0, 6000); // 6000 ticks = 5 minutes
    }

    @Override
    public void onDisable() {
        getLogger().info("プラグインが停止しました");
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().toLowerCase(); // コマンドを小文字で取得する
        // コマンドが /kill または /kill @e の場合にキャンセルする
        if (command.startsWith("/kill")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("You cannot use the /kill command.");
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        String message = event.getMessage();

        getLogger().info("Received message: " + message + " from " + event.getPlayer().getName());

        ChatInfo chatInfo = chatLogs.getOrDefault(playerId, new ChatInfo());
        if (chatInfo.isSameMessage(message)) {
            chatInfo.incrementCount();
            getLogger().info("Count for " + event.getPlayer().getName() + ": " + chatInfo.getCount());
            if (chatInfo.getCount() == 3) {
                getLogger().info("Player " + event.getPlayer().getName() + " has sent the same message 3 times in a row.");
                // コンソールから警告コマンドを同期スレッドで実行
                //executeConsoleCommand("kick " + event.getPlayer().getName() + " Spamming is not allowed!");
                executeConsoleCommand("tempmute " + event.getPlayer().getName() + " 5m 連投スパムを検出しました。");
            }
        } else {
            chatInfo.resetCount(message);
            getLogger().info("Reset count for " + event.getPlayer().getName());
        }

        chatLogs.put(playerId, chatInfo);
    }

    private void resetChatLogs() {
        getLogger().info("Resetting chat logs");
        chatLogs.clear();
    }

    private void executeConsoleCommand(String command) {
        Bukkit.getScheduler().runTask(this, () -> {
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            Bukkit.dispatchCommand(console, command);
        });
    }

    private static class ChatInfo {
        private String lastMessage;
        private int count;

        public boolean isSameMessage(String message) {
            return message.equals(lastMessage);
        }

        public void resetCount(String message) {
            this.lastMessage = message;
            this.count = 1;
        }

        public void incrementCount() {
            this.count++;
        }

        public int getCount() {
            return count;
        }
    }
}
