package com.spring.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.spring.profile.entity.Image;




public interface ImageRepository extends JpaRepository<Image, Long> {
}
