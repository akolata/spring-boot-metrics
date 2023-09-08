package pl.akolata.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BooksService {

    private final BooksRepository booksRepository;
    private final MeterRegistry meterRegistry;

    public BooksService(BooksRepository booksRepository, MeterRegistry meterRegistry) {
        this.booksRepository = booksRepository;
        this.meterRegistry = meterRegistry;

        Gauge.builder(MetricUtil.METRIC_BOOKS_IN_STORE_COUNT, booksRepository::countBooks)
            .description("A current number of books in store")
            .register(meterRegistry);
    }

    @SneakyThrows
    public List<Book> findByTitle(String title) {
        Tag titleTag = Tag.of(MetricUtil.TAG_TITLE, MetricUtil.getTagTitle(title));
        List<Book> books;
        Timer.Sample timer = Timer.start(meterRegistry);

        if (StringUtils.isEmpty(title)) {
            log.warn("BooksService#findByTitle sleeping for a short time - find all");
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 200));
            books = booksRepository.getBooks();
        } else {
            if ("Fundamental Algorithms".equalsIgnoreCase(title)) {
                log.warn("BooksService#findByTitle sleeping for a long time - Fundamental Algorithms search");
                Thread.sleep(ThreadLocalRandom.current().nextInt(1_000, 3_000));
            } else {
                log.warn("BooksService#findByTitle sleeping for an average time");
                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 1_000));
            }

            books = booksRepository.getBooks()
                .stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
        }

        timer.stop(Timer.builder(MetricUtil.METRIC_BOOKS_BY_TITLE_SEARCH)
            .description("Times showing how long does it take to search for books")
            .tags(List.of(titleTag))
            .register(meterRegistry));

        return books;
    }

}
