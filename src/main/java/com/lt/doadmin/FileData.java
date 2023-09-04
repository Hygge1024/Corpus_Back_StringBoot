package com.lt.doadmin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FileData {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    //    @JsonProperty("alternativeText")
    private String alternativeText;
    //    @JsonProperty("caption")
    private String caption;
    //    @JsonProperty("width")
    private String width;
    //    @JsonProperty("height")
    private String height;
    //    @JsonProperty("formats")
    private String formats;
    //    @JsonProperty("hash")
    private String hash;
    //    @JsonProperty("ext")
    private String ext;
    //    @JsonProperty("mime")
    private String mime;
    @JsonProperty("size")
    private double size;
    //    @JsonProperty("previewUrl")
    private String previewUrl;
    //    @JsonProperty("provider")
    private String provider;
    //    @JsonProperty("provider_metadata")
    private String provider_metadata;
    @JsonProperty("created_at")
    private Date created_at;
    @JsonProperty("updated_at")
    private Date updated_at;
    private String url;
}
