package com.lt.controller;

import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.domain.Tag;
import com.lt.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagsController {
    @Autowired
    private TagService tagService;

    @GetMapping
    public Result getAllTags() {
        List<Tag> tagList = tagService.findAll();
        Integer code = tagList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = tagList != null ? "查询成功" : "数据查询失败,请重试";
        return new Result(code, msg, tagList);
    }

    @PostMapping
    public Result create(@RequestBody Tag tag) {
        int flag = tagService.create(tag);
        Integer code = flag != 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag != 0 ? "添加成功" : "添加失败";
        return new Result(code, msg, null);
    }

    @DeleteMapping("/{tid}")
    public Result delete(@PathVariable int tid) {
        int flag = tagService.delete(tid);
        Integer code = flag != 0 ? Code.DELETE_OK : Code.DELETE_ERR;
        String msg = flag != 0 ? "删除成功" : "删除失败";
        return new Result(code, msg, null);
    }

    @PutMapping
    public Result update(@RequestBody Tag tag) {
        int flag = tagService.update(tag);
        Integer code = flag != 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag != 0 ? "更新成功" : "更新失败";
        return new Result(code, msg, null);
    }
}
