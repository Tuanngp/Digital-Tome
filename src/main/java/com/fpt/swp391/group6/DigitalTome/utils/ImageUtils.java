package com.fpt.swp391.group6.DigitalTome.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
public class ImageUtils {

    private static Cloudinary cloudinary;

    @Autowired
    public ImageUtils(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public static String uploadImage(MultipartFile file, String directoryName) {

        try{
            Map<String, String> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "digital_tome/"+directoryName,
                    "use_filename", true,
                    "unique_filename", true,
                    "resource_type","auto"
            ));
            return  result.get("secure_url");
        }catch (IOException io){
            throw new RuntimeException("Image upload fail");
        }
    }

    public static Boolean destroyImage(String nameOfImage, String directoryName){
        try {
            Map result  = cloudinary.uploader().destroy(nameOfImage, ObjectUtils.asMap(
                    "folder", "digital_tome/"+directoryName,
                    "resource_type","image"
            ));
        }catch (IOException io){
            throw new RuntimeException("Image destroy fail");
        }
        return true;
    }
}
