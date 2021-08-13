package com.lijian.springgraphqlusage.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {
    @Id
    private Long id;
    private String plateNum; // 车牌号
    private String ownerCardNum; // 车主身份证号
}
