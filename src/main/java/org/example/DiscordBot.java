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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class DiscordBot {
    private static final JDA bot = JDABuilder.createDefault("MTIwNTUwMjgyMTYzODg3MzE0OA.Gp6oUs.UelgnS7klD31eaQNSz086ZcbK166ojgt23Y0hc").setActivity(Activity.customStatus("Scraping Bazos 😎")).build();

    private static void sendImage(FileUpload img, String inzeratURL ,ImageCallback callback) {
        try {
            bot.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        img.setName(img.getName() + ".jpg");
        TextChannel textChannel = bot.getTextChannelById("1063949035054051409");
        textChannel.sendMessage(inzeratURL).addFiles(img).queue(callback::onImageSent);


    }
    private static void getImageURL(ImageUrlCallback callback) {
        try {
            bot.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        TextChannel textChannel = bot.getTextChannelById("1063949035054051409");
        String latestID = textChannel.getLatestMessageId();
        textChannel.retrieveMessageById(latestID).queue((message) -> {
            String imgURL = message.getAttachments().get(0).getUrl();
            System.out.println(imgURL);
            message.addReaction(Emoji.fromUnicode("U+2705")).queue();
            callback.onImageUrlReceived(imgURL);

        }, new ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE, (e) -> {
            throw new RuntimeException(e);
        }));


    }
    public static void storeImage(Inzerat inzerat, ImageSentCallback callback) {

        InputStream input = null;
        try {
            input = new java.net.URL(inzerat.img).openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileUpload inputUpload = FileUpload.fromData(input,  inzerat.nadpis);
            sendImage(inputUpload, inzerat.url,(message -> {
                getImageURL(new ImageUrlCallback() {
                    @Override
                    public void onImageUrlReceived(String imageUrl) {
                        inzerat.img = imageUrl;
                        callback.onImageSent();
                    }
                });
            }));

    }
    public static void exit() {
        try {
            bot.awaitShutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
interface ImageCallback {
    void onImageSent(Message message);
}
interface ImageSentCallback {
    void onImageSent();
}
interface ImageUrlCallback {
    void onImageUrlReceived(String imageUrl);
}

