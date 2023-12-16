package com.lt.controller;

import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.domain.ExerciseScoreDao;
import com.lt.domain.Exercises;
import com.lt.domain.ExercisesDao;
import com.lt.domain.RcCorpus;
import com.lt.service.BaiduService;
import com.lt.service.CorpusService;
import com.lt.service.ExercisesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {
    @Autowired
    private ExercisesService exercisesService;
    @Autowired
    private BaiduService baiduService;
    @Autowired
    private CorpusService corpusService;
    private static String BaiDuAPI_KEY;
    private static String BaiDuSECRET_KEY;
    private static String Url;

    @Value("${baidu.BaiDuAPI_KEY}")
    public void setBaiDuAPI_KEY(String BaiDuAPI_KEY) {
        ExerciseController.BaiDuAPI_KEY = BaiDuAPI_KEY;
    }

    @Value("${baidu.BaiDuSECRET_KEY}")
    public void setBaiDuSECRET_KEY(String BaiDuSECRET_KEY) {
        ExerciseController.BaiDuSECRET_KEY = BaiDuSECRET_KEY;
    }

    @Value("${strapi.url}")
    public void setUrl(String url) {
        ExerciseController.Url = url;
    }

    @GetMapping("/{stuId}")
    public Result getAllExercises(@PathVariable String stuId) {
        List<Exercises> exercisesList = exercisesService.getAllExercises(stuId);
        Integer code = exercisesList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = exercisesList != null ? "查询成功" : "数据查询失败,请重试!";
        return new Result(code, msg, exercisesList);
    }

    @GetMapping("/byEid/{eid}")
    public Result getOneExercise(@PathVariable int eid) {
        Exercises exercises = exercisesService.getOneExercises(eid);
        Integer code = exercises != null ? Code.GET_OK : Code.GET_ERR;
        String msg = exercises != null ? "查询成功" : "数据查询失败，请重试!";
        return new Result(code, msg, exercises);
    }

    @GetMapping("/byCid/{cid}")
    public Result getByCid(@PathVariable int cid) {
        List<Exercises> exercisesList = exercisesService.getByCid(cid);
        Integer code = exercisesList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = exercisesList != null ? "查询成功" : "数据查询失败，请重试!";
        return new Result(code, msg, exercisesList);
    }

    @GetMapping("{currentPage}/{pageSize}/{stuId}")
    public Result getPage(@PathVariable int currentPage, @PathVariable int pageSize, @PathVariable String stuId) {
        List<Exercises> exercisesList = exercisesService.getPage(currentPage, pageSize, stuId);
        Integer code = exercisesList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = exercisesList != null ? "查询成功" : "数据查询失败，请重试!";
        return new Result(code, msg, exercisesList);
    }

    @PostMapping(value = "/upload")
    public Result uploadStuFile(@RequestParam("multipartFile") MultipartFile multipartFile, @ModelAttribute ExercisesDao exercisesDao) throws IOException, InterruptedException {
        RcCorpus rcCorpus = exercisesService.upload(multipartFile);
        System.out.println("上传后的文件地址为: " + rcCorpus.getFileId());
        exercisesDao.setStuFile(rcCorpus.getFileId());
//        调用百度api，将识别结果赋值给identifyText
        System.out.println(Url.substring(0, Url.length() - 1) + rcCorpus.getFileUrl());
//        未部署时的方法
//        String result = baiduService.Toriginaltext("http://8.137.53.253:1337/uploads/cancer_treatment_could_get_a_vaccine_b326cf9cd8.mp3", corpusService.getOneCorpus(exercisesDao.getCorpus()).getDirection(), BaiDuAPI_KEY, BaiDuSECRET_KEY);
//        部署后的方法
        String result = baiduService.Toriginaltext(Url.substring(0, Url.length() - 1) + rcCorpus.getFileUrl(), corpusService.getOneCorpus(exercisesDao.getCorpus()).getDirection(), BaiDuAPI_KEY, BaiDuSECRET_KEY);
        exercisesDao.setIdentifyText(result);
//        正式上传
        int flag = exercisesService.create(exercisesDao);
        Integer code = flag != 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag != 0 ? "上传成功" : "上传失败，出现重复属性";
        return new Result(code, msg, exercisesDao);
    }

    @PutMapping
    public Result update(@ModelAttribute ExerciseScoreDao exerciseScoreDao) {
        int flag = exercisesService.updateScore(exerciseScoreDao);
        Integer code = flag != 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag != 0 ? "更新成功" : "更新失败";
        return new Result(code, msg, null);
    }

    @DeleteMapping("/{eid}")
    public Result delete(@PathVariable int eid) {
        int flag = exercisesService.delete(eid);
        Integer code = flag != 0 ? Code.DELETE_OK : Code.DELETE_ERR;
        String msg = flag != 0 ? "删除成功" : "删除失败";
        return new Result(code, msg, null);
    }

    @GetMapping("/byClass/{clasname}")
    public Result getExerciseByClass(@PathVariable String clasname) {
        List<Exercises> exercisesList = exercisesService.getExerciseByClass(clasname);
        Integer code = exercisesList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = exercisesList != null ? "查询班级练习成功" : "查询班级练习失败";
        return new Result(code, msg, exercisesList);
    }
}
