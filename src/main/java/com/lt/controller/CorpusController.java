package com.lt.controller;

import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.doadmin.Corpus;
import com.lt.doadmin.CorpusDao;
import com.lt.service.CorpusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequestMapping("/api/corpus")
public class CorpusController {
    @Autowired
    private CorpusService corpusService;

    @GetMapping
    public Result getAllCorpus() {
        List<Corpus> corpusList = corpusService.getAllCorpus();
        Integer code = corpusList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = corpusList != null ? "查询成功" : "数据查询失败,请重试!";
        return new Result(code, msg, corpusList);
    }

    @GetMapping("/byCid/{cid}")
    public Result getOneCorpus(@PathVariable int cid) {
        Corpus corpus = corpusService.getOneCorpus(cid);
        Integer code = corpus != null ? Code.GET_OK : Code.GET_ERR;
        String msg = corpus != null ? "查询成功" : "数据查询失败，请重试!";
        return new Result(code, msg, corpus);
    }

    @GetMapping("{currentPage}/{pageSize}")
    public Result getPage(@PathVariable int currentPage, @PathVariable int pageSize) {
        List<Corpus> corpusList = corpusService.getPage(currentPage, pageSize);
        Integer code = corpusList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = corpusList != null ? "查询成功" : "数据查询失败，请重试!";
        return new Result(code, msg, corpusList);
    }

    @GetMapping("/ByTag_ids/{Tag_ids}")
    public Result getByTag_ids(@PathVariable int Tag_ids) {
        List<Corpus> corpusList = corpusService.getByTag_ids(Tag_ids);
        Integer code = corpusList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = corpusList != null ? "查询成功" : "数据查询失败，请重试!";
        return new Result(code, msg, corpusList);
    }

    //    @GetMapping("/search/{Direction}/{Difficulty}/{Type}/{Tag_ids}/{Title_contains}")
//    public Result getByfactor(@PathVariable(value = "Direction", required = false) int Direction, @PathVariable(value = "Difficulty", required = false) int Difficulty, @PathVariable(value = "Type", required = false) int Type, @PathVariable(value = "Tag_ids", required = false) int Tag_ids, @PathVariable(value = "Title_contains", required = false) String Title_contains) {
//        List<Corpus> corpusList = corpusService.getByFactory(Direction, Difficulty, Type, Tag_ids, Title_contains);
//        Integer code = corpusList != null ? Code.GET_OK : Code.GET_ERR;
//        String msg = corpusList != null ? "查询成功" : "数据查询失败，请重试!";
//        return new Result(code, msg, corpusList);
//    }
    @GetMapping("/search/{currentPage}/{pageSize}/{Direction}/{Difficulty}/{Type}/{Tag_ids}/{Title_contains}")
    public Result getByfactor(
            @PathVariable(name = "currentPage") int currentPage,
            @PathVariable(name = "pageSize") int pageSize,
            @PathVariable(name = "Direction") int Direction,
            @PathVariable(name = "Difficulty") int Difficulty,
            @PathVariable(name = "Type") int Type,
            @PathVariable(name = "Tag_ids") int Tag_ids,
            @PathVariable(name = "Title_contains") String Title_contains) {
        // 判断 Title_contains 是否等于默认值
        if (Title_contains.equals("default")) {
            // 如果等于默认值，则不使用该参数进行查询
            Title_contains = null;
        }
        List<Corpus> corpusList = corpusService.getByFactory(currentPage, pageSize, Direction, Difficulty, Type, Tag_ids, Title_contains);
        Integer code = corpusList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = corpusList != null ? "查询成功" : "数据查询失败，请重试!";
        return new Result(code, msg, corpusList);
    }

    @GetMapping({"/self/{currentPage}/{pageSize}/{AuthorID}/{Title_contains}/{created_at}", "/self/{currentPage}/{pageSize}/{AuthorID}", "/self/{currentPage}/{pageSize}/{AuthorID}/{Title_contains}", "/self/{currentPage}/{pageSize}/{AuthorID}/{created_at}"})
    public Result getBySelf(
            @PathVariable(name = "currentPage") int currentPage,
            @PathVariable(name = "pageSize") int pageSize,
            @PathVariable(name = "AuthorID") String AuthorID,
            @PathVariable(name = "Title_contains", required = false) String Title_contains,
            @PathVariable(name = "created_at", required = false) String created_at) {
        if (Title_contains.equals("default")) {
            Title_contains = null;
        }
        List<Corpus> corpusList = corpusService.getSelf(currentPage, pageSize, AuthorID, Title_contains, created_at);
        System.out.println("AuthorID: " + AuthorID + "\n" + "Title_contains: " + Title_contains + "\n" + "created_at: " + created_at);
        Integer code = corpusList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = corpusList != null ? "查询成功" : "数据查询失败，请重试!";
        return new Result(code, msg, corpusList);
    }


    @PostMapping(value = "/upload")
    public Result uploadFile(@RequestParam("multipartFile") MultipartFile multipartFile, @ModelAttribute CorpusDao corpusDTO) {
//        System.out.println("开始执行");
        System.out.println(multipartFile);
        Long fileId = corpusService.upload(multipartFile);//获取上传的文件的id
        System.out.println(fileId);
//        System.out.println("标签值为：" + corpusDTO.getTag_ids());
        corpusDTO.setFile(fileId);
//        System.out.println();
        int flag = corpusService.create(corpusDTO);
        Integer code = flag != 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag != 0 ? "上传成功" : "上传失败，出现重复属性";
        return new Result(code, msg, corpusDTO);
    }

    @PutMapping
    public Result update(@ModelAttribute CorpusDao corpusDao) {
//        System.out.println(corpusDao);
//        System.out.println("成功进入！！！");
        int flag = corpusService.update(corpusDao);
//        System.out.println("执行完毕");
        Integer code = flag != 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag != 0 ? "更新成功" : "更新失败";
        return new Result(code, msg, null);
    }

    @DeleteMapping("/{cid}")
    public Result delete(@PathVariable int cid) {
        int flag = corpusService.delete(cid);
        Integer code = flag != 0 ? Code.DELETE_OK : Code.DELETE_ERR;
        String msg = flag != 0 ? "删除成功" : "删除失败";
        return new Result(code, msg, null);
    }
}
