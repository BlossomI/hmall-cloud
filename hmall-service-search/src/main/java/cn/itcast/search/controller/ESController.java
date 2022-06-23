package cn.itcast.search.controller;

import cn.itcast.hmall.dto.common.PageDTO;
import cn.itcast.hmall.dto.common.ResultDTO;
import cn.itcast.hmall.dto.search.SearchReqDTO;
import cn.itcast.hmall.pojo.item.ItemDoc;
import cn.itcast.search.service.ESService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class ESController {

    @Resource
    private ESService esService;

    @GetMapping("/importItemData")
    public ResultDTO import2ES() {
        return esService.import2ES();
    }

    /**
     * 搜索建议
     *
     * @param key 用户输入的关键字
     * @return 建议字符串列表
     */
    @GetMapping("/suggestion")
    public List<String> getSuggestions(@RequestParam String key) {
        return esService.getSuggestions(key);
    }

    /**
     * 聚合条件展示
     *
     * @param searchReqDTO 请求参数
     * @return Map集合
     */
    @PostMapping("/filters")
    public Map<String, List<String>> filterSearchResults(@RequestBody SearchReqDTO searchReqDTO) {
        return esService.getFilterSearchResults(searchReqDTO);
    }

    @PostMapping("/list")
    public PageDTO<ItemDoc> basicQueryPage(@RequestBody SearchReqDTO searchReqDTO){
        return esService.basicQuery(searchReqDTO);
    }
}
