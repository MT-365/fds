package com.mt365.sbootfds.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mt365.sbootfds.entity.Stock;
import com.mt365.sbootfds.mapper.StockMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class StockService {
    @Resource
    StockMapper stockMapper;

    public void add(Stock stock) {

        stockMapper.insert(stock);
    }




    public void deleteById(String ts_code) {
        stockMapper.deleteById(ts_code);
    }

    public void deleteBatch(List<String> ts_codes) {
        for (String ts_code : ts_codes) {
            this.deleteById(ts_code);
        }
    }

    public void updateById(Stock stock) {
        stockMapper.updateById(stock);
    }

    public Stock findById(String ts_code) {
        return stockMapper.selectById(ts_code);
    }


    public List<Stock> selectAlll() {
        return stockMapper.selectAlll();
    }
    public List<Stock> findAll(String name) {
        return stockMapper.selectAll(name);
    }

    public PageInfo selectPage(Integer pageNum, Integer pageSize, String name) {
        PageHelper.startPage(pageNum,pageSize); // 分页查询
        List<Stock> list = this.findAll(name); // 查询所有
        return PageInfo.of(list);  // 返回分页信息
    }
}
