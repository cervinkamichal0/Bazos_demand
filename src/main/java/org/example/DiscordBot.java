package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.utils.FileUpload;


import java.awt.image.DataBufferByte;
import java.io.File;

import static java.lang.Thread.sleep;

public class DiscordBot {
    private static JDA getBot() {
        final JDA bot = JDABuilder.createDefault("MTIwNTUwMjgyMTYzODg3MzE0OA.Gp6oUs.UelgnS7klD31eaQNSz086ZcbK166ojgt23Y0hc").setActivity(Activity.watching("Bazos")).build();
        return bot;
    }
    public static String sendImage(String message) {
        JDA bot = getBot();
        try {
            bot.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
            FileUpload img = FileUpload.fromData(new File("D:\\java\\scraper_2.0\\src\\main\\resources\\test.jpg"), "image.png");
            TextChannel textChannel = bot.getTextChannelById("1063949035054051409");
            textChannel.sendFiles(img).queue();

            String imgURL=getImageURL();
            return imgURL;
    }
    private static String getImageURL() {
        JDA bot = getBot();

        try {
            bot.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        TextChannel textChannel = bot.getTextChannelById("1063949035054051409");
        String latestID = textChannel.getLatestMessageId();
        textChannel.retrieveMessageById(latestID).queue((message) -> {
            message.addReaction(Emoji.fromUnicode("U+2714")).queue();
            String imgURL = message.getAttachments().get(0).getUrl();
            System.out.println(imgURL);


        }, new ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE, (e) -> {
            throw new RuntimeException(e);
        }));
        return null;

    }
    //JDA bot = JDABuilder.createDefault("MTIwNTUwMjgyMTYzODg3MzE0OA.GLh6Rm.ClrxTruBsQMEvvM59nnOOfzn-UkG21zwNZsvYQ").setActivity(Activity.watching("Bazos")).build();

}
