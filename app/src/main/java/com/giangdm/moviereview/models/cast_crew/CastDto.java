package com.giangdm.moviereview.models.cast_crew;

/**
 * Created by GiangDM on 23-04-19
 */
public class CastDto {
    private String name;
    private String path;

    public CastDto(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
