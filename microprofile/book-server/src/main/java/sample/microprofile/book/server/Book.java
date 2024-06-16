package sample.microprofile.book.server;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter @ToString
@EqualsAndHashCode // Entityにつけるのは本来はよくない
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Update.class)
    private Integer id;

    @Column(unique = true)
    @NotBlank
    @Size(min = 1, max = 20)
    private String title;

    @Size(max = 20)
    private String author;

    public Book clone() {
        return new Book(this.id, this.title, this.author);
    }

    public boolean hasSameTitleAs(Book other) {
        if (other == null) {
            return false;
        }
        if (this == other || this.getId().equals(other.getId())) {
            return false;
        }
        return this.title.equals(other.getTitle());
    }


    public interface Update {
    }
}
