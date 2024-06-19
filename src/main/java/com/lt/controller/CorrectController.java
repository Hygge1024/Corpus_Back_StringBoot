package com.lt.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.domain.CorpusDao;
import com.lt.domain.Correct;
import com.lt.domain.CorrectDao;
import com.lt.service.CorrectService;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/correct")
@CrossOrigin
@Slf4j
public class CorrectController {
    @Autowired
    private CorrectService correctService;
    @PostMapping("/")
    public Result upload(@RequestParam("TemplateFile") MultipartFile[] TemplateFiles, @RequestParam("BatchExerciseFiles") MultipartFile[] BatchExerciseFiles,@ModelAttribute CorrectDao correctDao) throws IOException, InterruptedException {
        int flag = correctService.createCorrect(TemplateFiles,BatchExerciseFiles,correctDao);
        Integer code = flag != 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag != 0 ? "上传成功" : "上传失败!";
        return new Result(code, msg, flag);

    }
    @GetMapping("/all/{currentPage}/{pageSize}")
    public Result getAll(@PathVariable int currentPage, @PathVariable int pageSize){
        IPage page = correctService.getPage(currentPage,pageSize);
        if(currentPage > page.getPages()){
            page = correctService.getPage((int)page.getPages(),pageSize);
        }
        List<Correct> correctList = page.getRecords();
        Map<String,Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("list",correctList);
        Integer code = correctList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = correctList != null ? "查询成功":"查询失败";
        return new Result(code,msg,data);
    }
    @DeleteMapping("/delete/{correct_id}")
    public Result delete(@PathVariable int correct_id){
        int flag = correctService.delete(correct_id);
        Integer code = flag != 0 ? Code.DELETE_OK : Code.DELETE_ERR;
        String msg = flag != 0 ? "删除成功":"删除失败";
        return new Result(code,msg,flag);
    }

}
