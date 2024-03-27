package com.lt.controller;

import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.domain.Corpus;
import com.lt.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommend")
public class RecommendController {
    @Autowired
    private RecommendService recommendService;

    @GetMapping("/byuserid/{userid}")
    public Result getRecommendByUserid(@PathVariable String userid){
        List<Corpus> corpusList = recommendService.getRecommendByUserID(userid);
        Integer code = corpusList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = corpusList != null ? "推荐成功":"推荐失败，请联系后端程序猿";
        return new Result(code,msg,corpusList);
    }
}
