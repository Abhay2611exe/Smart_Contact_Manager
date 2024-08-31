package com.smartcontactmanger.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    public String uploadImage(MultipartFile contactImage, String filename);
    public String getUrlFromPublicId(String publicId);

}
