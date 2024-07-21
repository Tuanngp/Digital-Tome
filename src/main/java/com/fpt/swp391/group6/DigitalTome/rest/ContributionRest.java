package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.dto.ContributionDto;
import com.fpt.swp391.group6.DigitalTome.entity.ContributionEntity;
import com.fpt.swp391.group6.DigitalTome.rest.output.AbstractOutput;
import com.fpt.swp391.group6.DigitalTome.service.BookService;
import com.fpt.swp391.group6.DigitalTome.service.ContributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contribution")
public class ContributionRest {

    private ContributionService contributionService;
    private BookService bookService;

    @Autowired
    public ContributionRest(ContributionService contributionService, BookService bookService) {
        this.contributionService = contributionService;
        this.bookService = bookService;
    }

    @GetMapping
    public AbstractOutput<ContributionDto> getPublishedBooks(@RequestParam int page, @RequestParam int limit) {
        return getBooksByStatus(1, page, limit);
    }

    @GetMapping("/rejection")
    public AbstractOutput<ContributionDto> getRejectedBooks(@RequestParam int page, @RequestParam int limit) {
        return getBooksByStatus(3, page, limit);
    }

    @GetMapping("/acception")
    public AbstractOutput<ContributionDto> getAcceptedBooks(@RequestParam int page, @RequestParam int limit) {
        return getBooksByStatus(2, page, limit);
    }

  /*  @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteContributionBookById(@PathVariable(name = "id") long id) {
        contributionService.deleteContributionBook(id);
        return ResponseEntity.noContent().build();
    }*/

    private AbstractOutput<ContributionDto> getBooksByStatus(int status, int page, int limit) {
        AbstractOutput<ContributionDto> result = new AbstractOutput<>();
        result.setListResults(contributionService.getContributionByStatus(status, PageRequest.of(page - 1, limit)));
        result.setTotalPages((int) Math.ceil((double) contributionService.countContributionByStatus(status) / limit));
        result.setCurrentPage(page);
        return result;
    }



}
