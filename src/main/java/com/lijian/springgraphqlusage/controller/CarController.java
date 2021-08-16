package com.lijian.springgraphqlusage.controller;

import com.lijian.springgraphqlusage.domain.Car;
import com.lijian.springgraphqlusage.domain.Human;
import com.lijian.springgraphqlusage.service.CarService;
import com.lijian.springgraphqlusage.service.HumanService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlController;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import java.util.List;

@GraphQlController
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    /**
     * 根据车主身份证号查询车辆列表接口
     * @param ownerCardNum
     * @return
     */
    @QueryMapping(value = "carList")
    public List<Car> list(@Argument String ownerCardNum){
        return carService.list(ownerCardNum);
    }
}
