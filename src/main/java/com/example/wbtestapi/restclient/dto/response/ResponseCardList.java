package com.example.wbtestapi.restclient.dto.response;

import com.example.wbtestapi.client.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCardList {
    private ResponseData data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseData {
        private List<ResponseCard> cards;
        private Cursor cursor;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseCard {
        private List<Size> sizes;
        private List<String> mediaFiles;
        private List<String> colors;
        private LocalDateTime updateAt;
        private String vendorCode;
        private String brand;
        private String object;
        private Long nmID;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Cursor {
        private String updatedAt;
        private Long nmID;
        private Integer total;
    }
}
