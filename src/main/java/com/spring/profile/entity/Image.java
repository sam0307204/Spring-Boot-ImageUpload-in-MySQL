package com.spring.profile.entity;

import org.apache.tomcat.util.codec.binary.Base64;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profiles")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String designation;

    @Column
    private String githubLink;

    @Column
    private String twitterLink;

    @Column
    private String email;

    @Lob
    @Column
    private byte[] imageData;

    public String generateBase64Image() {
        return Base64.encodeBase64String(this.imageData);
    }
}