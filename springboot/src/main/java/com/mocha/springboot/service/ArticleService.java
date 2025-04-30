package com.mocha.springboot.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mocha.springboot.entity.Article;
import com.mocha.springboot.mapper.ArticleMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service//创建 Service 并且标注为 Springboot 里面的一个 bean
public class ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    //增
    public void add(Article article) {
        article.setTime(DateUtil.now()); //获取当前最新的时间，字符串
        articleMapper.insert(article);
    }

    //删
    public void deleteById(Integer id) {
        articleMapper.deleteById(id);
    }

    //批量删除
    public void deleteBatch(List<Integer> ids) {
        for(Integer id: ids) {
            this.deleteById(id);
        }
    }

    //改
    public void update(Article article) {
        articleMapper.updateById(article);
    }

    public List<Article> selectAll(Article article) {
        return articleMapper.selectAll(article);
    }

    public Article selectById(Integer id) {
        return articleMapper.selectById(id);
    }

    public PageInfo<Article> selectPage(Article article,Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Article> list = articleMapper.selectAll(article);
        return PageInfo.of(list);
    }

}
