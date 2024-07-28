package com.example.jangkau.config;

import org.springframework.stereotype.Component;

@Component("emailTemplate")
public class EmailTemplate {
    public String getResetPassword(){

        return "<!doctype html>\n" +
                "<html lang=\"en-US\">\n" +
                "<head>" +
                "<meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\" />\n" +
                "<title>Reset Password Email Template</title>\n" +
                "<meta name=\"description\" content=\"Reset Password Email Template.\"> \n" +
                "<style type=\"text/css\"> \n" +
                "a:hover {text-decoration: underline !important;}\n" +
                "</style> \n" +
                "</head>\n" +
                "<body marginheight=\"0\" topmargin=\"0\" marginwidth=\"0\" style=\"margin: 0px; background-color: #f2f3f8;\" leftmargin=\"0\">\n" +
                "<table cellspacing=\"0\" border=\"0\" cellpadding=\"0\" width=\"100%\" bgcolor=\"#f2f3f8\"\n" +
                "style=\"@import url(https://fonts.googleapis.com/css?family=Rubik:300,400,500,700|Open+Sans:300,400,600,700); font-family: 'Open Sans', sans-serif;\">\n" +
                "<tr>\n" +
                "<td>\n" +
                "<table style=\"background-color: #f2f3f8; max-width:670px;  margin:0 auto;\" width=\"100%\" border=\"0\"" +
                "align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "<tr>\n" +
                "<td style=\"height:80px;\">&nbsp;</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>\n" +
                "<table width=\"95%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"" +
                "style=\"max-width:670px;background:#fff; border-radius:3px; text-align:center;-webkit-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);-moz-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);box-shadow:0 6px 18px 0 rgba(0,0,0,.06);\">\n" +
                "<tr>\n" +
                "<td style=\"height:40px;\">&nbsp;</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td style=\"padding:0 35px;\">\n" +
                "<h1 style=\"color:#1e1e2d; font-weight:500; margin:0;font-size:32px;font-family:'Rubik',sans-serif;\">Hi {{USERNAME}}, you have requested verification code for your password.</h1>\n" +
                "<span " +
                "style=\"display:inline-block; vertical-align:middle; margin:29px 0 26px; border-bottom:1px solid #cecece; width:100px;\"></span>\n" +
                "<p style=\"color:#455056; font-size:15px;line-height:24px; margin:0;\">\n" +
                "Please use the verification code below to reset your password : <br/> </p>" +
                "<strong style=\"font-size:24px;\">{{TOKEN}}</strong> <br/>\n" +
                "<p style=\"color:#455056; font-size:15px;line-height:24px; margin:0;\">\n" +
                "If this action wasn’t done by you, please contact us on <b>jangkauofficial@gmail.com</b>. But if it’s you, you can ignore this message. </p>" +
                "</td>\n"+
                "</tr>\n"+
                "<tr>\n" +
                "<td style=\"height:40px;\">&nbsp;</td>" +
                "</tr>\n"+
                "</table>\n"+
                "</td>\n" +
                "<tr>\n" +
                "<td style=\"height:20px;\">&nbsp;</td>\n" +
                "</tr>\n" +
                "<tr>\n"+
                "<td style=\"text-align:center;\">\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td style=\"height:80px;\">&nbsp;</td>\n"+
                "</tr>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>\n";
    }

}

