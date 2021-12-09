package cn.com.gfa.cloud.demo.Model;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class Address {
    private String province;
    private String city;
    private String area;
    private String address;
    private String zipcode;
}
