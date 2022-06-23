package cn.itcast.item.service.impl;

import cn.itcast.hmall.constants.MqConstants;
import cn.itcast.hmall.dto.common.PageDTO;
import cn.itcast.hmall.dto.common.ResultDTO;
import cn.itcast.hmall.pojo.item.Item;
import cn.itcast.hmall.pojo.req.PageReq;
import cn.itcast.item.mapper.ItemMapper;
import cn.itcast.item.service.ItemService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {

    @Resource
    private ItemMapper itemMapper;

    @Resource
    private RabbitTemplate rabbit;

    public PageDTO<Item> pageQuery(PageReq pageReq) {

        // 创建wrapper对象
        LambdaQueryWrapper<Item> wrapper = Wrappers.lambdaQuery();

        // 设置查询条件
        queryParams(pageReq, wrapper);

        // 设置分页参数
        IPage<Item> page = new Page<>(pageReq.getPage(), pageReq.getSize());

        // 查询数据
        itemMapper.selectPage(page, wrapper);

        // 封装结果
        PageDTO<Item> pageDTO = new PageDTO<>();
        pageDTO.setTotal(page.getTotal());
        pageDTO.setList(page.getRecords());

        return pageDTO;
    }


    /**
     * 改变商品上下架状态
     *
     * @param id     商品id
     * @param status 目标状态id
     * @return 通用返回对象
     */
    @Override
    public ResultDTO updateStatus(Integer id, Integer status) {
        // 通过id查询所有信息，封装成对象
        Item item = itemMapper.selectById(id);

        // 将status重新设置
        item.setStatus(status);

        // 更新数据信息
        int i = itemMapper.updateById(item);

        if (status == 1) {
            rabbit.convertAndSend(MqConstants.ITEM_EXCHANGE, MqConstants.ITEM_INSERT_KEY, id);
        } else {
            rabbit.convertAndSend(MqConstants.ITEM_EXCHANGE, MqConstants.ITEM_DELETE_KEY, id);
        }

        return i == 1 ? ResultDTO.ok() : ResultDTO.error();
    }

    private void queryParams(@NotNull PageReq pageReq, LambdaQueryWrapper<Item> wrapper) {
        // ItemName
        if (StringUtils.isNotBlank(pageReq.getName())) {
            wrapper.eq(Item::getName, pageReq.getName());
        }

        // brand
        if (StringUtils.isNotBlank(pageReq.getBrand())) {
            wrapper.eq(Item::getBrand, pageReq.getBrand());
        }

        // category
        if (StringUtils.isNotBlank(pageReq.getCategory())) {
            wrapper.eq(Item::getCategory, pageReq.getCategory());
        }

        // time range
        if (pageReq.getBeginDate() != null && pageReq.getEndDate() != null) {
            //设置创建时间范围条件;
            wrapper.between(
                    // createTim，between beginDate and endDate
                    Item::getCreateTime,
                    pageReq.getBeginDate(),
                    pageReq.getEndDate());
        }
    }

}
