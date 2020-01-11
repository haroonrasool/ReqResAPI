package reqResAPI;

import com.google.gson.JsonObject;
import io.restassured.response.Response;
import org.hamcrest.core.Is;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

public class ReqResAPI {
    @Test
    public void list_Users() {
        Response response = given().when().get("https://reqres.in/api/users?page=2");
        response.prettyPrint(); // output on the console in JSON format
        response.then().assertThat().statusCode(200).and()
                .body("page", is(2)).and()
                .body("data.id", hasItems(7, 8, 9, 10, 11, 12));
    }

    @Test
    public void single_User() {
        Response response = given().when().get("https://reqres.in/api/users/2");
        response.prettyPrint();
        response.then().assertThat().statusCode(200)
                .body("data.id", is(2))
                .body("data.email", endsWith(".in"))
                .body("data.first_name", equalTo("Janet"))
                .body("data.last_name", equalTo("Weaver"))
                .body("data.avatar", containsString("josephstein/128.jpg"));
    }

    @Test
    public void single_User_Not_Found() {
        Response response = given().when().get("https://reqres.in/api/users/23");
        response.prettyPrint();
        response.then().assertThat().statusCode(404)
                .body("{}", isEmptyOrNullString());
    }

    @Test
    public void list_Resource() {
        Response response = given().when().get("https://reqres.in/api/unknown");
        response.prettyPrint();
        response.then().assertThat().statusCode(200)
                .body("page", is(1)).and()
                .body("data.id", hasItems(1, 2, 3))
                .body("data.name", hasItems("cerulean", "fuchsia rose", "true red"))
                .body("data.year", hasItems(2000, 2001, 2002))
                .body("data.color", hasItems("#98B2D1", "#C74375", "#BF1932"))
                .body("data.pantone_value", hasItems("15-4020", "17-2031", "19-1664"));
    }

    @Test
    public void single_Resource() {
        Response response = given().when().get("https://reqres.in/api/unknown/2");
        response.prettyPrint();
        response.then().assertThat().statusCode(200)
                .body("data.id", is(2))
                .body("data.name", is("fuchsia rose"))
                .body("data.year", is(2001))
                .body("data.color", is("#C74375"))
                .body("data.pantone_value", is("17-2031"));
    }

    @Test
    public void single_resource_not_found() {
        Response response = given().when().get("https://reqres.in/api/unknown/23");
        response.prettyPrint();
        response.then().assertThat().statusCode(404)
                .body("{}", isEmptyOrNullString());// running
    }

    @Test
    public void delayed_response() {
        Response response = given().when().get("https://reqres.in/api/users?delay=3");
        response.prettyPrint();
        response.then().assertThat().statusCode(200)
                .body("page", is(1))
                .body("data.id", hasItems(1, 2, 3))
                .body("data.first_name", hasItems("George", "Janet", "Emma"))
                .body("data.last_name", hasItems("Bluth", "Weaver", "Wong"));
    }

    @Test
    public void createUser() {
        String payload = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"leader\"\n" +
                "}";
        Response response = given().contentType("application/json").when().body(payload).post("https://reqres.in/api/users");
        response.prettyPrint();
        response.then().assertThat().statusCode(201)
                .body("name", Is.is("morpheus"), "job", Is.is("leader"));
    }

    @Test
    public void deleteUser() {
        Response response = given().when().delete("https://reqres.in/api/users");
        response.prettyPrint();
        response.then().assertThat().statusCode(204);
    }

    @Test
    public void updateUser() {
        String payload = "{\n" +
                "    \"name\": \"Spider Man\",\n" +
                "    \"job\": \"Good Guy\"\n" +
                "}";
        Response response = given().contentType("application/json").when().body(payload).put("https://reqres.in/api/users");
        response.prettyPrint();
        response.then().assertThat().statusCode(200);
    }
    @Test
    public void register_successful() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", "eve.holt@reqres.in");
        jsonObject.addProperty("password", "pistol");
        given().contentType("application/json").when().body(jsonObject)
                .post("https://reqres.in/api/register").prettyPrint();
    }

    @Test
    public void login_successful() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", "eve.holt@reqres.in");
        jsonObject.addProperty("password", "pistol");
        given().contentType("application/json").when().body(jsonObject)
                .post("https://reqres.in/api/login").prettyPrint();
    }
}
