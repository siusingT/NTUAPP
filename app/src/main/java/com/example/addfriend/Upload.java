package com.example.addfriend;

public class Upload {
    private String ImageName;
    private String ImageUrl;

    public Upload(String imageName, String imageUrl) {
        ImageName = imageName;
        ImageUrl = imageUrl;
    }

    public Upload() {}

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
