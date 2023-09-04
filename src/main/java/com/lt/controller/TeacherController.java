package com.lt.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.doadmin.teachers;
import com.lt.doadmin.teaclass;
import com.lt.service.TeachersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    @Autowired
    private TeachersService teachersService;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping
    public Result getTeaAll() {
        List<teachers> teachersList = teachersService.getTeaAll();
        Integer code = teachersList != null ? Code.SAVE_OK : Code.SAVE_ERR;
        String msg = teachersList != null ? "查询成功" : "数据查询失败,请重试!";
        return new Result(code, msg, teachersList);
    }

    @GetMapping("/bytid/{tid}")
    public Result getByTid(@PathVariable Integer tid) {
        teachers teachers = teachersService.getByTid(tid);
        Integer code = teachers != null ? Code.GET_OK : Code.GET_ERR;
        String msg = teachers != null ? "查询成功" : "数据查询失败,请重试!";
        return new Result(code, msg, teachers);
    }

    @GetMapping("/{teanumber}")
    public Result getByTeanumber(@PathVariable String teanumber) {
        teachers tea = teachersService.getByTeanumber(teanumber);
        Integer code = tea != null ? Code.GET_OK : Code.GET_ERR;
        String msg = tea != null ? "查询成功" : "数据查询失败,请重试!";
        return new Result(code, msg, tea);
    }

    @GetMapping("{currentPage}/{pageSize}")
    public Result getPage(@PathVariable int currentPage, @PathVariable int pageSize) {
        IPage<teachers> page = teachersService.getPage(currentPage, pageSize);
        if (currentPage > page.getPages()) {
            page = teachersService.getPage((int) page.getPages(), pageSize);
        }
        System.out.println("总页数:" + page.getPages());
        System.out.println("总记录数:" + page.getTotal());
        List<teachers> teachersList = page.getRecords();
        Integer code = teachersList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = teachersList != null ? "查询成功_分页" : "数据查询失败,请重试!";
        return new Result(code, msg, teachersList);
    }

    @PostMapping("/register")
    public Result register(@RequestBody teachers tea) {
        return teachersService.registe(tea);
    }

    @PostMapping("/login")
    public Result loginCheck(@RequestBody teachers loginUser) {
        String password = loginUser.getTeapassword();
        teachers user = teachersService.loginCheck(loginUser);
        if (user == null) {
            return new Result(Code.LOGIN_ERR, "用户不存在", null);
        }
        if (!passwordEncoder.matches(password, user.getTeapassword())) {
            return new Result(Code.LOGIN_ERR, "密码错误", null);
        }
        if (user.getTeastate() == 1) {
            return new Result(Code.LOGIN_OK, "登录成功", user);
        } else if (user.getTeastate() == 404) {
            return new Result(Code.LOGIN_ERR, "该账号审核未通过，请联系管理员", user);
        } else {
            return new Result(Code.LOGIN_ERR, "该账号尚未被激活，请联系管理员", user);
        }

    }

    @DeleteMapping("/{tid}")//对于删除功能 可能还需要更改——只需要将teastate有设置为0就好了，当然也可以用管理员操作
    public Result delete(@PathVariable Integer tid) {
        int flag = teachersService.deleteById(tid);
        Integer code = flag > 0 ? Code.DELETE_OK : Code.DELETE_ERR;
        String msg = flag > 0 ? "删除成功" : "删除失败";
        return new Result(code, msg, flag);
    }

    @PutMapping
    public Result update(@RequestBody teachers tea) {
        int flag = teachersService.update(tea);
        Integer code = flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag > 0 ? "更改成功" : "更改失败";
        return new Result(code, msg, flag);
    }

    @GetMapping("/selfclass/{tunmber}")
    public Result getAllSelfClass(@PathVariable String tunmber) {
        List<teaclass> teaclassList = teachersService.getAllSelfClass(tunmber);
        Integer code = teaclassList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = teaclassList != null ? "查询成功" : "查询失败";
        return new Result(code, msg, teaclassList);
    }

    @PostMapping("/selfclass")
    public Result insertSelfClass(@RequestBody teaclass teaclass) {
        int flag = teachersService.insertSelfClass(teaclass);
        Integer code = flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag > 0 ? "添加成功" : "添加失败";
        return new Result(code, msg, null);
    }

    @DeleteMapping("/selfclass")
    public Result deleteSelf(@RequestBody teaclass teaclass) {
        int flag = teachersService.deleteSelf(teaclass);
        Integer code = flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag > 0 ? "删除成功" : "删除失败";
        return new Result(code, msg, null);
    }


    /*
    哈希加密方法
     */
    private String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
