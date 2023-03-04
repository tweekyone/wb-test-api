package com.example.wbtestapi.restclient.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class RequestCardList {
    private Sort sort;

    @Data
    public static class Sort {
        public Sort(Cursor cursor, Filter filter) {
            this.cursor = cursor;
            this.filter = filter;
        }

        private Cursor cursor;
        private Filter filter;
        private Map<String, Object> sort = Map.of("sortColumn", "updateAt", "ascending", false);
    }

    @Data
    @AllArgsConstructor
    public static class Cursor {
        //    "cursor": {
        //      "updatedAt": "2022-09-23T17:41:32Z",
        //      "nmID": 66965444,
        //      "limit": 1000
        //    },

        public Cursor(Integer limit) {
            this.limit = limit;
        }

        private String updatedAt;
        private Long nmID;
        private Integer limit;
    }

    @Data
    public static class Filter {
        //    "filter": {
        //      "textSearch": "test",
        //      "withPhoto": -1
        //    },
        private String textSearch;
        //-1 - Показать все КТ.
        //0 - Показать КТ без фото.
        //1 - Показать КТ с фото.
        private int withPhoto;

        public Filter(int withPhoto) {
            this.withPhoto = withPhoto;
        }

        public Filter(String textSearch, int withPhoto) {
            this.textSearch = textSearch;
            this.withPhoto = withPhoto;
        }
    }

}

