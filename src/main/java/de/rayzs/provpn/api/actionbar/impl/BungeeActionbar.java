package de.rayzs.provpn.api.actionbar.impl;

import de.rayzs.provpn.api.actionbar.TextBar;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatMessageType;

public class BungeeActionbar implements TextBar {

    @Override
    public void execute(Object object, String text) {
        ProxiedPlayer player = (ProxiedPlayer) object;
        player.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(text));
    }
}
