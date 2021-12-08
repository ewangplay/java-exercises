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
import cn.com.gfa.cloud.demo.IO.UserRequest;
import cn.com.gfa.cloud.demo.Model.User;
import cn.com.gfa.cloud.demo.Repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository repository;

    @PostMapping("/add")
    public Response addUser(@RequestBody UserRequest req) {
        Response resp = new Response();

        // Check the params
        if (!StringUtils.hasText(req.getName())) {
            resp.setCode(ErrorCode.RequestParamInvalid.getCode());
            resp.setMessage(ErrorCode.RequestParamInvalid.getMessage());
            return resp;
        }

        // Save user to DB
        User user = new User();
        user.setName(req.getName());
        User saveUser = repository.save(user);

        // Build successful response
        resp.setCode(ErrorCode.Success.getCode());
        resp.setMessage(ErrorCode.Success.getMessage());
        resp.setData(saveUser);
        return resp;
    }

    @DeleteMapping("/deletebyid/{id}")
    public Response delUserById(@PathVariable(value = "id") Integer id) {
        Response resp = new Response();

        // Check the params
        if (id <= 0) {
            resp.setCode(ErrorCode.RequestParamInvalid.getCode());
            resp.setMessage(ErrorCode.RequestParamInvalid.getMessage());
            return resp;
        }

        // Delete user from DB
        repository.deleteById(id);

        // Build successful response
        resp.setCode(ErrorCode.Success.getCode());
        resp.setMessage(ErrorCode.Success.getMessage());
        return resp;
    }

    @DeleteMapping("/deletebyname")
    public Response delUserByName(@RequestParam(value = "name") String name) {
        Response resp = new Response();

        // Check the params
        if (!StringUtils.hasText(name)) {
            resp.setCode(ErrorCode.RequestParamInvalid.getCode());
            resp.setMessage(ErrorCode.RequestParamInvalid.getMessage());
            return resp;
        }

        // Find the user by name
        Optional<User> result = repository.findByName(name);
        if (result.isPresent()) {
            // Delete user from DB
            repository.deleteById(result.get().getId());

            // Build successful response
            resp.setCode(ErrorCode.Success.getCode());
            resp.setMessage(ErrorCode.Success.getMessage());

        } else {
            resp.setCode(ErrorCode.UserNotExist.getCode());
            resp.setMessage(ErrorCode.UserNotExist.getMessage());
        }
        return resp;
    }

    @GetMapping("/getbyid/{id}")
    public Response getUserById(@PathVariable(value = "id") Integer id) {
        Response resp = new Response();

        // Check the params
        if (id <= 0) {
            resp.setCode(ErrorCode.RequestParamInvalid.getCode());
            resp.setMessage(ErrorCode.RequestParamInvalid.getMessage());
            return resp;
        }

        // Find user from DB
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

        // Check the params
        if (!StringUtils.hasText(name)) {
            resp.setCode(ErrorCode.RequestParamInvalid.getCode());
            resp.setMessage(ErrorCode.RequestParamInvalid.getMessage());
            return resp;
        }

        // Find user from DB
        Optional<User> result = repository.findByName(name);
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
    public Response updateUser(@PathVariable(value = "id") Integer id, @RequestBody UserRequest req) {
        Response resp = new Response();

        // Check the params
        if (id <= 0) {
            resp.setCode(ErrorCode.RequestParamInvalid.getCode());
            resp.setMessage(ErrorCode.RequestParamInvalid.getMessage());
            return resp;
        }

        // Save user to DB
        User user = new User();
        user.setName(req.getName());
        User saveUser = repository.save(user);

        // Build successful response
        resp.setCode(ErrorCode.Success.getCode());
        resp.setMessage(ErrorCode.Success.getMessage());
        resp.setData(saveUser);
        return resp;
    }
}
