package webserver;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {
    @ParameterizedTest
    @MethodSource("run")
    void run(String message, String expectedResponseMessage) throws IOException {
        int port = ThreadLocalRandom.current().nextInt(50000, 60000);
        ServerSocket server = new ServerSocket(port);

        Socket browser = new Socket("localhost", port);

        RequestHandler requestHandler = new RequestHandler(server.accept());

        BufferedOutputStream browserStream = new BufferedOutputStream(browser.getOutputStream());


        browserStream.write(message.getBytes(StandardCharsets.UTF_8));
        browserStream.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
        browserStream.flush();

        requestHandler.start();

        BufferedReader br = new BufferedReader(new InputStreamReader(browser.getInputStream()));

        assertThat(br.lines().collect(Collectors.joining(System.lineSeparator()))).isEqualTo(expectedResponseMessage);
    }

    static Stream<Arguments> run() {
        return Stream.of(
                Arguments.arguments(
                        "GET /index.html HTTP/1.1" + System.lineSeparator() +
                                "Host: localhost:8080" + System.lineSeparator() +
                                "Connection: keep-alive" + System.lineSeparator() +
                                "Accept: */*" + System.lineSeparator() +
                                "" + System.lineSeparator(),
                        "HTTP/1.1 200 OK" + System.lineSeparator() +
                                "Content-Type: text/html;charset=utf-8" + System.lineSeparator() +
                                "Content-Length: 7051" + System.lineSeparator() +
                                "" + System.lineSeparator() +
                                "<!DOCTYPE html>" + System.lineSeparator() +
                                "<html lang=\"kr\">" + System.lineSeparator() +
                                "\t<head>" + System.lineSeparator() +
                                "\t\t<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">" + System.lineSeparator() +
                                "\t\t<meta charset=\"utf-8\">" + System.lineSeparator() +
                                "\t\t<title>SLiPP Java Web Programming</title>" + System.lineSeparator() +
                                "\t\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">" + System.lineSeparator() +
                                "\t\t<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">" + System.lineSeparator() +
                                "\t\t<!--[if lt IE 9]>" + System.lineSeparator() +
                                "\t\t\t<script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script>" + System.lineSeparator() +
                                "\t\t<![endif]-->" + System.lineSeparator() +
                                "\t\t<link href=\"css/styles.css\" rel=\"stylesheet\">" + System.lineSeparator() +
                                "\t</head>" + System.lineSeparator() +
                                "\t" + System.lineSeparator() +
                                "\t<body>" + System.lineSeparator() +
                                "<nav class=\"navbar navbar-fixed-top header\">" + System.lineSeparator() +
                                " \t<div class=\"col-md-12\">" + System.lineSeparator() +
                                "        <div class=\"navbar-header\">" + System.lineSeparator() +
                                "" + System.lineSeparator() +
                                "            <a href=\"index.html\" class=\"navbar-brand\">SLiPP</a>" + System.lineSeparator() +
                                "          <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse1\">" + System.lineSeparator() +
                                "          <i class=\"glyphicon glyphicon-search\"></i>" + System.lineSeparator() +
                                "          </button>" + System.lineSeparator() +
                                "      " + System.lineSeparator() +
                                "        </div>" + System.lineSeparator() +
                                "        <div class=\"collapse navbar-collapse\" id=\"navbar-collapse1\">" + System.lineSeparator() +
                                "          <form class=\"navbar-form pull-left\">" + System.lineSeparator() +
                                "              <div class=\"input-group\" style=\"max-width:470px;\">" + System.lineSeparator() +
                                "                <input type=\"text\" class=\"form-control\" placeholder=\"Search\" name=\"srch-term\" id=\"srch-term\">" + System.lineSeparator() +
                                "                <div class=\"input-group-btn\">" + System.lineSeparator() +
                                "                  <button class=\"btn btn-default btn-primary\" type=\"submit\"><i class=\"glyphicon glyphicon-search\"></i></button>" + System.lineSeparator() +
                                "                </div>" + System.lineSeparator() +
                                "              </div>" + System.lineSeparator() +
                                "          </form>" + System.lineSeparator() +
                                "          <ul class=\"nav navbar-nav navbar-right\">             " + System.lineSeparator() +
                                "             <li>" + System.lineSeparator() +
                                "                <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-bell\"></i></a>" + System.lineSeparator() +
                                "                <ul class=\"dropdown-menu\">" + System.lineSeparator() +
                                "                  <li><a href=\"https://slipp.net\" target=\"_blank\">SLiPP</a></li>" + System.lineSeparator() +
                                "                  <li><a href=\"https://facebook.com\" target=\"_blank\">Facebook</a></li>" + System.lineSeparator() +
                                "                </ul>" + System.lineSeparator() +
                                "             </li>" + System.lineSeparator() +
                                "             <li><a href=\"./user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>" + System.lineSeparator() +
                                "           </ul>" + System.lineSeparator() +
                                "        </div>\t" + System.lineSeparator() +
                                "     </div>\t" + System.lineSeparator() +
                                "</nav>" + System.lineSeparator() +
                                "<div class=\"navbar navbar-default\" id=\"subnav\">" + System.lineSeparator() +
                                "    <div class=\"col-md-12\">" + System.lineSeparator() +
                                "        <div class=\"navbar-header\">" + System.lineSeparator() +
                                "            <a href=\"#\" style=\"margin-left:15px;\" class=\"navbar-btn btn btn-default btn-plus dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-home\" style=\"color:#dd1111;\"></i> Home <small><i class=\"glyphicon glyphicon-chevron-down\"></i></small></a>" + System.lineSeparator() +
                                "            <ul class=\"nav dropdown-menu\">" + System.lineSeparator() +
                                "                <li><a href=\"user/profile.html\"><i class=\"glyphicon glyphicon-user\" style=\"color:#1111dd;\"></i> Profile</a></li>" + System.lineSeparator() +
                                "                <li class=\"nav-divider\"></li>" + System.lineSeparator() +
                                "                <li><a href=\"#\"><i class=\"glyphicon glyphicon-cog\" style=\"color:#dd1111;\"></i> Settings</a></li>" + System.lineSeparator() +
                                "            </ul>" + System.lineSeparator() +
                                "            " + System.lineSeparator() +
                                "            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse2\">" + System.lineSeparator() +
                                "            \t<span class=\"sr-only\">Toggle navigation</span>" + System.lineSeparator() +
                                "            \t<span class=\"icon-bar\"></span>" + System.lineSeparator() +
                                "            \t<span class=\"icon-bar\"></span>" + System.lineSeparator() +
                                "            \t<span class=\"icon-bar\"></span>" + System.lineSeparator() +
                                "            </button>            " + System.lineSeparator() +
                                "        </div>" + System.lineSeparator() +
                                "        <div class=\"collapse navbar-collapse\" id=\"navbar-collapse2\">" + System.lineSeparator() +
                                "            <ul class=\"nav navbar-nav navbar-right\">" + System.lineSeparator() +
                                "                <li class=\"active\"><a href=\"index.html\">Posts</a></li>" + System.lineSeparator() +
                                "                <li><a href=\"user/login.html\" role=\"button\">로그인</a></li>" + System.lineSeparator() +
                                "                <li><a href=\"user/form.html\" role=\"button\">회원가입</a></li>" + System.lineSeparator() +
                                "                <!--" + System.lineSeparator() +
                                "                <li><a href=\"#loginModal\" role=\"button\" data-toggle=\"modal\">로그인</a></li>" + System.lineSeparator() +
                                "                <li><a href=\"#registerModal\" role=\"button\" data-toggle=\"modal\">회원가입</a></li>" + System.lineSeparator() +
                                "                -->" + System.lineSeparator() +
                                "                <li><a href=\"#\" role=\"button\">로그아웃</a></li>" + System.lineSeparator() +
                                "                <li><a href=\"#\" role=\"button\">개인정보수정</a></li>" + System.lineSeparator() +
                                "            </ul>" + System.lineSeparator() +
                                "        </div>" + System.lineSeparator() +
                                "    </div>" + System.lineSeparator() +
                                "</div>" + System.lineSeparator() +
                                "" + System.lineSeparator() +
                                "<div class=\"container\" id=\"main\">" + System.lineSeparator() +
                                "   <div class=\"col-md-12 col-sm-12 col-lg-10 col-lg-offset-1\">" + System.lineSeparator() +
                                "      <div class=\"panel panel-default qna-list\">" + System.lineSeparator() +
                                "          <ul class=\"list\">" + System.lineSeparator() +
                                "              <li>" + System.lineSeparator() +
                                "                  <div class=\"wrap\">" + System.lineSeparator() +
                                "                      <div class=\"main\">" + System.lineSeparator() +
                                "                          <strong class=\"subject\">" + System.lineSeparator() +
                                "                              <a href=\"./qna/show.html\">국내에서 Ruby on Rails와 Play가 활성화되기 힘든 이유는 뭘까?</a>" + System.lineSeparator() +
                                "                          </strong>" + System.lineSeparator() +
                                "                          <div class=\"auth-info\">" + System.lineSeparator() +
                                "                              <i class=\"icon-add-comment\"></i>" + System.lineSeparator() +
                                "                              <span class=\"time\">2016-01-15 18:47</span>" + System.lineSeparator() +
                                "                              <a href=\"./user/profile.html\" class=\"author\">자바지기</a>" + System.lineSeparator() +
                                "                          </div>" + System.lineSeparator() +
                                "                          <div class=\"reply\" title=\"댓글\">" + System.lineSeparator() +
                                "                              <i class=\"icon-reply\"></i>" + System.lineSeparator() +
                                "                              <span class=\"point\">8</span>" + System.lineSeparator() +
                                "                          </div>" + System.lineSeparator() +
                                "                      </div>" + System.lineSeparator() +
                                "                  </div>" + System.lineSeparator() +
                                "              </li>" + System.lineSeparator() +
                                "              <li>" + System.lineSeparator() +
                                "                  <div class=\"wrap\">" + System.lineSeparator() +
                                "                      <div class=\"main\">" + System.lineSeparator() +
                                "                          <strong class=\"subject\">" + System.lineSeparator() +
                                "                              <a href=\"./qna/show.html\">runtime 에 reflect 발동 주체 객체가 뭔지 알 방법이 있을까요?</a>" + System.lineSeparator() +
                                "                          </strong>" + System.lineSeparator() +
                                "                          <div class=\"auth-info\">" + System.lineSeparator() +
                                "                              <i class=\"icon-add-comment\"></i>" + System.lineSeparator() +
                                "                              <span class=\"time\">2016-01-05 18:47</span>" + System.lineSeparator() +
                                "                              <a href=\"./user/profile.html\" class=\"author\">김문수</a>" + System.lineSeparator() +
                                "                          </div>" + System.lineSeparator() +
                                "                          <div class=\"reply\" title=\"댓글\">" + System.lineSeparator() +
                                "                              <i class=\"icon-reply\"></i>" + System.lineSeparator() +
                                "                              <span class=\"point\">12</span>" + System.lineSeparator() +
                                "                          </div>" + System.lineSeparator() +
                                "                      </div>" + System.lineSeparator() +
                                "                  </div>" + System.lineSeparator() +
                                "              </li>" + System.lineSeparator() +
                                "          </ul>" + System.lineSeparator() +
                                "          <div class=\"row\">" + System.lineSeparator() +
                                "              <div class=\"col-md-3\"></div>" + System.lineSeparator() +
                                "              <div class=\"col-md-6 text-center\">" + System.lineSeparator() +
                                "                  <ul class=\"pagination center-block\" style=\"display:inline-block;\">" + System.lineSeparator() +
                                "                      <li><a href=\"#\">«</a></li>" + System.lineSeparator() +
                                "                      <li><a href=\"#\">1</a></li>" + System.lineSeparator() +
                                "                      <li><a href=\"#\">2</a></li>" + System.lineSeparator() +
                                "                      <li><a href=\"#\">3</a></li>" + System.lineSeparator() +
                                "                      <li><a href=\"#\">4</a></li>" + System.lineSeparator() +
                                "                      <li><a href=\"#\">5</a></li>" + System.lineSeparator() +
                                "                      <li><a href=\"#\">»</a></li>" + System.lineSeparator() +
                                "                </ul>" + System.lineSeparator() +
                                "              </div>" + System.lineSeparator() +
                                "              <div class=\"col-md-3 qna-write\">" + System.lineSeparator() +
                                "                  <a href=\"./qna/form.html\" class=\"btn btn-primary pull-right\" role=\"button\">질문하기</a>" + System.lineSeparator() +
                                "              </div>" + System.lineSeparator() +
                                "          </div>" + System.lineSeparator() +
                                "        </div>" + System.lineSeparator() +
                                "    </div>" + System.lineSeparator() +
                                "</div>" + System.lineSeparator() +
                                "" + System.lineSeparator() +
                                "<!-- script references -->" + System.lineSeparator() +
                                "<script src=\"js/jquery-2.2.0.min.js\"></script>" + System.lineSeparator() +
                                "<script src=\"js/bootstrap.min.js\"></script>" + System.lineSeparator() +
                                "<script src=\"js/scripts.js\"></script>" + System.lineSeparator() +
                                "\t</body>" + System.lineSeparator() +
                                "</html>"
                ), Arguments.arguments(
                        "GET /index2.html HTTP/1.1" + System.lineSeparator() +
                                "Host: localhost:8080" + System.lineSeparator() +
                                "Connection: keep-alive" + System.lineSeparator() +
                                "Accept: */*" + System.lineSeparator() +
                                "" + System.lineSeparator(),
                        "HTTP/1.1 404 NotFound" + System.lineSeparator()
                )
        );
    }
}
