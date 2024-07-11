package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.dto.BookDetailDto;
import com.fpt.swp391.group6.DigitalTome.dto.BookDto;
import com.fpt.swp391.group6.DigitalTome.entity.AuthorEntity;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.entity.CategoryEntity;
import com.fpt.swp391.group6.DigitalTome.entity.PaymentEntity;
import com.fpt.swp391.group6.DigitalTome.mapper.BookDetailMapper;
import com.fpt.swp391.group6.DigitalTome.mapper.BookMapper;
import com.fpt.swp391.group6.DigitalTome.repository.BookRepository;
import com.fpt.swp391.group6.DigitalTome.repository.IBookDetailRepository;
import com.fpt.swp391.group6.DigitalTome.repository.PaymentRepository;
import com.fpt.swp391.group6.DigitalTome.rest.input.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookDetailMapper bookDetailMapper;
    private final IBookDetailRepository IBookDetailRepositoryImpl;
    private final PaymentRepository paymentRepository;

    @Autowired
    public BookService(BookRepository bookRepository, BookMapper bookMapper, PaymentRepository paymentRepository,BookDetailMapper bookDetailMapper, IBookDetailRepository IBookDetailRepositoryImpl) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.bookDetailMapper = bookDetailMapper;
        this.IBookDetailRepositoryImpl = IBookDetailRepositoryImpl;
        this.paymentRepository = paymentRepository;
    }

    public void saveBook(BookDto bookDto){
        boolean existsIsbn = bookRepository.existsBookEntityByIsbn(bookDto.getIsbn());
        if(existsIsbn){
            throw new RuntimeException("The book already exists");
        }
        BookEntity bookEntity = bookMapper.toBook(bookDto);
        bookRepository.save(bookEntity);
    }
    public BookEntity getBookByIsbn(String isbn){
        return bookRepository.findByIsbn(isbn);
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

    public List<BookEntity> getBooks() {
        return bookRepository.findAll();
    }

    public List<BookEntity> getBooks(int pageSize) {
        Pageable pageable = PageRequest.of(0, pageSize);
        Page<BookEntity> bookPage = bookRepository.findAll(pageable);
        return bookPage.getContent();
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
        BookEntity bookEntity = this.IBookDetailRepositoryImpl.findByIsbnIncludeCategoriesAndAuthors(isbn);
        return mapToDto(bookEntity) ;
    }

    public void updateStatusByIsbn(int status,String isbn){
        BookEntity bookEntity = bookRepository.findByIsbn(isbn);
        if(bookEntity != null){
            List<String> args = new ArrayList<>();
            args.add("Book_Collection_Digital_Tome");
            args.add(bookEntity.getIsbn());
            args.add(bookEntity.getTitle());
            args.add(bookEntity.getDescription());
            if(insertToQdrant(args)) this.bookRepository.updateStatusByIsbn(status, isbn);
        }
    }

    public List<BookDetailDto>  findByStatus(int status, Pageable pageable){
        List<BookEntity> list = this.IBookDetailRepositoryImpl.findByStatus(status, pageable);
        return list.stream().
                map(this::mapToDto).
                collect(Collectors.toList());
    }

    public int countBookByStatus(int status){
        return this.bookRepository.countByStatus(2);
    }

    public Page<BookDetailDto> search(SearchRequest searchRequest, Pageable pageable){
        Page<BookEntity> list = this.IBookDetailRepositoryImpl.search(searchRequest,pageable);
        return  new PageImpl<>(list.getContent().stream().map(this::mapToDto).collect(Collectors.toList()), list.getPageable(), list.getTotalElements());
    }

    private BookDetailDto mapToDto(BookEntity bookEntity){
        BookDetailDto bookDetailDto = bookDetailMapper.toDto(bookEntity);
        bookDetailDto.setAuthors(bookEntity.getAuthorEntityList().stream().map(AuthorEntity::getName).collect(Collectors.toList()));
        bookDetailDto.setCategories(bookEntity.getCategoryEntityList().stream().map(CategoryEntity::getName).collect(Collectors.toList()));
        return bookDetailDto;
    }

    public List<BookEntity> getRecommendation() {
        return bookRepository.findTop10ByOrderByViews();
    }

    public Page<BookEntity> findPaginatedByAccountId(Long accountId, int page, int pageSize, String sortField, String sortDir) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
        return bookRepository.findByAccountId(accountId, pageable);
    }

    public List<PaymentEntity> getPaymentsByBookIdsAndDateRange(List<Long> bookIds, Date startDate, Date endDate) {
        return paymentRepository.findPaymentsByBookIdsAndDateRange(bookIds, startDate, endDate);
    }

    public BigDecimal calculateTotalRevenue(List<PaymentEntity> payments) {
        return payments.stream()
                .map(PaymentEntity::getDecimal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int calculateTotalBooksSold(List<PaymentEntity> payments) {
        return payments.size();
    }

    public Map<String, Integer> groupPaymentsByPeriod(List<PaymentEntity> payments, String period, boolean isBookCount) {
        Map<String, Integer> groupedBooksSoldData = new LinkedHashMap<>();
        Map<String, BigDecimal> groupedRevenueData = new LinkedHashMap<>();
        SimpleDateFormat sdf;
        Calendar calendar = Calendar.getInstance();

        switch (period) {
            case "7days":
            case "1month":
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                break;
            case "1year":
                sdf = new SimpleDateFormat("yyyy-MM");
                break;
            case "all":
                sdf = new SimpleDateFormat("yyyy");
                break;
            default:
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                break;
        }

        for (PaymentEntity payment : payments) {
            String key = sdf.format(payment.getCreatedDate());
            if (isBookCount) {
                groupedBooksSoldData.put(key, groupedBooksSoldData.getOrDefault(key, 0) + 1);
            } else {
                groupedRevenueData.put(key, groupedRevenueData.getOrDefault(key, BigDecimal.ZERO).add(payment.getDecimal()));
            }
        }

        if (isBookCount) {
            return groupedBooksSoldData;
        } else {
            // Convert BigDecimal values to Integer for revenue details
            Map<String, Integer> revenueDetails = new LinkedHashMap<>();
            for (Map.Entry<String, BigDecimal> entry : groupedRevenueData.entrySet()) {
                revenueDetails.put(entry.getKey(), entry.getValue().intValue());
            }
            return revenueDetails;
        }
    }

    public Map<String, BigDecimal> groupRevenueByPeriod(List<PaymentEntity> payments, String period) {
        Map<String, BigDecimal> groupedData = new LinkedHashMap<>();
        SimpleDateFormat sdf;
        switch (period) {
            case "7days":
            case "1month":
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                break;
            case "1year":
                sdf = new SimpleDateFormat("yyyy-MM");
                break;
            case "all":
                sdf = new SimpleDateFormat("yyyy");
                break;
            default:
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                break;
        }

        for (PaymentEntity payment : payments) {
            String key = sdf.format(payment.getCreatedDate());
            groupedData.put(key, groupedData.getOrDefault(key, BigDecimal.ZERO).add(payment.getDecimal()));
        }

        return groupedData;
    }

    private boolean insertToQdrant(List<String> args){
        try {
            List<String> command = new ArrayList<>();
            command.add("python");
            command.add("src/main/python/InsertDataToQdrant.py");
            command.addAll(args);

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);

            Process p = pb.start();


            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line = br.readLine();
                return Boolean.parseBoolean(line);
            }catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                return false;
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    private static List<String> findSimilarBooksByDes(List<String> args) {
        try {
            List<String> command = new ArrayList<>();
            command.add("python");
            command.add("src/main/python/FindSimilarText.py");
            command.addAll(args);

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);

            Process p = pb.start();

            List<String> isbns = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    if(line.equals("false")){
                        return null;
                    }
                    isbns.add(line);
                }
            }catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            return isbns;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }


}

