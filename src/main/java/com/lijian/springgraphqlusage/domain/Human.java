package com.lijian.springgraphqlusage.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Human {
    @Id
    private Long id;
    private String cardNum; // 身份证号
    private String name;
    private Integer age;
    @Embedded
    private Address address;

    @Embeddable
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Address {
        private String province;
        private String city;
        private String addr;
    }

    public void modifyName(String name) {
        this.name = name;
    }
}
