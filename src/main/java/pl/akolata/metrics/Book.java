package pl.akolata.metrics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private String id;
    private String title;

    public Book(Book book) {
        this.id = book.id;
        this.title = book.title;
    }
}
