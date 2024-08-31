package com.smartcontactmanger.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import static jakarta.persistence.FetchType.EAGER;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Contact {

    @Id
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String picture;
    private String description;
    private boolean favourite = false;
    private String websiteLink;
    private String linkedInLink;
    private String cloudinaryImagePublicId;

    // private List<String> socaiLinks= new ArrayList<>();
    @ManyToOne
    @com.fasterxml.jackson.annotation.JsonIgnore
    private User user;


    // RelationShip with SocialLinks
    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, fetch = EAGER, orphanRemoval = true)
    private List<SocialLinks> socialLinks = new ArrayList<>();

}
