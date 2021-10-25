package com.example.ntumobile;

public class uploadImage_get {
    private String ImageName;
    private String ImageUrl;

    public uploadImage_get(String imageName, String imageUrl) {
        ImageName = imageName;
        ImageUrl = imageUrl;
    }

    public uploadImage_get() {}

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
