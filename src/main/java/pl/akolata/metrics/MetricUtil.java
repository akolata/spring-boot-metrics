package pl.akolata.metrics;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

@UtilityClass
public class MetricUtil {
    String BOOKS_SERVICE_PREFIX = "books_service";
    String BOOKS = "_books";
    String API_BOOKS = BOOKS_SERVICE_PREFIX + "_api" + BOOKS;
    String METRIC_API_BOOKS_GET_COUNT = API_BOOKS + "_get_count";
    String METRIC_BOOKS_IN_STORE_COUNT = BOOKS_SERVICE_PREFIX + BOOKS + "_in_store_count";
    String METRIC_BOOKS_BY_TITLE_SEARCH = BOOKS_SERVICE_PREFIX + BOOKS + "_search_by_title";
    String TAG_TITLE = "title";
    String TAG_MATCHING_BOOKS = "matching_books";

    static String getTagTitle(String title) {
        return StringUtils.isEmpty(title) ? "all" : title;
    }
}
