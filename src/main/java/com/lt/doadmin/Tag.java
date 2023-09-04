package com.lt.doadmin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
@Data
public class Tag {
   @JsonProperty("id")
   private int id;
   @JsonProperty("tagName")
   private String tagName;
   @JsonProperty("introduce")
   private String introduce;
   /*
   下面的属性，暂时不需要，就不显示了
    */
//   @JsonProperty("published_at")
//   private Date published_at;
//   @JsonProperty("created_at")
//   private Date created_at;
//   @JsonProperty("updated_at")
//   private Date updated_at;
}
