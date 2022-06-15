package com.example.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mybatisplus.mapper.UserMapper;
import com.example.mybatisplus.pojo.User;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


@SpringBootTest
class MyBatisPlusApplicationTests {

    @Autowired
    UserMapper userMapper;

    // 查询全部数据
    @Test
    void contextLoads1() {
        System.out.println("-------selectAll method test 测试查询所有用户方法------");
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
        System.out.println(userList);
    }

    // 根据id查询一条数据
    @Test
    void test () {
        User user = userMapper.selectById(1);
        System.out.println(user);

    }

    // 根据id批量查询
    @Test
    void test2() {
        List<User> list = userMapper.selectBatchIds(Arrays.asList(1,2,3,4));
        list.forEach(System.out::println);
        System.out.println(list);
    }

    // 通过map封装查询条件
    @Test
    void test3() {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("name","Jone");
        hashMap.put("age",18);
        List<User> users = userMapper.selectByMap(hashMap);
        users.forEach(System.out::println);
        System.out.println(users);
    }

    // 查询数据记录
    @Test
    void test4() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.in("id",Arrays.asList(1,2,3,4));
        Integer integer = Math.toIntExact(userMapper.selectCount(wrapper));
        System.out.println(integer);
    }

    // 根据条件查询，只返回一条数据，返回多条会报错
    @Test
    void test5() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id",1);
        userMapper.selectOne(wrapper);
    }

    @Test
    void selectPage() {
        // 复杂查询
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        // 配置分页
        Page<User> page = new Page<>(1,3);

        // 查询
        userMapper.selectPage(page,wrapper);
        System.out.println(page);
    }

    // 测试添加操作
    @Test
    void testInsert() {
        User user = new User();
        user.setName("小谷");
        user.setAge(18);
        user.setEmail("519347732@qq.com");
        //  没有设置id却自动生成id
        userMapper.insert(user);
    }

    // 更新测试
    @Test
    void testUpdateById() {
        User user = userMapper.selectById(13l);
        user.setName("小");
        user.setAge(18);
        userMapper.updateById(user);
    }

    // 更新测试
    @Test
    void testUpdate() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<User>();
        lambdaQueryWrapper.eq(StringUtils.isNotBlank("name"),User::getName,"123");

        User user = userMapper.selectById(1534434661004443650l);
        user.setAge(18);
        user.setEmail("1344479418@qq.com");

        userMapper.update(user,lambdaQueryWrapper);
        System.out.println(lambdaQueryWrapper);
        System.out.println(user);

    }

    @Test
    void testUpdate1() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name","123");
        User user = userMapper.selectById(1534434661004443650l);
        user.setAge(20);
        user.setEmail("519347732@qq.com");
        userMapper.update(user,queryWrapper);
    }

    // 乐观锁测试
    @Test
    void testOptimisticLocker() {
        // 1. 查询用户信息
        User user = userMapper.selectById(1534343090133188610L);
        // 2. 修改用户信息
        user.setEmail("519347732@qq.com");
        user.setName("杰");
        // 3. 更新操作
        userMapper.updateById(user);

    }

    // 模拟多线程下乐观锁失败案例
    @Test
    void testOptimisticLocker2() {
        // 模拟多线程
        User user = userMapper.selectById(1534343090133188610L);
        user.setName("小张");
        // 在这里对线程1进行修改
        user.setEmail("123@qq.com");


        // 线程2插队
        User user2 = userMapper.selectById(1534343090133188610L);
        user.setName("小谷");
        user.setEmail("123@qq.com");
        // 线程2抢先提交
        userMapper.updateById(user2);
        // 线程1失败，乐观锁在这种情况下防止了脏数据存在，没有乐观锁就会有覆盖掉
        userMapper.updateById(user);
    }

    // 测试分页查询
    @Test
    void testPage() {
        // 开启拦截器后，会注册一个page对象，当前页，每页条数
        Page<User> page = new Page<>(1,5);
        // 模糊查询，条件语句
        QueryWrapper queryWrapper = new QueryWrapper();
        // 模糊查询%小%
        queryWrapper.like("name","小");
        // 分页查询
        userMapper.selectPage(page,queryWrapper);
        // 获取分页后的数据 打印
        page.getRecords().forEach(System.out::println);
        // 获取记录总数
        System.out.println(page.getTotal());
    }

    // 添加测试
    @Test
    void testInsert2() {
        User user = new User();
        user.setName("123");
        user.setAge(18);
        user.setEmail("123@qq.com");

        userMapper.insert(user);
        System.out.println(user);
    }

    // 添加测试
    @Test
    void testInsert3() {
        User user = new User();
        user.setName("咚咚");
        user.setAge(12);
        user.setEmail("123123@qq.com");
        user.setVersion(1);

        userMapper.insert(user);
        System.out.println(user);
    }

    // 添加测试
    @Test
    void testInsert4() {
        User user = new User();
        user.setId(13L);
        user.setName("钉钉");
        user.setAge(18);
        user.setEmail("123@qq.com");

        userMapper.insert(user);
        System.out.println(user);
    }

    // 删除测试,根据id进行删除操作
    @Test
    void testDeleteById() {
        User user = new User();
        user.setId(1534436199085060097L);
        userMapper.deleteById(user);
    }

    // 简化版
    @Test
    void testDeleteById2() {
        userMapper.deleteById(1534435421364588545L);
        // 全部删除
userMapper.delete(null);

    }

    // 根据条件进行删除,将同一个名字的都删除
    @Test
    void testDelete() {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("name","钉钉");
        userMapper.delete(queryWrapper);
    }

    // 批量删除
    @Test
    void testDeleteBatchIds() {
        List list = new ArrayList();
        list.add(10L);
        list.add(9L);

        userMapper.deleteBatchIds(list);
        System.out.println(list);
    }

    // 根据map条件删除记录
    @Test
    void testDeleteMap() {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("id",8l);
        hashMap.put("name","钉钉");
        userMapper.deleteByMap(hashMap);
        System.out.println(hashMap);
    }

    // 逻辑删除
    @Test
    void testDelete2() {
        userMapper.deleteById(7L);
    }

    // 条件构造器
    @Test
    void contextLoads2() {
        // 查询多个
        // 查询一个复杂的，比如查询用户name,邮件不为空，年龄大于20的用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // 添加查询条件
        wrapper.isNotNull("name")
                .eq("email","519347732@qq.com")
                .isNotNull("email")
                .ge("age",20);
        userMapper.selectList(wrapper).forEach(System.out::println);

        User user = userMapper.selectOne(wrapper);
        System.out.println(user);
    }

    // 区间查询与计数,between(),selectCount()
    @Test
    void test6() {
        // 查询区间记录
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.between("age",20,30);
        Long count = userMapper.selectCount(wrapper);
        System.out.println(count);
    }

    @Test
    void test7() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.between(User::getAge,20,30);
        long count = userMapper.selectCount(lambdaQueryWrapper);
        System.out.println(count);
    }

    // 模糊查询
    @Test
    void test8() {
        // 模糊查询
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.like(ObjectUtils.isNotEmpty("name"),User::getName,"99")
                    .notLike(ObjectUtils.isNotEmpty("name"),User::getName,"6")
                    .likeRight(ObjectUtils.isNotEmpty("email"),User::getEmail,"9")
                    .likeLeft(ObjectUtils.isNotEmpty("email"),User::getEmail,"m");
            // 根据wapper条件查询语句
            userMapper.selectMaps(lambdaQueryWrapper);
    }

    // 子查询 (多表联查)
    @Test
    void test9() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.inSql(ObjectUtils.isNotEmpty("id"),User::getId,"select id from user where id < 7");
        userMapper.selectObjs(lambdaQueryWrapper).forEach(System.out::println);
    }

    @Test
    void test10() {
        // 子查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("id","select id from user where id < 13");
        userMapper.selectObjs(queryWrapper).forEach(System.out::println);
    }

    // 排序
    @Test
    void test11() {
        // 排序
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(ObjectUtils.isNotEmpty("id"),User::getId);
        userMapper.selectList(lambdaQueryWrapper).forEach(System.out::println);
    }

    @Test
    void test12() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderBy(ObjectUtils.isNotEmpty("id"),false,User::getId);
        userMapper.selectList(lambdaQueryWrapper).forEach(System.out::println);
    }

    // 分组，条件
    @Test
    void test13() {
        // 分组排序
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.groupBy(ObjectUtils.isNotEmpty("id"),User::getId)
            .having(ObjectUtils.isNotEmpty("id"),"id = 1");
        userMapper.selectList(lambdaQueryWrapper).forEach(System.out::println);
    }

    @Test
    void test14() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("id").having("id = 5");
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    @Test
    void test15() {
        //分组排序
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.groupBy("id").having("id = 1");
        userMapper.selectList(wrapper).forEach(System.out::println);
    }

    @Test
    void test16() {

    }
}
