package com.lt.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.domain.download;
import com.lt.service.DownLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/download/{userid}/{who}")
@CrossOrigin
public class DownloadController {
    @Autowired
    private DownLoadService downLoadService;

    @GetMapping("/all/{currentPage}/{pageSize}")
    public Result getAll(@PathVariable String userid, @PathVariable int who, @PathVariable int currentPage, @PathVariable int pageSize) {
        IPage<download> page = downLoadService.getAll(currentPage, pageSize, userid, who);
        if (currentPage > page.getPages()) {
            page = downLoadService.getAll((int) page.getPages(), pageSize, userid, who);
        }
        List<download> downloadList = page.getRecords();
        Integer code = downloadList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = downloadList != null ? "查询成功" : "查询失败";
        return new Result(code, msg, downloadList);
    }

    @GetMapping("/bytitle/{currentPage}/{pageSize}/{title}")
    public Result getByTitle(@PathVariable String title, @PathVariable String userid, @PathVariable int who, @PathVariable int currentPage, @PathVariable int pageSize) {
        IPage<download> page = downLoadService.getByTitle(currentPage, pageSize, title, userid, who);
        if (currentPage > page.getPages()) {
            page = downLoadService.getByTitle((int) page.getPages(), pageSize, title, userid, who);
        }
        List<download> downloadList = page.getRecords();
        Integer code = downloadList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = downloadList != null ? "查询成功" : "查询失败";
        return new Result(code, msg, downloadList);
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody download download) {
        int flag = downLoadService.insert(download);
        Integer code = flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag > 0 ? "添加成功" : "添加失败,可能已下载过";
        return new Result(code, msg, null);
    }

    @DeleteMapping("/{did}")
    public Result delete(@PathVariable int did) {
        int flag = downLoadService.delete(did);
        Integer code = flag > 0 ? Code.DELETE_OK : Code.DELETE_ERR;
        String msg = flag > 0 ? "删除成功" : "删除失败,该资源可能已被删除,请刷新";
        return new Result(code, msg, null);
    }
}
