package com.lt.controller;

import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.domain.Mark;
import com.lt.domain.MarkUpDao;
import com.lt.service.MarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mark")
@CrossOrigin
public class MarkController {
    @Autowired
    private MarkService markService;

    @GetMapping("/eid={eid}/stunumber={stunumber}")
    public Result getAllBySelf(@PathVariable int eid, @PathVariable String stunumber) {
        List<Mark> markList = markService.getAll(eid, stunumber);
        Integer code = markList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = markList != null ? "查询成功" : "数据查询失败,请重试!";
        return new Result(code, msg, markList);
    }

    @PostMapping("/addMark")
    public Result addMark(@RequestBody Mark mark) {
        int flag = markService.addMark(mark);
        Integer code = flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag > 0 ? "添加成功" : "添加失败，请联系管理员";
        return new Result(code, msg, flag);
    }

    @PostMapping("/update")
    public Result updateMark(@RequestBody MarkUpDao markUpDao) {
        int flag = markService.updateMark(markUpDao);
        Integer code = flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag > 0 ? "更新成功" : "更新失败，请联系管理员";
        return new Result(code, msg, flag);
    }

    @DeleteMapping("/mid={mid}")
    public Result deleteMark(@PathVariable int mid) {
        int flag = markService.deleteMark(mid);
        Integer code = flag > 0 ? Code.DELETE_OK : Code.DELETE_ERR;
        String msg = flag > 0 ? "删除成功" : "删除失败，请联系管理员";
        return new Result(code, msg, flag);
    }
}
