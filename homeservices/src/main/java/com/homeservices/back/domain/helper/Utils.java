package com.homeservices.back.domain.helper;

public class Utils {
    public static String buildEmail(String name, String link) {
        return "<div>" +
                "Thank you for registering. Please click on the below link to activate your account: <a href=\"" + link + "\">Activate Now</a> " +
                "<p>See you soon</p>" +
                "</div>";
    }
}
