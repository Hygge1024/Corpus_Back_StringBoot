package com.lt.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.doadmin.Classes;
import com.lt.service.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//该接口被弃用
@RestController
@RequestMapping("/api/class")
public class ClassController {
    @Autowired
    private ClassesService classesService;
//
//    @GetMapping("/{currentPage}/{pageSize}")
//    public Result getAllClass(@PathVariable int currentPage, @PathVariable int pageSize) {
//        IPage<Classes> page = classesService.getAllClass(currentPage, pageSize);
//        if (currentPage > page.getPages()) {
//            page = classesService.getAllClass((int) page.getPages(), pageSize);
//        }
//        List<Classes> classesList = page.getRecords();
//        Integer code = classesList != null ? Code.GET_OK : Code.GET_ERR;
//        String msg = classesList != null ? "查询班级信息成功" : "查询失败";
//        return new Result(code, msg, classesList);
//    }
//
//    @PostMapping
//    public Result addClasses(@RequestBody Classes classes) {
//        int flag = classesService.addClss(classes);
//        Integer code = flag != 0 ? Code.SAVE_OK : Code.SAVE_ERR;
//        String msg = flag != 0 ? "添加成功" : "添加失败，该班级可能已存在";
//        return new Result(code, msg, null);
//    }
//
//    @DeleteMapping("/{cname}")
//    public Result deleteByName(@PathVariable String cname) {
//        int flage = classesService.deleteByName(cname);
//        Integer code = flage != 0 ? Code.DELETE_OK : Code.DELETE_ERR;
//        String msg = flage != 0 ? "删除成功" : "删除失败";
//        return new Result(code, msg, null);
//    }
//
//    public static class updateClass {
//        private String oldName;
//        private String newName;
//
//        public String getOldName() {
//            return oldName;
//        }
//
//        public void setOldName(String oldName) {
//            this.oldName = oldName;
//        }
//
//        public String getNewName() {
//            return newName;
//        }
//
//        public void setNewName(String newName) {
//            this.newName = newName;
//        }
//    }
//
//    @PutMapping()
//    public Result update(@RequestBody updateClass uc) {
//        String oldName = uc.getOldName();
//        String newName = uc.getNewName();
//        int flag = classesService.update(oldName, newName);
//        Integer code = flag != 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
//        String msg = flag != 0 ? "修改班级信息成功" : "修改失败，可能新班级已存在";
//        return new Result(code, msg, null);
//    }


}
