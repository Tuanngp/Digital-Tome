package com.fpt.swp391.group6.DigitalTome.rest;

import com.fpt.swp391.group6.DigitalTome.dto.ContributionDto;
import com.fpt.swp391.group6.DigitalTome.rest.output.AbstractOutput;
import com.fpt.swp391.group6.DigitalTome.service.BookService;
import com.fpt.swp391.group6.DigitalTome.service.ContributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping()
    public AbstractOutput<ContributionDto> getPublishedBooks(@RequestParam int page, @RequestParam int limit) {
        AbstractOutput<ContributionDto> result = new AbstractOutput<ContributionDto>();
        result.setListResults(contributionService.
                getContributionByStatus(1, PageRequest.of(page-1,limit)));
        result.setTotalPages((int)Math.ceil((double)contributionService.countContributionByStatus(1)/(double)limit));
        result.setCurrentPage(page);
        return result;
    }

    @GetMapping("/rejection")
    public AbstractOutput getRejectedBooks(@RequestParam int page, @RequestParam int limit) {
        AbstractOutput result = new AbstractOutput();
        result.setListResults(contributionService.
                getContributionByStatus(3, PageRequest.of(page-1,limit)));
        result.setTotalPages((int)Math.ceil((double)contributionService.countContributionByStatus(3)/(double)limit));
        result.setCurrentPage(page);
        return result;
    }

    @GetMapping("/acception")
    public AbstractOutput getAcceptedBooks(@RequestParam int page, @RequestParam int limit) {
        AbstractOutput result = new AbstractOutput();
        result.setListResults(contributionService.
                getContributionByStatus(2, PageRequest.of(page-1,limit)));
        result.setTotalPages((int)Math.ceil((double)contributionService.countContributionByStatus(2)/(double)limit));
        result.setCurrentPage(page);
        return result;
    }



}
