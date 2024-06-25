package com.mile.log.discord;


import com.mile.log.discord.exception.ErrorLogAppenderException;
import com.mile.log.discord.model.Author;
import com.mile.log.discord.model.EmbedObject;
import com.mile.log.discord.model.Field;
import com.mile.log.discord.model.Footer;
import com.mile.log.discord.model.Image;
import com.mile.log.discord.model.JsonObject;
import com.mile.log.discord.model.Thumbnail;
import com.mile.log.utils.ApiCallUtil;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DiscordWebHook {

    private final String urlString;
    private final List<EmbedObject> embeds = new ArrayList<>();
    private String username;
    private String avatarUrl;
    private boolean tts;

    public DiscordWebHook(String urlString, String username, String avatarUrl, boolean tts) {
        this.urlString = urlString;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.tts = tts;
    }

    public void addEmbed(EmbedObject embed) {
        this.embeds.add(embed);
    }

    public void execute() throws IOException {
        if (this.embeds.isEmpty()) {
            throw new RuntimeException("컨텐츠를 설정하거나 하나 이상의 Embed Object를 추가해야 합니다.");
        }

        try {
            ApiCallUtil.callDiscordAppenderPostAPI(
                    this.urlString, createDiscordEmbedObject(
                            this.embeds, initializerDiscordSendForJsonObject(new JsonObject())
                    ));

        } catch (IOException ioException) {
            throw ioException;
        }
    }

    private JsonObject initializerDiscordSendForJsonObject(JsonObject json) {
        json.put("username", this.username);
        json.put("avatar_url", this.avatarUrl);
        json.put("tts", this.tts);
        return json;
    }

    private JsonObject createDiscordEmbedObject(List<EmbedObject> embeds, JsonObject json) {
        if (embeds.isEmpty()) {
            throw new ErrorLogAppenderException();
        }

        List<JsonObject> embedObjects = new ArrayList<>();

        for (EmbedObject embed : embeds) {
            JsonObject jsonEmbed = new JsonObject();

            jsonEmbed.put("title", embed.getTitle());
            jsonEmbed.put("description", embed.getDescription());
            jsonEmbed.put("url", embed.getUrl());

            processDiscordEmbedColor(embed, jsonEmbed);
            processDiscordEmbedFooter(embed.getFooter(), jsonEmbed);
            processDiscordEmbedImage(embed.getImage(), jsonEmbed);
            processDiscordEmbedThumbnail(embed.getThumbnail(), jsonEmbed);
            processDiscordEmbedAuthor(embed.getAuthor(), jsonEmbed);
            processDiscordEmbedMessageFields(embed.getFields(), jsonEmbed);

            embedObjects.add(jsonEmbed);
        }
        json.put("embeds", embedObjects.toArray());

        return json;
    }

    private void processDiscordEmbedColor(EmbedObject embed, JsonObject jsonEmbed) {
        if (embed.getColor() != null) {
            Color color = embed.getColor();
            int rgb = color.getRed();
            rgb = (rgb << 8) + color.getGreen();
            rgb = (rgb << 8) + color.getBlue();

            jsonEmbed.put("color", rgb);
        }
    }

    private void processDiscordEmbedFooter(Footer footer, JsonObject jsonEmbed) {
        if (footer != null) {
            JsonObject jsonFooter = new JsonObject();
            jsonFooter.put("text", footer.getText());
            jsonFooter.put("icon_url", footer.getIconUrl());
            jsonEmbed.put("footer", jsonFooter);
        }
    }

    private void processDiscordEmbedImage(Image image, JsonObject jsonEmbed) {
        if (image != null) {
            JsonObject jsonImage = new JsonObject();
            jsonImage.put("url", image.getUrl());
            jsonEmbed.put("image", jsonImage);
        }
    }

    private void processDiscordEmbedThumbnail(Thumbnail thumbnail, JsonObject jsonEmbed) {
        if (thumbnail != null) {
            JsonObject jsonThumbnail = new JsonObject();
            jsonThumbnail.put("url", thumbnail.getUrl());
            jsonEmbed.put("thumbnail", jsonThumbnail);
        }
    }

    private void processDiscordEmbedAuthor(Author author, JsonObject jsonEmbed) {
        if (author != null) {
            JsonObject jsonAuthor = new JsonObject();
            jsonAuthor.put("name", author.getName());
            jsonAuthor.put("url", author.getUrl());
            jsonAuthor.put("icon_url", author.getIconUrl());
            jsonEmbed.put("author", jsonAuthor);
        }
    }

    private void processDiscordEmbedMessageFields(List<Field> fields, JsonObject jsonEmbed) {
        List<JsonObject> jsonFields = new ArrayList<>();

        for (Field field : fields) {
            JsonObject jsonField = new JsonObject();

            jsonField.put("name", field.getName());
            jsonField.put("value", field.getValue());
            jsonField.put("inline", field.isInline());

            jsonFields.add(jsonField);
        }

        jsonEmbed.put("fields", jsonFields.toArray());
    }
}
