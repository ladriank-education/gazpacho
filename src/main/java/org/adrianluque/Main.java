package org.adrianluque;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.adrianluque.bot.Bot;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;

public class Main {
    public static void main(String[] args) throws Exception {
        Bot bot = new Bot();

        DiscordApi api = new DiscordApiBuilder().setToken(bot.config.getToken()).setAllIntents().login().join();

        System.out.println(bot.config.username + "#" + bot.config.tag + " is online now!\n");

        for (final Bot.Command cmd : bot.commands) {
            api.addMessageCreateListener(event -> {
                try {
                    if (!((User) event.getMessageAuthor().asUser().get()).getDiscriminatedName()
                            .equals(bot.config.username)) {
                        if (cmd.prefix == null) {
                            if (event.getMessageContent().equalsIgnoreCase(bot.config.defaultPrefix + cmd.name)) {
                                String message = "";
                                for (String param : cmd.getActionParameters()) {
                                    message += param + " ";
                                }
                                if (!message.isBlank()) {
                                    event.getChannel().sendMessage(message);
                                }
                            }
                        } else {
                            if (event.getMessageContent().startsWith(cmd.prefix)
                                    && event.getMessageContent().equalsIgnoreCase(cmd.name)) {
                                String message = "";
                                for (String param : cmd.getActionParameters()) {
                                    message += param + " ";
                                }

                                if (!message.isBlank()) {
                                    event.getChannel().sendMessage(message);
                                }
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        }

    }
}
