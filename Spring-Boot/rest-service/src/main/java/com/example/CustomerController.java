package com.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    @Autowired
    JdbcTemplate jdbcTemplate; 

    @PostMapping("/customer/addCustomer")
    public String addCustomer(@RequestBody Customer customer) {
        if (customer.getFirstName() == "") {
            return "FAILURE: first name missing";
        }
        if (customer.getLastName() == "") {
            return "FAILURE: last name missing";
        }
        jdbcTemplate.update("INSERT INTO customers(first_name, last_name) VALUES (?,?)", customer.getFirstName(), customer.getLastName());
        return "SUCCESS: "  + customer;
    }
    
    @GetMapping("/customer/getAllCustomers")
    public List<Customer> getAllCustomers() {
        String sql = "SELECT id, first_name, last_name FROM customers";
        List<Customer> customers = jdbcTemplate.query(sql,
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name")));

        return customers;
    }
}