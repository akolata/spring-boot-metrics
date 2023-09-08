package pl.akolata.metrics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class BooksRepository {

    private static final List<Book> BOOKS = List.of(
        new Book("8eacc5d3-5f01-48e0-9aae-52cd2388d10d", "Fundamental Algorithms"),
        new Book("ceedf23f-6199-4cd7-b108-6d0e92d7c8a3", "Domain Driven Design"),
        new Book("0f75c02a-b640-4304-9bc7-6548bc875fef", "Analysis Patterns")
    );

    public List<Book> getBooks() {
        return BOOKS.stream()
            .map(Book::new)
            .collect(Collectors.toList());
    }

    public Long countBooks() {
        log.info("BooksRepository#countBooks counting books");
        return (long) BOOKS.size();
    }
}
