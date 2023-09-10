package pl.akolata.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/books")
public class BooksController {

    private final BooksService booksService;
    private final MeterRegistry meterRegistry;

    @GetMapping
    public ResponseEntity<List<Book>> getBooks(@RequestParam(required = false) String title) {
        log.info("Received request: GET /api/books?title=" + title);
        List<Book> books = booksService.findByTitle(title);

        // Chosen tags might not be the best - this is only for demonstration purposes, not according to the best monitoring practices
        Counter counter = Counter.builder(MetricUtil.METRIC_API_BOOKS_GET_COUNT)
            .tag(MetricUtil.TAG_TITLE, MetricUtil.getTagTitle(title))
            .tag(MetricUtil.TAG_MATCHING_BOOKS, String.valueOf(books.size()))
            .description("a number of requests to GET /api/books endpoint")
            .register(meterRegistry);
        counter.increment();

        return ResponseEntity.ok(books);
    }

}
