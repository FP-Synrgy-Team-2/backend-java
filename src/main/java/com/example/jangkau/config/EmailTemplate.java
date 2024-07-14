package com.example.jangkau.config;

import org.springframework.stereotype.Component;

@Component("emailTemplate")
public class EmailTemplate {
    public String getRegisterTemplate() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "\t.email-container {\n" +
                "\t\tpadding-top: 10px;\n" +
                "\t}\n" +
                "\tp {\n" +
                "\t\ttext-align: left;\n" +
                "\t}\n" +
                "\n" +
                "\ta.btn {\n" +
                "\t\tdisplay: block;\n" +
                "\t\tmargin: 30px auto;\n" +
                "\t\tbackground-color: #01c853;\n" +
                "\t\tpadding: 10px 20px;\n" +
                "\t\tcolor: #fff;\n" +
                "\t\ttext-decoration: none;\n" +
                "\t\twidth: 30%;\n" +
                "\t\ttext-align: center;\n" +
                "\t\tborder: 1px solid #01c853;\n" +
                "\t\ttext-transform: uppercase;\n" +
                "\t}\n" +
                "\ta.btn:hover,\n" +
                "\ta.btn:focus {\n" +
                "\t\tcolor: #01c853;\n" +
                "\t\tbackground-color: #fff;\n" +
                "\t\tborder: 1px solid #01c853;\n" +
                "\t}\n" +
                "\t.user-name {\n" +
                "\t\ttext-transform: uppercase;\n" +
                "\t}\n" +
                "\t.manual-link,\n" +
                "\t.manual-link:hover,\n" +
                "\t.manual-link:focus {\n" +
                "\t\tdisplay: block;\n" +
                "\t\tcolor: #396fad;\n" +
                "\t\tfont-weight: bold;\n" +
                "\t\tmargin-top: -15px;\n" +
                "\t}\n" +
                "\t.mt--15 {\n" +
                "\t\tmargin-top: -15px;\n" +
                "\t}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<div class=\"email-container\">\n" +
                "\t\t<p>Halo <span class=\"user-name\">{{USERNAME}}</span> Selamat bergabung</p>\n" +
                "\t\t<p>Harap konfirmasikan email kamu dengan memasukan kode dibawah ini</p>\n" +
                "\t\t\n" +
                "\t\tkode: <b>{{TOKEN}}</b>\n" +
                "\t\t\n" +
                "\t\t<p class=\"mt--15\">Jika kamu butuh bantuan atau pertanyaan, hubungi customer care kami di .... atau kirim email ke ....</p>\n" +
                "\t\t\n" +
                "\t\t<p>Semoga harimu menyenangkan!</p>\n" +
                "\t\t\n" +
                "\t\t<p>PT ABC,</p>\n" +
                "\t\t<p class=\"mt--15\".....</p>\n" +
                "\n" +
                "\t\t\n" +
                "\t</div>\n" +
                "</body>\n" +
                "</html>";
    }

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
                "<td style=\"text-align:center;\">\n" +
                "<a href=\"#\" title=\"logo\" target=\"_blank\">\n" +
                "<img width=\"60\" src=\"https://drive.google.com/uc?export=view&id=1NiO5cQZDXA31eFJozu_wrEHUK4vbzz1o\" title=\"logo\" alt=\"logo\">\n" +
                "</a>\n" +
                "</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td style=\"height:20px;\">&nbsp;</td>\n"+
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
                "If this action wasn’t done by you, please contact us on <b>ridhoga186@gmail.com</b>. But if it’s you, you can ignore this message. </p>" +
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

