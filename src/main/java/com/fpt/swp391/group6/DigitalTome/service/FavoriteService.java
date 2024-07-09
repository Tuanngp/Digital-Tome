package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.entity.FavoriteEntity;
import com.fpt.swp391.group6.DigitalTome.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService  {

    private final FavoriteRepository favoriteRepository;

    @Autowired
    public FavoriteService (FavoriteRepository historyRepository) {
        this.favoriteRepository = historyRepository;
    }

    public List<FavoriteEntity> getFavoritesByUser(AccountEntity user){
        return favoriteRepository.findByAccountEntity(user);
    }

    public FavoriteEntity addFavorite(FavoriteEntity favorite) {
        return favoriteRepository.save(favorite);
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
