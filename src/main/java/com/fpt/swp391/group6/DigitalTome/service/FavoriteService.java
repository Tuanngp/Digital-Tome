package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.entity.FavoriteEntity;
import com.fpt.swp391.group6.DigitalTome.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService  {

    private FavoriteRepository favoriteRepository;

    @Autowired
    public FavoriteService (FavoriteRepository historyRepository) {
        this.favoriteRepository = historyRepository;
    }

    public List<FavoriteEntity> getFavoritesByUserId(Long userId){
        return favoriteRepository.findByAccountEntityId(userId);
    }

    public FavoriteEntity addFavorite(FavoriteEntity favorite) {
        return favoriteRepository.save(favorite);
    }
    public void removeFavorite(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }
}
