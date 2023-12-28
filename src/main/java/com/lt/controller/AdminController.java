package com.lt.controller;

import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.domain.admins;
import com.lt.service.AdminsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@CrossOrigin
public class AdminController {
    @Autowired
    private AdminsService adminsService;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping
    public Result getAdmAll() {
        List<admins> adminsList = adminsService.getAdmAll();
        Integer code = adminsList != null ? Code.SAVE_OK : Code.SAVE_ERR;
        String msg = adminsList != null ? "查询成功" : "数据查询失败,请重试!";
        return new Result(code, msg, adminsList);
    }

    @GetMapping("/byaid/{aid}")
    public Result getByAid(@PathVariable Integer aid) {
        admins admins = adminsService.getByAid(aid);
        Integer code = admins != null ? Code.GET_OK : Code.GET_ERR;
        String msg = admins != null ? "查询成功" : "数据查询失败,请重试!";
        return new Result(code, msg, admins);
    }

    @GetMapping("/{anumber}")
    public Result getByAnumber(@PathVariable String anumber) {
        admins adm = adminsService.getByAdmnumber(anumber);
        Integer code = adm != null ? Code.GET_OK : Code.GET_ERR;
        String msg = adm != null ? "查询成功" : "数据查询失败,请重试";
        return new Result(code, msg, adm);
    }

    @PostMapping("/register")
    public Result register(@RequestBody admins adm) {
        return adminsService.registe(adm);
    }

    @PostMapping("/login")
    public Result loginCheck(@RequestBody admins loginUser) {
        String password = loginUser.getAdmpassword();
        admins user = adminsService.loginCheck(loginUser);
        if (null == user) {
            return new Result(Code.LOGIN_ERR, "用户不存在", null);
        }
        if (!passwordEncoder.matches(password, user.getAdmpassword())) {
            return new Result(Code.LOGIN_ERR, "密码错误", null);
        }
        return new Result(Code.LOGIN_OK, "登录成功", user);
    }

    @DeleteMapping("/{aid}")
    public Result delete(@PathVariable Integer aid) {
        int flag = adminsService.deleteById(aid);
        Integer code = flag > 0 ? Code.DELETE_OK : Code.DELETE_ERR;
        String msg = flag > 0 ? "删除成功" : "删除失败";
        return new Result(code, msg, flag);
    }

    @PutMapping
    public Result update(@RequestBody admins adm) {
        int flag = adminsService.update(adm);
        Integer code = flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag > 0 ? "更改成功" : "更改失败";
        return new Result(code, msg, flag);
    }

    @PostMapping("/ok/{teanumber}")
    public Result Ok(@PathVariable String teanumber) {
        int flag = adminsService.OkTea(teanumber);
        Integer code = flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag > 0 ? "通过成功" : "用过失败";
        return new Result(code, msg, flag);
    }

    @PostMapping("/no/{teanumber}")
    public Result No(@PathVariable String teanumber) {
        int flag = adminsService.NoTea(teanumber);
        Integer code = flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag > 0 ? "拒绝成功" : "拒绝失败";
        return new Result(code, msg, flag);
    }

    /*
   哈希加密方法
    */
    private String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
