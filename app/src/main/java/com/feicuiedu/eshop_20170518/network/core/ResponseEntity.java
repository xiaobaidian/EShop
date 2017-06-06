package com.feicuiedu.eshop_20170518.network.core;

import com.feicuiedu.eshop_20170518.network.entity.Status;
import com.google.gson.annotations.SerializedName;

/**
 * Created by 蔡传飞 on 2017-05-26.
 */
// 响应体的实体基类：为了防止直接实例化
public abstract class ResponseEntity {

    // 每一个响应的实体类都有Status，提取到基类中
    @SerializedName("status")
    private Status mStatus;

    public Status getStatus() {
        return mStatus;
    }
}
