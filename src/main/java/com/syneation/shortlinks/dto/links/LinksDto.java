package com.syneation.shortlinks.dto.links;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class LinksDto {

    @NotNull
    private Long creator;

    @NotEmpty
    private String original_link;

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

}
