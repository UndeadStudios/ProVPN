package de.rayzs.provpn.api.actionbar.impl;

import de.rayzs.provpn.api.actionbar.TextBar;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.entity.Player;

public class BukkitAPIActionbar implements TextBar {

    @Override
    public void execute(Object object, String text) {
        Player player = (Player) object;
        if(player == null) return;
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(text));
    }
}
