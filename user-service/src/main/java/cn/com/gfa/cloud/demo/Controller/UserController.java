package cn.com.gfa.cloud.demo.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.com.gfa.cloud.demo.Common.ErrorCode;
import cn.com.gfa.cloud.demo.IO.Response;
import cn.com.gfa.cloud.demo.Model.User;
import cn.com.gfa.cloud.demo.Repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository repository;

    // Whether the user is valid
    private boolean isValidUser(User user) {
        if (!StringUtils.hasText(user.getName())) {
            return false;
        }
        return true;
    }

    // Whether the user id is valid
    private boolean isValidUserId(Integer id) {
        if (id <= 0) {
            return false;
        }
        return true;
    }

    // Whether the user name is valid
    private boolean isValidUserName(String name) {
        if (!StringUtils.hasText(name)) {
            return false;
        }
        return true;
    }

    @PostMapping("/add")
    public Response addUser(@RequestBody User user) {
        Response resp = new Response();

        // Check the user
        if (!isValidUser(user)) {
            resp.setCode(ErrorCode.RequestParamInvalid.getCode());
            resp.setMessage(ErrorCode.RequestParamInvalid.getMessage());
            return resp;
        }

        // Save the user to DB
        User saveUser = repository.save(user);

        // Return response
        resp.setCode(ErrorCode.Success.getCode());
        resp.setMessage(ErrorCode.Success.getMessage());
        resp.setData(saveUser);
        return resp;
    }

    @DeleteMapping("/deletebyid")
    public Response delUserById(@RequestParam(value = "id") Integer id) {
        Response resp = new Response();

        // Check the user id
        if (!isValidUserId(id)) {
            resp.setCode(ErrorCode.RequestParamInvalid.getCode());
            resp.setMessage(ErrorCode.RequestParamInvalid.getMessage());
            return resp;
        }

        // Delete the user from DB by id
        repository.deleteById(id);

        // return response
        resp.setCode(ErrorCode.Success.getCode());
        resp.setMessage(ErrorCode.Success.getMessage());
        return resp;
    }

    @DeleteMapping("/deletebyname")
    public Response delUserByName(@RequestParam(value = "name") String name) {
        Response resp = new Response();

        // Check the user name
        if (!isValidUserName(name)) {
            resp.setCode(ErrorCode.RequestParamInvalid.getCode());
            resp.setMessage(ErrorCode.RequestParamInvalid.getMessage());
            return resp;
        }

        // Find the user by name
        Iterable<User> result = repository.findByName(name);
        List<User> list = new ArrayList<>();
        for (User u : result) {
            list.add(u);

            // Delete the user from DB by id
            repository.deleteById(u.getId());
        }

        if (!list.isEmpty()) {
            resp.setCode(ErrorCode.Success.getCode());
            resp.setMessage(ErrorCode.Success.getMessage());
            // 返回删除记录的数量
            // 注意：这里如果直接返回list列表，请求时会发生异常
            //  似乎是引用的User数据已经被销毁，无法创建返回的JSON数据
            resp.setData(list.size());
        } else {
            resp.setCode(ErrorCode.UserNotExist.getCode());
            resp.setMessage(ErrorCode.UserNotExist.getMessage());
        }

        return resp;
    }

    @GetMapping("/getbyid")
    public Response getUserById(@RequestParam(value = "id") Integer id) {
        Response resp = new Response();

        // Check the user id
        if (!isValidUserId(id)) {
            resp.setCode(ErrorCode.RequestParamInvalid.getCode());
            resp.setMessage(ErrorCode.RequestParamInvalid.getMessage());
            return resp;
        }

        // Find the user from DB by id
        Optional<User> result = repository.findById(id);
        if (result.isPresent()) {
            resp.setCode(ErrorCode.Success.getCode());
            resp.setMessage(ErrorCode.Success.getMessage());
            resp.setData(result.get());
        } else {
            resp.setCode(ErrorCode.UserNotExist.getCode());
            resp.setMessage(ErrorCode.UserNotExist.getMessage());
        }

        return resp;
    }

    @GetMapping("/getbyname")
    public Response getUserByName(@RequestParam(value = "name") String name) {
        Response resp = new Response();

        // Check the user name
        if (!isValidUserName(name)) {
            resp.setCode(ErrorCode.RequestParamInvalid.getCode());
            resp.setMessage(ErrorCode.RequestParamInvalid.getMessage());
            return resp;
        }

        // Find users from DB by name
        Iterable<User> result = repository.findByName(name);
        List<User> list = new ArrayList<>();
        for (User u : result) {
            list.add(u);
        }
        if (!list.isEmpty()) {
            resp.setCode(ErrorCode.Success.getCode());
            resp.setMessage(ErrorCode.Success.getMessage());
            resp.setData(list);
        } else {
            resp.setCode(ErrorCode.UserNotExist.getCode());
            resp.setMessage(ErrorCode.UserNotExist.getMessage());
        }

        return resp;
    }

    @GetMapping("/getAll")
    public Response getAllUsers() {
        Response resp = new Response();

        // Find all users from DB
        Iterable<User> result = repository.findAll();

        List<User> list = new ArrayList<>();
        for (User u : result) {
            list.add(u);
        }

        if (!list.isEmpty()) {
            resp.setCode(ErrorCode.Success.getCode());
            resp.setMessage(ErrorCode.Success.getMessage());
            resp.setData(list);
        } else {
            resp.setCode(ErrorCode.UserNotExist.getCode());
            resp.setMessage(ErrorCode.UserNotExist.getMessage());
        }

        return resp;
    }

    @PutMapping("/update/{id}")
    public Response updateUser(@PathVariable(value = "id") Integer id, @RequestBody User user) {
        Response resp = new Response();

        // Check the user id
        if (!isValidUserId(id)) {
            resp.setCode(ErrorCode.RequestParamInvalid.getCode());
            resp.setMessage(ErrorCode.RequestParamInvalid.getMessage());
            return resp;
        }

        // The user must exist when updating user info
        Optional<User> result = repository.findById(id);
        if (!result.isPresent()) {
            resp.setCode(ErrorCode.UserNotExist.getCode());
            resp.setMessage(ErrorCode.UserNotExist.getMessage());
            return resp;
        }

        // Save the updated user to DB
        User saveUser = repository.save(user);

        // Build successful response
        resp.setCode(ErrorCode.Success.getCode());
        resp.setMessage(ErrorCode.Success.getMessage());
        resp.setData(saveUser);
        return resp;
    }
}
