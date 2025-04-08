package com.mt365.sbootfds.mapper;



import com.mt365.sbootfds.entity.Stock;

import java.util.List;

public interface StockMapper {

    //    @Select("select * from admin where username = #{username}")
    Stock selectByname(String name);

    void insert(Stock stock);

    void deleteById(String ts_code);

    void updateById(Stock stock);

    Stock selectById(String  ts_code);

    List<Stock> selectAll(String name);

    List<Stock> selectAlll();
}

