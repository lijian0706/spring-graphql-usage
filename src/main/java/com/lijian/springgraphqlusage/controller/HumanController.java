package com.lijian.springgraphqlusage.controller;

import com.lijian.springgraphqlusage.controller.dto.request.HumanUpdateCmd;
import com.lijian.springgraphqlusage.domain.Human;
import com.lijian.springgraphqlusage.service.HumanService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlController;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import java.util.List;

@GraphQlController
public class HumanController {

    private final HumanService humanService;

    public HumanController(HumanService humanService) {
        this.humanService = humanService;
    }

    /**
     * human列表接口
     * @return
     */
    @QueryMapping
    public List<Human> list(){
        return humanService.list();
    }

    /**
     * 根据id查询human详情
     * @param id
     * @return
     */
    @QueryMapping
    public Human details(@Argument Long id){
        return humanService.details(id);
    }

    /**
     * 修改姓名接口
     * @param cmd
     * @return
     */
    @MutationMapping
    public Human modifyName(@Argument HumanUpdateCmd cmd){
        return humanService.modifyName(cmd.getId(), cmd.getName());
    }
}
