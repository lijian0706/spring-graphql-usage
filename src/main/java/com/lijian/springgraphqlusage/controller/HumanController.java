package com.lijian.springgraphqlusage.controller;

import com.lijian.springgraphqlusage.domain.Human;
import com.lijian.springgraphqlusage.service.HumanService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlController;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import java.util.List;

@GraphQlController
public class HumanController {

    private final HumanService humanService;

    public HumanController(HumanService humanService) {
        this.humanService = humanService;
    }

    @QueryMapping
    public List<Human> list(){
        return humanService.list();
    }

    @QueryMapping
    public Human details(@Argument Long id){
        return humanService.details(id);
    }


}
