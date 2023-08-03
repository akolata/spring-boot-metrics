package pl.akolata.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/books")
public class BooksController {

    private final BooksService booksService;
    private final MeterRegistry meterRegistry;

    @GetMapping
    public ResponseEntity<List<Book>> getBooks(@RequestParam(required = false) String title) {
        Counter counter = Counter.builder("api_books_get")
            .tag("title", StringUtils.isEmpty(title) ? "all" : title)
            .description("a number of requests to /api/books endpoint")
            .register(meterRegistry);
        counter.increment();

        return ResponseEntity.ok(booksService.findByTitle(title));
    }

}
