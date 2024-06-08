package com.fpt.swp391.group6.DigitalTome.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fpt.swp391.group6.DigitalTome.entity.*;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;
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
    private String isbn;  // Mã ISBN, không được phép trống và phải duy nhất

    @Column(name = "title", nullable = false)
    private String title;  // Tiêu đề sách, không được phép trống

    @Column(name = "description", nullable = false)
    private String description;  // Mô tả sách, không được phép trống

    @Column(name = "edition")
    private String edition;  // Phiên bản của sách


    @Column(name = "publication_date")
    private Date publicationDate;
    // Ngày xuất bản

    @Column(name = "language")
    private String language;  // Ngôn ngữ của sách

    @Column(name = "views", columnDefinition = "BIGINT DEFAULT 0")
    private long views;  // Số lượt xem, mặc định là 0

    @Column(name = "point", columnDefinition = "BIGINT DEFAULT 0")
    private long point;  // Điểm số, mặc định là 0

    @Column(name = "book_cover")
    private String bookCover;  // Đường dẫn ảnh bìa sách

    @Column(name = "book_path", nullable = false)
    private String bookPath; // Đường dẫn tệp sách, không được phép trống

    @Column(name = "audio_path")
    private String audioPath; // Đường dẫn tệp âm thanh, nếu có

    @Column(name = "is_restricted")
    private boolean isRestricted;  // Đánh dấu sách có bị hạn chế hay không

    @Column(name = "status", columnDefinition = "INT DEFAULT 0")
    private int status;  // Trạng thái của sách, mặc định là 0

    @Column(name = "download_url")
    private String download_url;  // Đường dẫn để tải xuống sách

    @OneToMany(mappedBy = "bookEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ContributionEntity> contributionEntityList;  // Một sách có nhiều đóng góp

    @OneToMany(mappedBy = "bookEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CommentEntity> commentEntityList;   // Một sách có nhiều bình luận

    @OneToMany(mappedBy = "bookEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RateEntity> rateEntityList; // Một sách có nhiều đánh giá

    @OneToMany(mappedBy = "bookEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FavoriteEntity> favoriteEntityList;  // Một sách có nhiều lượt yêu thích

    @OneToMany(mappedBy = "bookEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PaymentEntity> paymentEntityList;  // Một sách có nhiều lượt thanh toán

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "book_categories",    // Bảng liên kết giữa sách và thể loại
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<CategoryEntity> categoryEntityList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "authors_books",               // Bảng liên kết giữa sách và tác giả
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<AccountEntity> authorEntityList;

    @Column(name = "is_new", columnDefinition = "INT DEFAULT 0")
    private int isNew;



}
