package cn.com.gfa.cloud.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import cn.com.gfa.cloud.demo.Common.ErrorCode;
import cn.com.gfa.cloud.demo.IO.Response;
import cn.com.gfa.cloud.demo.Model.Address;
import cn.com.gfa.cloud.demo.Model.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class UserAPITests {
    // User id to test
    final static Integer userId = 29;
    final static String userName = "张三";
    final static int userAge = 20;
    final static User.Sex userSex = User.Sex.男;
    final static String userProvince = "北京";
    final static String userCity = "北京市";
    final static String userArea = "朝阳区";
    final static String userAddress = "胜利花园B座";
    final static String userZipcode = "100000";
    final static String[] userHobbies = { "爬山", "游泳", "阅读" };
    static Date userBirthday;

    @Autowired
    private TestRestTemplate template;

    @BeforeAll
    public static void beforeAllTests() {
        try {
            userBirthday = new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-8");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(1)
    public void addUserWithoutName() {
        User user = new User();

        Response response = template.postForObject("/users/add", user, Response.class);
        assertRequestParamInvalidResponse(response);
    }

    @Test
    @Order(2)
    public void addUser() {
        User user = newUser();

        Response response = template.postForObject("/users/add", user, Response.class);
        assertSuccessResponse(response);
    }

    @Test
    @Order(3)
    public void getUserWithoutId() {
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("id", 0);

        ResponseEntity<Response> response = template.getForEntity("/users/getbyid?id={id}", Response.class, params);
        assertRequestParamInvalidResponse(response.getBody());
    }

    @Test
    @Disabled
    @Order(4)
    public void getUserById() throws JsonProcessingException {
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("id", userId);

        // Find users
        Response resp1 = template.getForObject("/users/getbyid?id={id}", Response.class, params);
        assertSuccessResponse(resp1);

        assertNotNull(resp1.getData());
        ObjectMapper mapper = new ObjectMapper();
        String strUser = mapper.writeValueAsString(resp1.getData());
        User u = mapper.readValue(strUser, User.class);
        assertEquals(userId, u.getId());
    }

    @Test
    @Order(5)
    public void getUserWithoutName() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", "");

        Response response = template.getForObject("/users/getbyname?name={name}", Response.class, params);
        assertRequestParamInvalidResponse(response);
    }

    @Test
    @Order(6)
    public void getUserByName() throws JsonProcessingException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", userName);

        // Find users
        Response resp1 = template.getForObject("/users/getbyname?name={name}", Response.class, params);
        assertSuccessResponse(resp1);
        assertUserList(resp1.getData());
    }

    @Test
    @Order(11)
    public void delUserWithoutId() {
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("id", 0);

        ResponseEntity<Response> response = template.exchange("/users/deletebyid?id={id}", HttpMethod.DELETE, null,
                Response.class,
                params);
        assertRequestParamInvalidResponse(response.getBody());
    }

    @Test
    @Disabled
    @Order(12)
    public void delUserById() {
        // Delete special user
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("id", userId);

        ResponseEntity<Response> response = template.exchange("/users/deletebyid?id={id}", HttpMethod.DELETE, null,
                Response.class,
                params);
        assertSuccessResponse(response.getBody());
    }

    @Test
    @Order(13)
    public void delUserWithoutName() {
        // Delete special user
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", "");

        ResponseEntity<Response> response = template.exchange("/users/deletebyname?name={name}", HttpMethod.DELETE,
                null,
                Response.class,
                params);
        assertRequestParamInvalidResponse(response.getBody());
    }

    @Test
    @Order(14)
    public void delUserByName() throws JsonProcessingException {
        // Delete special user
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", userName);

        ResponseEntity<Response> response = template.exchange("/users/deletebyname?name={name}", HttpMethod.DELETE,
                null,
                Response.class,
                params);
        assertSuccessResponse(response.getBody());
        //assertUserList(response.getBody().getData());
    }

    private User newUser() {
        User user = new User();
        user.setName(userName);
        user.setAge(userAge);
        user.setBirthday(userBirthday);
        user.setSex(userSex);
        Address addr = new Address();
        addr.setProvince(userProvince);
        addr.setCity(userCity);
        addr.setArea(userArea);
        addr.setAddress(userAddress);
        addr.setZipcode(userZipcode);
        user.setAddress(addr);
        List<String> hobbies = new ArrayList<String>();
        for (String h : userHobbies) {
            hobbies.add(h);
        }
        user.setHobbies(hobbies);
        return user;
    }

    private void assertRequestParamInvalidResponse(Response response) {
        assertEquals(ErrorCode.RequestParamInvalid.getCode(), response.getCode());
        assertEquals(ErrorCode.RequestParamInvalid.getMessage(), response.getMessage());
    }

    private void assertSuccessResponse(Response response) {
        assertEquals(ErrorCode.Success.getCode(), response.getCode());
        assertEquals(ErrorCode.Success.getMessage(), response.getMessage());
    }

    private void assertUserList(Object obj) throws JsonProcessingException {
        assertNotNull(obj);

        ObjectMapper mapper = new ObjectMapper();
        String jsonUsers = mapper.writeValueAsString(obj);
        System.out.println(jsonUsers);
        User[] users = mapper.readValue(jsonUsers, User[].class);
        assertTrue(users.length > 0);
        for (User u : users) {
            assertEquals(userName, u.getName());
            assertEquals(userAge, u.getAge());
            // assertEquals(userBirthday, u.getBirthday());
            assertEquals(userSex, u.getSex());
            assertEquals(userProvince, u.getAddress().getProvince());
            assertEquals(userCity, u.getAddress().getCity());
            assertEquals(userArea, u.getAddress().getArea());
            assertEquals(userAddress, u.getAddress().getAddress());
            assertEquals(userZipcode, u.getAddress().getZipcode());
            for (String h : userHobbies) {
                assertTrue(u.getHobbies().contains(h));
            }
        }
    }
}
