package com.mile.log.discord.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class EmbedObject {
    /**
     * <b>Discord Embed Message에 들어갈 Message 내용 List</b>
     */
    private final List<Field> fields = new ArrayList<>();

    /**
     * <b>Discord Embed Message 제목</b>
     */
    private String title;

    /**
     * <b>Discord Embed Message 상세 내용</b>
     */
    private String description;

    /**
     * <b>Discord Embed Message URL 형식</b>
     */
    private String url;

    /**
     * <b>Discord Embed Message 겉 색깔</b>
     */
    private Color color;

    /**
     * <b>Discord Embed Message 바닥글</b>
     */
    private Footer footer;

    /**
     * <b>Discord Embed Message 썸네일</b>
     */
    private Thumbnail thumbnail;

    /**
     * <b>Discord Embed Message 사진</b>
     */
    private Image image;

    /**
     * <b>Discord Embed Message 작성자</b>
     */
    private Author author;

    public String getTitle() {
        return title;
    }

    public EmbedObject setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EmbedObject setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public EmbedObject setUrl(String url) {
        this.url = url;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public EmbedObject setColor(Color color) {
        this.color = color;
        return this;
    }

    public Footer getFooter() {
        return footer;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public EmbedObject setThumbnail(String url) {
        this.thumbnail = new Thumbnail(url);
        return this;
    }

    public Image getImage() {
        return image;
    }

    public EmbedObject setImage(String url) {
        this.image = new Image(url);
        return this;
    }

    public Author getAuthor() {
        return author;
    }

    public List<Field> getFields() {
        return fields;
    }

    public EmbedObject setFooter(String text, String icon) {
        this.footer = new Footer(text, icon);
        return this;
    }

    public EmbedObject setAuthor(String name, String url, String icon) {
        this.author = new Author(name, url, icon);
        return this;
    }

    public EmbedObject addField(String name, String value, boolean inline) {
        this.fields.add(new Field(name, value, inline));
        return this;
    }
}
