package cart.controller;

import static org.hamcrest.Matchers.containsString;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ViewControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("인덱스 페이지를 잘 호출하는지 확인한다.")
    void productList() {
        RestAssured.given().log().all()
                .accept(MediaType.TEXT_HTML_VALUE)
                .when().get("/")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("상품목록"));
    }

    @Test
    @DisplayName("관리자 페이지를 잘 호출하는지 확인한다.")
    void admin() {
        RestAssured.given().log().all()
                .accept(MediaType.TEXT_HTML_VALUE)
                .when().get("/admin")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("<title>관리자 페이지</title>"));
    }

    @Test
    @DisplayName("설정 페이지를 잘 호출하는지 확인한다.")
    void settings() {
        RestAssured.given().log().all()
                .accept(MediaType.TEXT_HTML_VALUE)
                .when().get("/settings")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("사용자 선택"));
    }

    @Test
    @DisplayName("장바구니 페이지를 잘 호출하는지 확인한다.")
    void cart() {
        RestAssured.given().log().all()
                .accept(MediaType.TEXT_HTML_VALUE)
                .when().get("/cart")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("<title>장바구니</title>"));
    }

}
