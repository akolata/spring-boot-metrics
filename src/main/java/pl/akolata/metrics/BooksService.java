package pl.akolata.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class BooksService {

    private final BooksRepository booksRepository;
    private final MeterRegistry meterRegistry;

    public BooksService(BooksRepository booksRepository, MeterRegistry meterRegistry) {
        this.booksRepository = booksRepository;
        this.meterRegistry = meterRegistry;

        Gauge.builder("books_count", booksRepository::countBooks)
            .description("A current number of books in the system")
            .register(meterRegistry);
    }

    @SneakyThrows
    public List<Book> findByTitle(String title) {
        Tag titleTag = Tag.of("title", StringUtils.isEmpty(title) ? "all" : title);
        List<Book> books;
        Timer.Sample timer = Timer.start(meterRegistry);

        if (StringUtils.isEmpty(title)) {
            books = booksRepository.getBooks();
        } else {
            if ("Fundamental Algorithms".equalsIgnoreCase(title)) {
                Thread.sleep(ThreadLocalRandom.current().nextInt(200, 400));
            }

            books = booksRepository.getBooks()
                .stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
        }

        timer.stop(Timer.builder("service_books_find")
            .description("books searching timer")
            .tags(List.of(titleTag))
            .register(meterRegistry));

        return books;
    }

}
