package com.example.wbtestapi.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.asynchttpclient.*;
import org.asynchttpclient.request.body.multipart.FilePart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.wbtestapi.client.dto.RequestCardList;
import com.example.wbtestapi.client.dto.RequestUpdateMediaFiles;
import com.example.wbtestapi.client.dto.ResponseCardList;
import com.example.wbtestapi.client.exception.WildberriesException;
import com.example.wbtestapi.client.exception.WildberriesResponseException;
import com.example.wbtestapi.client.exception.WildberriesResponseRateLimitException;
import com.example.wbtestapi.client.model.*;
import com.example.wbtestapi.client.model.config.ObjectConfig;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

//TODO переписать на обычный RestTemplate
@Getter
public class WildberriesClient implements Closeable {
    private static final String JSON = "application/json; charset=UTF-8";
    private static final DefaultAsyncHttpClientConfig DEFAULT_ASYNC_HTTP_CLIENT_CONFIG =
    new DefaultAsyncHttpClientConfig.Builder().setFollowRedirect(true).build();
    private final String url;
    private final String token;
//    private final String supplierId;
    private final AsyncHttpClient client;
    private final Logger logger;
    private final Map<String, String> headers;
    private final ObjectMapper mapper;
    private boolean closed = false;
    private final boolean closeClient;


    public WildberriesClient(String url, String token) {
        this.token = token;
//        this.supplierId = supplierId;
        this.url = url;
        this.client = new DefaultAsyncHttpClient(DEFAULT_ASYNC_HTTP_CLIENT_CONFIG);
        this.logger = LoggerFactory.getLogger(WildberriesClient.class);
        this.headers = Collections.unmodifiableMap(new HashMap<>());
        this.mapper = createMapper();
        this.closeClient = client == null;
    }

