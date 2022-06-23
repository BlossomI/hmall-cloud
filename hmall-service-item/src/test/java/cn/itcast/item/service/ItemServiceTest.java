package cn.itcast.item.service;


import cn.itcast.hmall.pojo.item.Item;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ItemServiceTest {

    @Resource
    private ItemService itemService;

    @Test
    void testItemService(){
        LambdaQueryWrapper<Item> wrapper = Wrappers.lambdaQuery(Item.class);
        IPage<Item> page = new Page<>(2,5);
//        itemService.page()
        itemService.page(page,wrapper);

        System.out.println(page.getTotal());
        System.out.println(page.getRecords());
    }

}