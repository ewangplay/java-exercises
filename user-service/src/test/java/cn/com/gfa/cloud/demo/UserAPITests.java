package cn.com.gfa.cloud.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import cn.com.gfa.cloud.demo.IO.UserRequest;
import cn.com.gfa.cloud.demo.Model.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class UserAPITests {
    @Autowired
    private TestRestTemplate template;

    @Test
    @Order(1)
    public void addUserWithoutName() {
        UserRequest req = new UserRequest();

        Response response = template.postForObject("/users/add", req, Response.class);
        assertThat(response.getCode()).isEqualTo(ErrorCode.RequestParamInvalid.getCode());
        assertThat(response.getMessage()).isEqualTo(ErrorCode.RequestParamInvalid.getMessage());
    }

    @Test
    @Order(2)
    public void addUser() {
        UserRequest req = new UserRequest();
        req.setName("Tom");

        Response response = template.postForObject("/users/add", req, Response.class);
        assertThat(response.getCode()).isEqualTo(ErrorCode.Success.getCode());
        assertThat(response.getMessage()).isEqualTo(ErrorCode.Success.getMessage());
    }

    @Test
    @Order(3)
    public void getUserWithoutId() {
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("id", 0);

        ResponseEntity<Response> response = template.exchange("/users/get/{id}", HttpMethod.GET, null, Response.class,
                params);
        assertThat(response.getBody().getCode()).isEqualTo(ErrorCode.RequestParamInvalid.getCode());
        assertThat(response.getBody().getMessage()).isEqualTo(ErrorCode.RequestParamInvalid.getMessage());
    }

    @Test
    @Order(4)
    public void getUserById() throws JsonProcessingException {
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("id", 7);

        // Find users
        Response resp1 = template.getForObject("/users/get/{id}", Response.class, params);
        assertThat(resp1.getCode()).isEqualTo(ErrorCode.Success.getCode());
        assertThat(resp1.getMessage()).isEqualTo(ErrorCode.Success.getMessage());
        assertThat(resp1.getData()).isNotNull();

        ObjectMapper mapper = new ObjectMapper();
        String strUser = mapper.writeValueAsString(resp1.getData());
        User u = mapper.readValue(strUser, User.class);
        assertThat(u.getId()).isEqualTo(7);
    }

    @Test
    @Order(5)
    public void delUserWithoutId() {
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("id", 0);

        ResponseEntity<Response> response = template.exchange("/users/delete/{id}", HttpMethod.DELETE, null,
                Response.class,
                params);
        assertThat(response.getBody().getCode()).isEqualTo(ErrorCode.RequestParamInvalid.getCode());
        assertThat(response.getBody().getMessage()).isEqualTo(ErrorCode.RequestParamInvalid.getMessage());
    }

    @Test
    @Order(6)
    public void delUserSucc() {
        // Delete special user
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("id", 7);

        ResponseEntity<Response> response = template.exchange("/users/delete/{id}", HttpMethod.DELETE, null,
                Response.class,
                params);
        assertThat(response.getBody().getCode()).isEqualTo(ErrorCode.Success.getCode());
        assertThat(response.getBody().getMessage()).isEqualTo(ErrorCode.Success.getMessage());
    }

}
