package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.dto.BookDetailDto;
import com.fpt.swp391.group6.DigitalTome.dto.BookDto;
import com.fpt.swp391.group6.DigitalTome.entity.AuthorEntity;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.entity.CategoryEntity;
import com.fpt.swp391.group6.DigitalTome.mapper.BookDetailMapper;
import com.fpt.swp391.group6.DigitalTome.mapper.BookMapper;
import com.fpt.swp391.group6.DigitalTome.repository.BookRepository;
import com.fpt.swp391.group6.DigitalTome.repository.IBookDetailRepository;
import com.fpt.swp391.group6.DigitalTome.rest.input.SearchPageableRequest;
import com.fpt.swp391.group6.DigitalTome.rest.input.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookDetailMapper bookDetailMapper;
    private final IBookDetailRepository bookDetailRepositoryImpl;

    @Autowired
    public BookService(BookRepository bookRepository, BookMapper bookMapper, BookDetailMapper bookDetailMapper, IBookDetailRepository bookDetailRepositoryImpl) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.bookDetailMapper = bookDetailMapper;
        this.bookDetailRepositoryImpl = bookDetailRepositoryImpl;
    }

    public void saveBook(BookDto bookDto){
        boolean existsIsbn = bookRepository.existsBookEntityByIsbn(bookDto.getIsbn());
        if(existsIsbn){
            throw new RuntimeException("The book already exists");
        }
        BookEntity bookEntity = bookMapper.toBook(bookDto);
        bookRepository.save(bookEntity);
    }

    public BookEntity getBookById(long id) {
        Optional<BookEntity> optionalBook = bookRepository.findById(id);
        BookEntity book;
        if (optionalBook.isPresent()) {
            book = optionalBook.get();
        } else {
            throw new RuntimeException("Book not found for id : " + id);
        }
        return book;
    }

    public BookEntity getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public List<BookEntity> getBooks() {
        return bookRepository.findAll();
    }

    public void saveBook(BookEntity book){
        this.bookRepository.save(book);
    }


    public void deleteBookById(long id){
        this.bookRepository.deleteById(id);
    }

    public Page<BookEntity> findPaginated(int pageNum, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return this.bookRepository.findAll(pageable);
    }
    public boolean isISBNAlreadyExists(String isbn) {

        return bookRepository.existsByIsbn(isbn);
    }

    public BookEntity findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public BookDetailDto findByIsbnIncludeCategoriesAndAuthors(String isbn){
        BookEntity bookEntity = this.bookDetailRepositoryImpl.findByIsbnIncludeCategoriesAndAuthors(isbn);
        return mapToDto(bookEntity) ;
    }

    public void updateStatusByIsbn(int status,String isbn){
        this.bookRepository.updateStatusByIsbn(status, isbn);
    }


    public List<BookDetailDto>  findByStatus(int status, Pageable pageable){
        List<BookEntity> list = this.bookDetailRepositoryImpl.findByStatus(status, pageable);
        return list.stream().
                map(this::mapToDto).
                collect(Collectors.toList());
    }

    public int countBookByStatus(int status){
        return this.bookRepository.countByStatus(2);
    }

    public Page<BookDetailDto> search(SearchRequest searchRequest, Pageable pageable){
        Page<BookEntity> list = this.bookDetailRepositoryImpl.search(searchRequest,pageable);
        return  new PageImpl<>(list.getContent().stream().map(this::mapToDto).collect(Collectors.toList()), list.getPageable(), list.getTotalElements());
    }


    private BookDetailDto mapToDto(BookEntity bookEntity){
        BookDetailDto bookDetailDto = bookDetailMapper.toDto(bookEntity);
        bookDetailDto.setAuthors(bookEntity.getAuthorEntityList().stream().map(AuthorEntity::getName).collect(Collectors.toList()));
        bookDetailDto.setCategories(bookEntity.getCategoryEntityList().stream().map(CategoryEntity::getName).collect(Collectors.toList()));
        return bookDetailDto;
    }
}

