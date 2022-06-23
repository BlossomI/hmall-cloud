package cn.itcast.search.service.impl;

import cn.itcast.search.service.ESService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ESServiceImplTest {

    @Autowired
    private ESService esService;

    @Test
    void import2ES() {
        esService.import2ES();
    }
}