package com.swust.generator.server;

import java.io.File;

public class TestDelete {
    public static void main(String[] args) {
        String path = "admins/public/static/image/course/084bca53b703abd71846e7e3794b1098-net.mp4.1";
        File file = new File(path);
        boolean delete = file.delete();
        System.out.println(delete);
    }
}
