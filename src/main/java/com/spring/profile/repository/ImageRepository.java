package com.spring.profile.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.profile.entity.Image;



@Repository
@Transactional(readOnly = true)
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByFilename(String filename);

    boolean existsByFilename(String filename);

    @Query("SELECT i.filename FROM Image i")
    List<String> findAllFilenames();

    @Modifying
    @Transactional
    @Query("DELETE FROM Image i WHERE i.filename = :filename")
    int deleteByFilename(@Param("filename") String filename);
}
