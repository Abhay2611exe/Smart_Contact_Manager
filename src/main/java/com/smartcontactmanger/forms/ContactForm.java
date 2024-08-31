package com.smartcontactmanger.forms;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactForm {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid Email address")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp="^[0-9]{10}$", message = "Invalid phone number")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    private String address;

    private String description;
    private String websiteLink;
    private String linkedInLink;
    private boolean favourite;
    
    
    private MultipartFile contactImage;

    private String picture;

}