    public static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); //2022-01-25T23:42:39Z
        mapper.enable(DeserializationFeature.USE_LONG_FOR_INTS);
        return mapper;
    }

    ///////////////////////////////////////////////////////////////
    // COMMANDS
    ///////////////////////////////////////////////////////////////

    public ResponseCardList getCardList(RequestCardList requestCardList) {

        return complete(submit(req("POST",cnst("/content/v1/cards/cursor/list"),JSON, json(requestCardList)),
                handle(ResponseCardList.class,"data")));
    }

    public String createCards(Card card) {
        return complete(createCardsAsync(List.of(List.of(card))));
    }

    public List<Card> getDetailedCards(Map<String, List<String>> requestDetailedCard) {
        return complete(submit(req("POST",cnst("/content/v1/cards/filter"),JSON, json(requestDetailedCard)),
                handleList(Card.class,"data")));
    }

    public ListenableFuture<String> createCardsAsync(List<List<Card>> cards) {

        return submit(req("POST", cnst("/content/v1/cards/upload"),
                        JSON, json(cards)),
                handleJobStatus());
    }

    public String uploadCardMediaFiles(RequestUpdateMediaFiles requestUpdateMediaFiles) {
        return complete(uploadCardMediaFilesAsync(requestUpdateMediaFiles));
    }

    public ListenableFuture<String> uploadCardMediaFilesAsync(RequestUpdateMediaFiles requestUpdateMediaFiles) {

        return submit(req("POST", cnst("/content/v1/media/save"),
                        JSON, json(requestUpdateMediaFiles)),
                handleJobStatus());
    }

    public String updateCard(List<Card> cards) {
        return complete(updateCardAsync(cards));
    }

    public ListenableFuture<String> updateCardAsync(List<Card> cards) {
        return submit(req("POST", cnst("/content/v1/cards/update"),
                        JSON, json(cards)),
                handleJobStatus());
    }

    public String updateStocks(List<Stock> stocks) {
        return complete(updateStocksAsync(stocks));
    }

    public ListenableFuture<String> updateStocksAsync(List<Stock> stocks) {
        return submit(req("POST", cnst("/api/v2/stocks"),
                        JSON, json(stocks)),
                handleJobStatus());
    }

    public String updatePrice(List<Price> price) {
        return complete(updatePriceAsync(price));
    }

    public ListenableFuture<String> updatePriceAsync(List<Price> price) {
        return submit(req("POST", cnst("/public/api/v1/prices"),
                        JSON, json(price)),
                handleJobStatus());
    }

    public String updateDiscount(List<Discount> discount) {
        return complete(updateDiscountAsync(discount));
    }

    public String revokeDiscount(List<Long> discount) {
        return complete(revokeDiscountAsync(discount));
    }

    public ListenableFuture<String> updateDiscountAsync(List<Discount> discount) {
        return submit(req("POST", cnst("/public/api/v1/updateDiscounts"),
                        JSON, json(discount)),
                handleJobStatus());
    }

    public ListenableFuture<String> revokeDiscountAsync(List<Long> discount) {
        return submit(req("POST", cnst("/public/api/v1/revokeDiscounts"),
                        JSON, json(discount)),
                handleJobStatus());
    }

    public List<NomenclatureInfo> getNomenclatureInfo() {
        return complete(submit(req("GET", "https://suppliers-api.wildberries.ru/public/api/v1/info?quantity=1"),
                handleList(NomenclatureInfo.class, "")));
    }

    // GET https://suppliers-api.wildberries.ru/api/v1/config/get/object/translated?name=Кроссовки
    /**
     * All characteristics for a certain category of products
     * @param categoryName
     * @return CategoryConfig
     */
    public ObjectConfig getObjectConfig(String categoryName) {
        return complete(submit(req("GET", tmpl("/api/v1/config/get/object/translated?name={categoryName}")
                        .set("categoryName", categoryName)),
                handle(ObjectConfig.class,"data")));
    }

    public void uploadFile(UUID uuid, File file) {
        RequestBuilder builder = reqBuilder("POST", cnst("/card/upload/file/multipart").toString());
        builder.setHeader("Content-Type", "multipart/form-data");
        builder.setHeader("X-File-Id", uuid.toString());
//        builder.addFormParam("data", file.getAbsolutePath());
        builder.addBodyPart(new FilePart("uploadfile",
                file,
                "multipart/form-data",
                StandardCharsets.UTF_8,
                file.getName()
                ));
        final Request req = builder.build();

        complete(submit(req, handleStatus()));
    }


    ///////////////////////////////////////////////////////////////
    // SERVICE METHODS
    ///////////////////////////////////////////////////////////////

    private static <T> T complete(ListenableFuture<T> future) {
        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new WildberriesException(e.getMessage(), e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof WildberriesException) {
                if (e.getCause() instanceof WildberriesResponseRateLimitException) {
                    throw new WildberriesResponseRateLimitException((WildberriesResponseRateLimitException) e.getCause());
                }
                if (e.getCause() instanceof WildberriesResponseException) {
                    throw new WildberriesResponseException((WildberriesResponseException)e.getCause());
                }
                throw new WildberriesException(e.getCause());
            }
            throw new WildberriesException(e.getMessage(), e);
        }
    }

    private <T> ListenableFuture<T> submit(Request request, WildberriesAsyncCompletionHandler<T> handler) {
        if (logger.isDebugEnabled()) {
            if (request.getStringData() != null) {
                logger.debug("Request {} {}\n{}", request.getMethod(), request.getUrl(), request.getStringData());
            } else if (request.getByteData() != null) {
                logger.debug("Request {} {} {} {} bytes", request.getMethod(), request.getUrl(),
                        request.getHeaders().get("Content-type"), request.getByteData().length);
            } else {
                logger.debug("Request {} {}", request.getMethod(), request.getUrl());
            }
        }
        return client.executeRequest(request, handler);
    }

    private Request req(String method, Uri template) {
        return req(method, template.toString());
    }

    private Request req(String method, String url) {
        return reqBuilder(method, url).build();
    }

    private Request req(String method, Uri template, String contentType, byte[] body) {
        RequestBuilder builder = reqBuilder(method, template.toString());
        builder.addHeader("Content-type", contentType);
        builder.setBody(body);
        return builder.build();
    }

    private RequestBuilder reqBuilder(String method, String url) {
        RequestBuilder builder = new RequestBuilder(method);
            builder.addHeader("Authorization", "Bearer " + this.token);
        headers.forEach(builder::setHeader);
        return builder.setUrl(url);
    }

    private TemplateUri tmpl(String template) {
        return new TemplateUri(url + template);
    }

    private Uri cnst(String template) {
        return new FixedUri(url + template);
    }

    private void logResponse(Response response) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("Response HTTP/{} {}\n{}", response.getStatusCode(), response.getStatusText(),
                    response.getResponseBody());
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Response headers {}", response.getHeaders());
        }
    }

    private boolean isStatus2xx(Response response) {
        return response.getStatusCode() / 100 == 2;
    }

    private boolean withoutError(Response response) throws Exception{
        boolean hasError = false;
        var errorNode = mapper.readTree(response.getResponseBodyAsStream()).get("error");
        if (Objects.nonNull(errorNode)) {
            hasError = errorNode.booleanValue();
        }
        var errorTextNode = mapper.readTree(response.getResponseBodyAsStream()).get("errorText");
        if (Objects.nonNull(errorTextNode)) {
            hasError = !errorTextNode.isEmpty();
        }
        return !hasError;
    }

    private boolean isRateLimitResponse(Response response) {
        return response.getStatusCode() == 429;
    }

    private byte[] json(Object object) {
        try {
            return mapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new WildberriesException(e.getMessage(), e);
        }
    }

    public boolean isClosed() {
        return closed || client.isClosed();
    }

    @Override
    public void close() {
        if (closeClient && !client.isClosed()) {
            try {
                client.close();
            } catch (IOException e) {
                logger.warn("Unexpected error on client close", e);
            }
        }
        closed = true;
    }

    ///////////////////////////////////////////////////////////////
    // HANDLERS
    ///////////////////////////////////////////////////////////////

    protected WildberriesAsyncCompletionHandler<Void> handleStatus() {
        return new WildberriesAsyncCompletionHandler<Void>() {
            @Override
            public Void onCompleted(Response response) throws Exception {
                logResponse(response);
                if (isStatus2xx(response) && withoutError(response)) {
                    return null;
                } else if (isRateLimitResponse(response)) {
                    throw new WildberriesResponseRateLimitException(response.toString());
                }
                throw new WildberriesResponseException(response.toString());
            }
        };
    }

    @SuppressWarnings("unchecked")
    protected <T> WildberriesAsyncCompletionHandler<T> handle(final Class<T> clazz) {
        return new WildberriesAsyncCompletionHandler<T>() {
            @Override
            public T onCompleted(Response response) throws Exception {
                logResponse(response);
                if (isStatus2xx(response) && withoutError(response)) {
                    return (T) mapper.readerFor(clazz).readValue(response.getResponseBodyAsStream());
                } else if (isRateLimitResponse(response)) {
                    throw new WildberriesResponseRateLimitException(response.toString());
                }
                if (response.getStatusCode() == 404) {
                    throw new WildberriesResponseException(response.toString());
                }
                return null;
            }
        };
    }

    protected WildberriesAsyncCompletionHandler<String> handleJobStatus() {
        return new BasicAsyncCompletionHandler<String>(String.class, "data") {
            @Override
            public String onCompleted(Response response) throws Exception {
                String result = super.onCompleted(response);
                if (result == null) {
                    // null is when we receive a 404 response.
                    // For an async job we trigger an error
//                    throw new WildberriesResponseException(response);
                }
                return result;
            }
        };
    }

    private class BasicAsyncCompletionHandler<T> extends WildberriesAsyncCompletionHandler<T> {
        private final Class<T> clazz;
        private final String name;
        private final Class[] typeParams;

        public BasicAsyncCompletionHandler(Class clazz, String name, Class... typeParams) {
            this.clazz = clazz;
            this.name = name;
            this.typeParams = typeParams;
        }

        @Override
        public T onCompleted(Response response) throws Exception {
            logResponse(response);
                if (isStatus2xx(response) && withoutError(response)) {
                if (typeParams.length > 0) {
                    JavaType type = mapper.getTypeFactory().constructParametricType(clazz, typeParams);
                    return mapper.convertValue(mapper.readTree(response.getResponseBodyAsStream()).get(name), type);
                }
                try {
                    return mapper.convertValue(mapper.readTree(response.getResponseBodyAsStream()).get(name), clazz);
                } catch (Exception e) {
                    // Предполагается что мы ожидаем просто текст в качестве возвращаемого значения
                    return (T) response.getResponseBody();
                }
            } else if (isRateLimitResponse(response)) {
                throw new WildberriesResponseRateLimitException(response.toString());
            }
            if (response.getStatusCode() == 404) {
                return null;
            }
            throw new WildberriesResponseException(response.toString());
        }
    }

    protected <T> WildberriesAsyncCompletionHandler<T> handle(final Class<T> clazz, final String name, final Class... typeParams) {
        return new BasicAsyncCompletionHandler<>(clazz, name, typeParams);
    }

    protected <T> PagedAsyncCompletionHandler<List<T>> handleList(final Class<T> clazz, final String name) {
        return new PagedAsyncListCompletionHandler<>(clazz, name);
    }


    ///////////////////////////////////////////////////////////////
    // STATIC CLASSES
    ///////////////////////////////////////////////////////////////

    private abstract class PagedAsyncCompletionHandler<T> extends WildberriesAsyncCompletionHandler<T> {
        private String nextPage;
        private static final String NEXT_PAGE = "next_page";

        public void setPagedProperties(JsonNode responseNode, Class<?> clazz) {
            JsonNode node = responseNode.get(NEXT_PAGE);
            if (node == null) {
                this.nextPage = null;
                if (logger.isDebugEnabled()) {
                    logger.debug(NEXT_PAGE + " property not found, pagination not supported" +
                            (clazz != null ? " for " + clazz.getName() : ""));
                }
            } else {
                this.nextPage = node.asText();
            }
        }

        public String getNextPage() {
            return nextPage;
        }

        public void setNextPage(String nextPage) {
            this.nextPage = nextPage;
        }
    }

    private static abstract class WildberriesAsyncCompletionHandler<T> extends AsyncCompletionHandler<T> {
        @Override
        public void onThrowable(Throwable t) {
            if (t instanceof IOException) {
                throw new WildberriesException(t);
            } else {
                super.onThrowable(t);
            }
        }
    }

    private class PagedAsyncListCompletionHandler<T> extends PagedAsyncCompletionHandler<List<T>> {
        private final Class<T> clazz;
        private final String name;
        public PagedAsyncListCompletionHandler(Class<T> clazz, String name) {
            this.clazz = clazz;
            this.name = name;
        }

        @Override
        public List<T> onCompleted(Response response) throws Exception {
            logResponse(response);
            if (isStatus2xx(response) && withoutError(response)) {
                List<T> values = new ArrayList<>();
                if (mapper.readTree(response.getResponseBodyAsBytes()).has(name)) {
                    JsonNode responseNode = mapper.readTree(response.getResponseBodyAsBytes()).get(name);
                    setPagedProperties(responseNode, clazz);

                    for (JsonNode node : responseNode) {
                        values.add(mapper.convertValue(node, clazz));
                    }
                } else {
                    JsonNode responseNode = mapper.readTree(response.getResponseBodyAsBytes());

                    for (JsonNode node : responseNode) {
                        values.add(mapper.convertValue(node, clazz));
                    }
                }

                return values;
            } else if (isRateLimitResponse(response)) {
                throw new WildberriesResponseRateLimitException(response.toString());
            }
            throw new WildberriesResponseRateLimitException(response.toString());
        }
    }



}
