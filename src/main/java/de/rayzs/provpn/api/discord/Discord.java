package de.rayzs.provpn.api.discord;

import de.rayzs.provpn.api.configuration.Settings;
import de.rayzs.provpn.plugin.logger.Logger;
import java.time.*;
import java.util.*;

public class Discord {

    private static final List<Webhook> WEBHOOKS = new ArrayList<>();
    private static long TIME = System.currentTimeMillis();

    public static void initialize() {
        if(!WEBHOOKS.isEmpty()) WEBHOOKS.clear();
        boolean ENABLED = Settings.DETECTED_WEBHOOK_ENABLED;

        if(ENABLED) for (String url : Settings.DETECTED_WEBHOOK_URLS)
            try {
                Webhook webhook = new Webhook(url);
                webhook.setUsername(Settings.DETECTED_WEBHOOK_NAME);
                webhook.setAvatarUrl(Settings.DETECTED_WEBHOOK_AVATAR);
                WEBHOOKS.add(webhook);
            }catch (Exception exception) {
                Logger.warning("Failed to create webhook! [url=" + url + "]");
            }
    }

    public static void sendEmbed(String name, String address) {
        for (Webhook webhook : WEBHOOKS) {
            try {
                long calcTime = System.currentTimeMillis() - TIME;
                if(calcTime < 100) return;
                TIME = calcTime;
                String date = LocalDate.now().toString().replace("-", ".");
                String time = LocalTime.now().toString();
                if(time.contains(".")) time = time.split("\\.")[0];
                Webhook.EmbedObject embedObject = new Webhook.EmbedObject();
                for (int i = 0; i < Settings.DETECTED_WEBHOOK_MESSAGE.size(); i++) {
                    String field = (Settings.DETECTED_WEBHOOK_MESSAGE.get(i)
                            .replace("%PLAYER%", name)
                            .replace("%ADDRESS%", address)
                            .replace("%DATE%", date)
                            .replace("%TIME%", time)
                    );

                    if(field.contains("%%%%")) {
                        String[] fieldSplit = field.split("%%%%");
                        embedObject.addField(fieldSplit[0], fieldSplit[1], true);
                    } else embedObject.setDescription(field);
                }
                embedObject.setTitle(Settings.DETECTED_WEBHOOK_TITLE
                                .replace("%PLAYER%", name)
                                .replace("%ADDRESS%", address)
                                .replace("%DATE%", date)
                                .replace("%TIME%", time));
                embedObject.setFooter(Settings.DETECTED_WEBHOOK_FOOTER
                        .replace("%PLAYER%", name)
                        .replace("%ADDRESS%", address)
                        .replace("%DATE%", date)
                        .replace("%TIME%", time), "");
                webhook.addEmbed(embedObject);
                webhook.execute();
            }catch (Exception exception) {
                Logger.warning("Failed to send embed! Webhook down? [url=" + webhook.getUrl() + "]");
                exception.printStackTrace();
            }
        }
    }
}
