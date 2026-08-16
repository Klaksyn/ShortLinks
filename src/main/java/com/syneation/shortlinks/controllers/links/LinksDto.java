package com.syneation.shortlinks.controllers.links;

import jakarta.validation.constraints.*;

public class LinksDto {

    @NotNull
    private Long creator;

    @NotEmpty(message = "The link is required to be filled in")
    private String original_link;

    @NotNull(message = "The link length is required to be filled in")
    private Short lenUrl;

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long id) {
        creator = id;
    }

    public String getOriginal_link() {
        return original_link;
    }

    public void setOriginal_link(String original_link) {
        this.original_link = original_link;
    }

    public Short getLenUrl() {
        return lenUrl;
    }

    public void setLenUrl(Short len_url) {
        this.lenUrl = len_url;
    }

}
