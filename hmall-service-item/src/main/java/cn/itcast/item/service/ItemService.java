package cn.itcast.item.service;

import cn.itcast.hmall.dto.common.PageDTO;
import cn.itcast.hmall.dto.common.ResultDTO;
import cn.itcast.hmall.pojo.item.Item;
import cn.itcast.hmall.pojo.req.PageReq;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

public interface ItemService extends IService<Item> {

    PageDTO<Item> pageQuery(PageReq pageReq);

    ResultDTO updateStatus(Integer id, Integer status);
}
