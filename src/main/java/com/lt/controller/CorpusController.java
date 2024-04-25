package com.lt.controller;

import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.domain.Corpus;
import com.lt.domain.CorpusDao;
import com.lt.domain.RcCorpus;
import com.lt.service.BaiduService;
import com.lt.service.CorpusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/corpus")
@CrossOrigin
public class CorpusController {
    @Autowired
    private CorpusService corpusService;
    @Autowired
    private BaiduService baiduService;

    private static String BaiDuAPI_KEY;
    private static String BaiDuSECRET_KEY;
    private static String Url;

    @Value("${baidu.BaiDuAPI_KEY}")
    public void setBaiDuAPI_KEY(String BaiDuAPI_KEY) {
        CorpusController.BaiDuAPI_KEY = BaiDuAPI_KEY;
    }

    @Value("${baidu.BaiDuSECRET_KEY}")
    public void setBaiDuSECRET_KEY(String BaiDuSECRET_KEY) {
        CorpusController.BaiDuSECRET_KEY = BaiDuSECRET_KEY;
    }

    @Value("${strapi.url}")
    public void setUrl(String url) {
        CorpusController.Url = url;
    }

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
        Map<String,Object> data = new HashMap<>();
        data.put("total",corpusService.getAllCorpus().size());
        data.put("list",corpusList);
        return new Result(code, msg, data);
    }

    @GetMapping("/byTnumber/{currentPage}/{pageSize}/{tnumber}")
    public Result getByTnumber(@PathVariable int currentPage, @PathVariable int pageSize, @PathVariable String tnumber) {
        List<Corpus> corpusList = corpusService.getByTnumber(currentPage, pageSize, tnumber);
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
        Map<String,Object> data = new HashMap<>();
        data.put("total",corpusList.size());
        data.put("list",corpusList);
        return new Result(code, msg, data);
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
    public Result uploadFile(@RequestParam("multipartFiles") MultipartFile[] multipartFiles, @ModelAttribute CorpusDao corpusDTO) throws IOException, InterruptedException {
        log.info("开始上传语料资源");
        RcCorpus rcCorpus = new RcCorpus();
        RcCorpus rcCorpus1 = new RcCorpus();

        String file1ContentType = "";
        String file2ContentType = "";
        if (multipartFiles.length < 2) {
//            return new Result(Code.UPDATE_ERR, "文件类型符合要求", null);
            file1ContentType = multipartFiles[0].getContentType();
        }else{
            file1ContentType = multipartFiles[0].getContentType();
            file2ContentType = multipartFiles[1].getContentType();
        }
        //对文件进行类型的判断，multipartFiles[0]是视频或者音频，

        log.info("第一个进行的是:"+file1ContentType);
        log.info("第二个进行的是:"+file2ContentType);


        if(file1ContentType != null && (file1ContentType.equals("audio/mp3") || file1ContentType.equals("audio/mp4") || file1ContentType.equals("application/json"))){
            rcCorpus = corpusService.upload(multipartFiles[0]);//获取上传的文件的id 和 线上地址，方便下面调用百度api
        }
        if(file2ContentType != null && (file2ContentType.startsWith("image/"))){
            rcCorpus1 = corpusService.uploadPic(multipartFiles[1]);//获取上传的图片的id 和 线上地址，方便下面调用百度api
        }

        log.info("上传音视频后的id：" + rcCorpus.getFileId());
        log.info("上传图片后的id：" + rcCorpus1.getFileId());

        corpusDTO.setFile(rcCorpus.getFileId());
        corpusDTO.setPicture(rcCorpus1.getFileId());


        log.info("文件上传成功，开始上传文章内容");
        System.out.println(corpusDTO);
//        正式上传
        int flag = corpusService.create(corpusDTO);
        Integer code = flag != 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag != 0 ? "上传成功" : "上传失败，出现重复属性";
        return new Result(code, msg, corpusDTO);
    }

    @PutMapping
    public Result update(@ModelAttribute CorpusDao corpusDao) {
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

    //部署后存在问题：不会用到的
    @GetMapping("/d3")
    @ResponseBody
    public Result getD3() throws IOException {
        String jsonFilePath = "./static/top5.json";
        Object d3Json = corpusService.readJson(jsonFilePath, Object.class);
        Integer code = d3Json != null ? Code.GET_OK : Code.GET_ERR;
        String msg = d3Json != null ? "查询成功" : "查询失败";
        return new Result(code, msg, d3Json);
    }
}
