package com.lt.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.domain.*;
import com.lt.service.BaiduService;
import com.lt.service.ClassesService;
import com.lt.service.LabourService;
import com.lt.service.TeachersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin
@Slf4j
public class TeacherController {
    @Autowired
    private TeachersService teachersService;
    @Autowired
    private LabourService labourService;
    @Autowired
    private ClassesService classesService;

    private static String WenXinAPI;
    private static String WenXinSecurity;

    @Value("${baidu.WenXinAPI}")
    public void setWenXinAPI(String wenXinAPI) {
        TeacherController.WenXinAPI = wenXinAPI;
    }

    @Value("${baidu.WenXinSecurity}")
    public void setWenXinSecurity(String WenXinSecurity) {
        TeacherController.WenXinSecurity = WenXinSecurity;
    }

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping
    public Result getTeaAll() {
        List<teachers> teachersList = teachersService.getTeaAll();
        Integer code = teachersList != null ? Code.SAVE_OK : Code.SAVE_ERR;
        String msg = teachersList != null ? "查询成功" : "数据查询失败,请重试!";
        Map<String, Object> data = new HashMap<>();
        data.put("total", teachersList.size());
        data.put("list", teachersList);
        return new Result(code, msg, data);
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
        Map<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("list", teachersList);
        return new Result(code, msg, data);
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

    @DeleteMapping("/{tid}") // 对于删除功能 可能还需要更改——只需要将teastate有设置为0就好了，当然也可以用管理员操作
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

    /*
     * 教师管理班级部分
     */
    @GetMapping("/selfclass")
    public Result getAllClass() {
        List<Classes> classesList = classesService.getAllClass();
        Integer code = classesList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = classesList != null ? "查询成功" : "查询失败";
        return new Result(code, msg, classesList);
    }

    @GetMapping("/selfclass/{tunmber}")
    public Result getAllSelfClass(@PathVariable String tunmber) {
        List<Classes> classesList = classesService.getBySelf(tunmber);
        Integer code = classesList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = classesList != null ? "查询成功" : "查询失败";
        return new Result(code, msg, classesList);
    }

    @PostMapping("/selfclass")
    public Result insertSelfClass(@RequestBody Classes classes) {
        int flag = classesService.addClss(classes);
        Integer code = flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag > 0 ? "添加成功" : "添加失败";
        return new Result(code, msg, null);
    }

    @DeleteMapping("/selfclass/{cid}")
    public Result deleteSelf(@PathVariable String cid) {
        int flag = classesService.deleteByCid(cid);
        Integer code = flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag > 0 ? "删除成功" : "删除失败";
        return new Result(code, msg, null);
    }

    @PutMapping("/selfclass")
    public Result updateSelfClass(@RequestBody Classes classes) {
        int flag = classesService.update(classes);
        Integer code = flag != 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag != 0 ? "修改班级信息成功" : "修改失败，可能新班级已存在";
        return new Result(code, msg, null);
    }

    /**
     * 教师发布练习
     *
     * @param task 练习信息
     * @return 是否成功信息
     */
    @PostMapping("/publish")
    public Result publish(@RequestBody Task task) {
        int flag = teachersService.publish(task);
        Integer code = flag != 0 ? Code.UPDATE_OK : Code.UPDATE_OK;
        String msg = flag != 0 ? "发布练习成功" : "已重新发布！！！";
        return new Result(code, msg, null);
    }

    /**
     * 取消练习
     *
     * @param cid       语料id
     * @param teanumber 教师账号
     * @return 操作状态
     */
    @GetMapping("/notpublish/{cid}/{teanumber}")
    public Result notpublish(@PathVariable int cid, @PathVariable String teanumber) {
        int flag = teachersService.notpublish(cid, teanumber);
        Integer code = flag != 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag != 0 ? "取消练习成功" : "取消练习失败";
        return new Result(code, msg, null);
    }

    @GetMapping("/charts/{className}")
    public Result getCharts(@PathVariable String className) {
        List<Charts> chartsList = teachersService.getCharts(className);
        Integer code = chartsList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = chartsList != null ? "查询成功，请开始统计吧！" : "查询失败，请联系开发人员";
        return new Result(code, msg, chartsList);
    }

    @GetMapping("/wenxin/{eid}")
    public Result getWenxin_Value(@PathVariable int eid) throws IOException {
        String Value = teachersService.getWenxin_Value(eid, WenXinAPI, WenXinSecurity);
        Integer code = Value != "" ? Code.GET_OK : Code.GET_ERR;
        String msg = Value != "" ? "文心模型 评价成功" : "文心模型 评价失败";
        return new Result(code, msg, Value);
    }

    @PostMapping("/autocorrect/{correctId}")
    public Result autoCorrect(@PathVariable int correctId) {
        try {
            labourService.autoCorrect(correctId, WenXinAPI, WenXinSecurity);
            return new Result(Code.UPDATE_OK, "智能批改完成", null);
        } catch (Exception e) {
            return new Result(Code.UPDATE_ERR, "智能批改失败", e.getMessage());
        }
    }

    /*
     * 哈希加密方法
     */
    private String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    /**
     * 提交每个维度的评语和分数
     *
     * @param dimensionScores 包含各个维度评语和分数的Map
     * @return 操作状态
     */
    @PostMapping("/submitDimensionScores")
    public Result submitDimensionScores(@RequestBody Map<String, Map<String, Object>> dimensionScores) {
        // 处理每个维度的评语和分数
        System.out.println("维度评分: " + dimensionScores);

        // 保存到文件或数据库中
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("dimension_scores.json"), dimensionScores);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(500, "保存维度评分时出错", null);
        }

        return new Result(200, "维度评分提交成功", null);
    }
}
