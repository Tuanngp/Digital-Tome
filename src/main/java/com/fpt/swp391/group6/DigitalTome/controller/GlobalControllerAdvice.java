package com.fpt.swp391.group6.DigitalTome.controller;

import com.fpt.swp391.group6.DigitalTome.entity.AccountEntity;
import com.fpt.swp391.group6.DigitalTome.service.AdsService;
import com.fpt.swp391.group6.DigitalTome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserService userService;
    private final AdsService adsService;

    @Autowired
    public GlobalControllerAdvice(UserService userService, AdsService adsService) {
        this.userService = userService;
        this.adsService = adsService;
    }

    @ModelAttribute
    public void setAccountInGlobal(@AuthenticationPrincipal OAuth2User oAuth2User, Principal principal, Model model) {
        AccountEntity accountEntity = null;
        if (principal != null) {
            String username = principal.getName();
            accountEntity = userService.findByUsername(username);
        } else if (oAuth2User != null){
            accountEntity = userService.findByUsername(oAuth2User.getAttribute("email"));
        }
        model.addAttribute("account", accountEntity);
    }

    @ModelAttribute
    public void setAdsInGlobal(Model model) {
        model.addAttribute("adsHomepage", adsService.getAdsHomepage());
        model.addAttribute("adsFooter", adsService.getAdsFooter());
    }

}
