package com.zheng.travel.client.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {

    YES(1),NO(0);

    private Integer value;
    StatusEnum(Integer value){
        this.value = value;
    }



}
