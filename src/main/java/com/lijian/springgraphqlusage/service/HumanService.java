package com.lijian.springgraphqlusage.service;

import com.lijian.springgraphqlusage.domain.Human;
import com.lijian.springgraphqlusage.repository.HumanRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HumanService {

    private final HumanRepository humanRepository;

    public HumanService(HumanRepository humanRepository) {
        this.humanRepository = humanRepository;
    }

    public void init() {
        List<Human> humans = new ArrayList<>();
        Human zhangsan = Human.builder()
                .id(1L)
                .name("张三")
                .age(20)
                .cardNum("1")
                .address(Human.Address.builder().province("安徽省").city("合肥市").addr("xx小区").build())
                .build();
        Human lisi = Human.builder()
                .id(2L)
                .name("李四")
                .age(21)
                .cardNum("2")
                .address(Human.Address.builder().province("安徽省").city("安庆市").addr("yy小区").build())
                .build();
        humans.add(zhangsan);
        humans.add(lisi);
        humanRepository.saveAll(humans);
    }

    public Human details(Long id){
        return humanRepository.findById(id).orElseThrow();
    }


    public List<Human> list() {
        return humanRepository.findAll();
    }

    public Human modifyName(Long id, String name) {
        Human human = humanRepository.findById(id).orElseThrow();
        human.modifyName(name);
        return humanRepository.save(human);
    }
}
