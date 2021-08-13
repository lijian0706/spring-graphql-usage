package com.lijian.springgraphqlusage.service;

import com.lijian.springgraphqlusage.domain.Car;
import com.lijian.springgraphqlusage.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public void init(){
        List<Car> cars = new ArrayList<>();
        Car zhangsanCar = Car.builder().id(1L).ownerCardNum("1").plateNum("皖A1111").build();
        Car lisiCar = Car.builder().id(2L).ownerCardNum("2").plateNum("皖A1112").build();
        cars.add(zhangsanCar);
        cars.add(lisiCar);
        carRepository.saveAll(cars);
    }

    public List<Car> list(String ownerCardNum) {
        return carRepository.findByOwnerCardNum(ownerCardNum);
    }


}
