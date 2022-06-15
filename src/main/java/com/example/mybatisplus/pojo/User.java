package com.example.mybatisplus.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {


    public static String get;
    // 枚举注解，使用ID_WORKER策略，全局唯一ID，数据库设置自增也没有用
    @TableId(type = IdType.ASSIGN_ID,value = "id")
    private Long id;
    private String name;
    private Integer age;
    private  String email;

    // 乐观锁注解
    @Version
    private Integer version;

    // 逻辑删除
    @TableLogic
    private Integer deleted;
}
