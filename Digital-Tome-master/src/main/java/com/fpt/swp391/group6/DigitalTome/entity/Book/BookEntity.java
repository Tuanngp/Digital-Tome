package com.fpt.swp391.group6.DigitalTome.entity.Book;

import com.fpt.swp391.group6.DigitalTome.entity.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "book")
public class BookEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "edition")
    private String edition;

    @Column(name = "publication_date")
    @Temporal(TemporalType.DATE)
    private Date publicationDate;

    @Column(name = "language")
    private String language;

    @Column(name = "views", columnDefinition = "BIGINT DEFAULT 0")
    private long views;

    @Column(name = "point", columnDefinition = "BIGINT DEFAULT 0")
    private long point;

    @Column(name = "book_cover")
    private String bookCover;

    @Column(name = "book_path", nullable = false)
    private String bookPath;

    @Column(name = "audio_path")
    private String audioPath;

    @Column(name = "is_restricted")
    private boolean isRestricted;

    @Column(name = "status", columnDefinition = "INT DEFAULT 0")
    private int status;

    @Column(name = "download_url")
    private String download_url;

    @OneToMany(mappedBy = "bookEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ContributionEntity> contributionEntityList;

    @OneToMany(mappedBy = "bookEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CommentEntity> commentEntityList;

    @OneToMany(mappedBy = "bookEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RateEntity> rateEntityList;

    @OneToMany(mappedBy = "bookEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FavoriteEntity> favoriteEntityList;

    @OneToMany(mappedBy = "bookEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PaymentEntity> paymentEntityList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "book_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<CategoryEntity> categoryEntityList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "authors_books",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<AccountEntity> authorEntityList;
}
