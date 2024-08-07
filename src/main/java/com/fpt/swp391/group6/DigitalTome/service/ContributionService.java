package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.dto.ContributionDto;
import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.exception.exceptionDefinition.ContributionGeneralErrorException;
import com.fpt.swp391.group6.DigitalTome.exception.exceptionDefinition.ContributionNotFoundException;
import com.fpt.swp391.group6.DigitalTome.exception.exceptionDefinition.UpdatingBookException;
import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import com.fpt.swp391.group6.DigitalTome.mapper.ContributionMapper;
import com.fpt.swp391.group6.DigitalTome.entity.ContributionEntity;
import com.fpt.swp391.group6.DigitalTome.repository.BookRepository;
import com.fpt.swp391.group6.DigitalTome.repository.ContributionRepository;
import com.sun.jna.WString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContributionService {

    private ContributionRepository contributionRepository;
    private BookRepository bookRepository;

    private ContributionMapper contributionMapper;

    @Autowired
    public ContributionService(ContributionRepository contributionRepository, ContributionMapper contributionMapper, BookRepository bookRepository) {
        this.contributionRepository = contributionRepository;
        this.contributionMapper = contributionMapper;
        this.bookRepository = bookRepository;
    }

    public List<ContributionDto> getContributionByStatus(int status, Pageable pageable){
        try {
            return  this.contributionRepository
                    .findByBookEntity_Status(status, pageable)
                    .stream()
                    .map(contributionMapper::toDto)
                    .collect(Collectors.toList());

        }catch (Exception e){
            throw new ContributionGeneralErrorException("Errors occur when getting contribution list");
        }
    }

    public int countContributionByStatus(int status){
        return  this.contributionRepository.countByBookEntity_Status(status);
    }

    public void updateModifiedDateByBookEntity_ISBN( String isbn){
       try {
           this.contributionRepository.updateModifiedDateByBookEntity_ISBN(isbn);
       }catch (Exception exception){
           throw new UpdatingBookException(exception.getMessage());
       }
    }

    public void saveContribution(ContributionEntity contribution) {
        BookEntity book = contribution.getBookEntity();
        if (book != null && book.getId() != null) {
            Optional<BookEntity> existingBook = bookRepository.findById(book.getId());
            if (existingBook.isPresent()) {
                contribution.setBookEntity(existingBook.get());
            } else {
                bookRepository.save(book);
            }
        }

        contributionRepository.save(contribution);
    }

    public boolean isAuthorOfBook(Long accountId, Long bookId) {
        return contributionRepository.existsByAccountEntityIdAndBookEntityId(accountId, bookId);
    }

    public List<Long> getBookIdsByAccountId(Long accountId) {
        return contributionRepository.findBookIdsByAccountId(accountId);
    }

    public ContributionEntity findByAccountAndBook(AccountEntity account, BookEntity book) {
        return contributionRepository.findByAccountEntityAndBookEntity(account, book);
    }

    /*public void deleteContributionBook(Long id){
        if(!contributionRepository.existsById(id))
            throw  new ContributionNotFoundException("Requested contribution book does not exist");
        contributionRepository.updateStatusById(0,id);
    }*/

    /*public ContributionEntity getContributionById(Long id) {
        return contributionRepository.findById(id).get();
    }*/
}
