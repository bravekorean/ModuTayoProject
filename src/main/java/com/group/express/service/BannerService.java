package com.group.express.service;

import com.group.express.domain.Banner;
import com.group.express.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BannerService {
    private final BannerRepository bannerRepository;

    @Autowired
    public BannerService(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    public Optional<Banner> getBannerById(int id) {
        if (bannerRepository.existsById((long) id)) {
            return Optional.of(bannerRepository.getReferenceById((long) id));
        } else {
            return Optional.empty();
        }
    }

    public Banner saveBanner(Banner banner){
        return bannerRepository.save(banner);
    }

    public List<Banner> getBannerList() {
        return bannerRepository.findAll();
    }
}
