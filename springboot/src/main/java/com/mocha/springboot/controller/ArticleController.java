package com.mocha.springboot.controller;

import com.github.pagehelper.PageInfo;
import com.mocha.springboot.common.ResultCode;
import com.mocha.springboot.entity.Article;
import com.mocha.springboot.service.ArticleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource private ArticleService articleService;

    @PostMapping("/add")
    public ResultCode add(@RequestBody Article article){
        articleService.add(article);
        return ResultCode.success();
    }

    @DeleteMapping("/deleteById/{id}")
    public ResultCode delete(@PathVariable Integer id){
        articleService.deleteById(id);
        return ResultCode.success();
    }

    @DeleteMapping("/deleteBatch")
    public ResultCode deleteBatch(@RequestBody List<Integer> ids){
        articleService.deleteBatch(ids);
        return ResultCode.success();
    }
    @PutMapping("/update")
    public ResultCode update(@RequestBody Article article){
        articleService.update(article);
        return ResultCode.success();
    }


    @GetMapping("/selectAll")
    public ResultCode selectAll(@RequestBody Article article){
        List<Article> articles = articleService.selectAll(article);
        return ResultCode.success(articles);
    }

    @GetMapping("/selectPage")
    public ResultCode selectPage(Article article,
                                 @RequestParam (defaultValue = "1") Integer pageNum,
                                 @RequestParam (defaultValue = "5") Integer pageSize){
        PageInfo<Article> articlePageInfo = articleService.selectPage(article,pageNum, pageSize);
        return ResultCode.success(articlePageInfo);
    }

}
