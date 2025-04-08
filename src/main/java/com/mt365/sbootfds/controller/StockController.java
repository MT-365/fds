package com.mt365.sbootfds.controller;

import com.github.pagehelper.PageInfo;
import com.mt365.sbootfds.common.Result;
import com.mt365.sbootfds.entity.Stock;
import com.mt365.sbootfds.service.StockService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockController {
    @Resource
    StockService stockService;


    @PostMapping("/add")
    public Result add(@RequestBody Stock stock){
        stockService.add(stock);
        return Result.success();
    }
    /*
     *删除管理员
     */
    @DeleteMapping("/delete/{ts_code}")
    public Result delete(@PathVariable String ts_code){
        stockService.deleteById(ts_code);
        return Result.success();
    }

    /*
     *批量删除管理员
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<String> ts_codes){
        stockService.deleteBatch(ts_codes);
        return Result.success();
    }
    /*
     *更新管理员
     */
    @PutMapping("/update")
    public Result update(@RequestBody Stock stock){
        stockService.updateById(stock);
        return Result.success();
    }
    /*
     *通过id查询管理员
     */
    @GetMapping("selectById/{ts_code}")
    public Result findById(@PathVariable String ts_code){
        Stock stock = stockService.findById(ts_code);
        return Result.success(stock);
    }

    /*
     *查询所有管理员
     */
    @GetMapping("/selectAll")
    public Result findAll(String name){
        List<Stock> list = stockService.findAll(name);
        return Result.success(list);
    }

//    查询所有数据
    @GetMapping("/selectAlll")
    public Result selectAlll(){
        List<Stock> list = stockService.selectAlll();
        return Result.success(list);
    }

    /*
    分页查询所有管理员
     */
    @GetMapping("/selectPage")
    public Result selectPage(
            String name,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "8") Integer pageSize){
        PageInfo pageInfo = stockService.selectPage(pageNum,pageSize,name);
        return Result.success(pageInfo);
    }
}
