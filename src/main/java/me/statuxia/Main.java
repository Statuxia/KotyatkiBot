package me.statuxia;

import me.statuxia.config.JSONManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Icon;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static Guild guild;
    private static boolean nightBanner = false;

    public static void main(String[] args) throws InterruptedException, IOException {
        String token;
        long guildID;
        JSONObject json = JSONManager.of(System.getProperty("user.dir") + "/config.json")
                .getJsonObject();
        if (!json.has("token") || !json.has("guildID") || json.get("token").equals("default")) {
            System.out.println("First start! Update config.json");
            return;
        }
        token = json.getString("token");
        guildID = json.getLong("guildID");
        JDA jda = JDABuilder.createLight(token)
                .build();
        jda.getGatewayIntents();
        jda.awaitReady();

        guild = jda.getGuildById(guildID);
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new RunnableTask(), 5, 1, TimeUnit.SECONDS);
    }

    static class RunnableTask implements Runnable {
        @Override
        public void run() {
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
            if (now.getHour() > 21 || now.getHour() < 9) {
                if (nightBanner) {
                    return;
                }
                nightBanner = true;
                try {
                    InputStream nightBannerStream = Main.class.getClassLoader().getResourceAsStream("IMG_8288.png");
                    Icon nightBannerIcon = Icon.from(nightBannerStream);
                    guild.getManager().setBanner(nightBannerIcon);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            if (!nightBanner) {
                return;
            }
            nightBanner = false;
            try {
                InputStream nightBannerStream = Main.class.getClassLoader().getResourceAsStream("IMG_8288.png");
                Icon nightBannerIcon = Icon.from(nightBannerStream);
                guild.getManager().setBanner(nightBannerIcon);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}