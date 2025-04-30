package com.mocha.springboot.controller;

import com.github.pagehelper.PageInfo;
import com.mocha.springboot.common.Result;
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
    public Result add(@RequestBody Article article){
        articleService.add(article);
        return Result.success();
    }

    @DeleteMapping("/deleteById/{id}")
    public Result delete(@PathVariable Integer id){
        articleService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        articleService.deleteBatch(ids);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody Article article){
        articleService.update(article);
        return Result.success();
    }


    @GetMapping("/selectAll")
    public Result selectAll(@RequestBody Article article){
        List<Article> articles = articleService.selectAll(article);
        return Result.success(articles);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Article article,
                             @RequestParam (defaultValue = "1") Integer pageNum,
                             @RequestParam (defaultValue = "5") Integer pageSize){
        PageInfo<Article> articlePageInfo = articleService.selectPage(article,pageNum, pageSize);
        return Result.success(articlePageInfo);
    }

}
