package com.lt.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lt.controller.utils.Code;
import com.lt.controller.utils.Result;
import com.lt.domain.Favorites;
import com.lt.service.CorpusService;
import com.lt.service.FavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favority/{userid}")
@CrossOrigin
public class FavorityController {
    @Autowired
    FavoritesService favoritesService;
    @Autowired
    CorpusService corpusService;

    @GetMapping("/student/{currentPage}/{pageSize}")
    public Result getAllStu(@PathVariable int currentPage, @PathVariable int pageSize, @PathVariable String userid) {
        IPage<Favorites> page = favoritesService.getAllStu(currentPage, pageSize, userid);
        //如果当前页码值大于总页码值，使用最大页码值
        if (currentPage > page.getPages()) {
            page = favoritesService.getAllStu((int) page.getPages(), pageSize, userid);
        }
        List<Favorites> favoritesList = page.getRecords();
        for(Favorites favorites : favoritesList){
            favorites.setCorpusFileUrl(corpusService.getOneCorpus(favorites.getCid()).getFile().get(0).getUrl());
        }
        Map<String,Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("list",favoritesList);
        Integer code = favoritesList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = page.getTotal() != 0 ? "查询成功" : "该用户收藏为0,或查询出现异常";
        return new Result(code, msg, data);
    }

    @GetMapping("/teacher/{currentPage}/{pageSize}")
    public Result getAllTea(@PathVariable int currentPage, @PathVariable int pageSize, @PathVariable String userid) {
        IPage<Favorites> page = favoritesService.getAllTea(currentPage, pageSize, userid);
        //如果当前页码值大于总页码值，使用最大页码值
        if (currentPage > page.getPages()) {
            page = favoritesService.getAllTea((int) page.getPages(), pageSize, userid);
        }
        List<Favorites> favoritesList = page.getRecords();
        for(Favorites favorites : favoritesList){
            favorites.setCorpusFileUrl(corpusService.getOneCorpus(favorites.getCid()).getFile().get(0).getUrl());
        }
        Map<String,Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("list",favoritesList);
        Integer code = favoritesList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = page.getTotal() != 0 ? "查询成功" : "该用户收藏为0,或查询出现异常";
        return new Result(code, msg, data);
    }

    /**
     * 判断当前语料corpus是否被收藏
     * @param userid 用户ID
     * @param cid 语料Corpus的ID
     * @return 返回是否存在的判断
     */
    @GetMapping("/{cid}")
    public Result isFavor(@PathVariable String userid,@PathVariable int cid){
        int flag = favoritesService.isFavor(userid,cid);
        Integer code = flag == 1 ? Code.GET_OK : Code.GET_ERR;
        String msg = flag == 1 ? "用户已收藏该语料" : "用户未收藏该语料";
        return new Result(code,msg,flag);
    }

    @GetMapping("/admins/{currentPage}/{pageSize}")
    public Result getAllAdm(@PathVariable int currentPage, @PathVariable int pageSize, @PathVariable String userid) {
        IPage<Favorites> page = favoritesService.getAllAdm(currentPage, pageSize, userid);
        //如果当前页码值大于总页码值，使用最大页码值
        if (currentPage > page.getPages()) {
            page = favoritesService.getAllAdm((int) page.getPages(), pageSize, userid);
        }
        List<Favorites> favoritesList = page.getRecords();
        for(Favorites favorites : favoritesList){
            favorites.setCorpusFileUrl(corpusService.getOneCorpus(favorites.getCid()).getFile().get(0).getUrl());
        }
        Map<String,Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("list",favoritesList);
        Integer code = favoritesList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = page.getTotal() != 0 ? "查询成功" : "该用户收藏为0,或查询出现异常";
        return new Result(code, msg, data);
    }

    @GetMapping("/byTag/{who}/{currentPage}/{pageSize}/{tagid}")
    public Result getByTag_ids(@PathVariable int who, @PathVariable int currentPage, @PathVariable int pageSize, @PathVariable int tagid, @PathVariable String userid) {
        IPage<Favorites> page = favoritesService.getByTag_ids(who, currentPage, pageSize, tagid, userid);
        //如果当前页码值大于总页码值，使用最大页码值
        if (currentPage > page.getPages()) {
            page = favoritesService.getByTag_ids(who, (int) page.getPages(), pageSize, tagid, userid);
        }
        List<Favorites> favoritesList = page.getRecords();
        Integer code = favoritesList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = favoritesList != null ? "查询成功" : "查询失败";
        return new Result(code, msg, favoritesList);
    }

    @GetMapping("/byTitle/{who}/{currentPage}/{pageSize}/{Title}")
    public Result getByTitle(@PathVariable int who, @PathVariable int currentPage, @PathVariable int pageSize, @PathVariable String Title, @PathVariable String userid) {
        IPage<Favorites> page = favoritesService.getByTitle(who, currentPage, pageSize, Title, userid);
        //如果当前页码值大于总页码值，使用最大页码值
        if (currentPage > page.getPages()) {
            page = favoritesService.getByTitle(who, (int) page.getPages(), pageSize, Title, userid);
        }
        List<Favorites> favoritesList = page.getRecords();
        Integer code = favoritesList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = favoritesList != null ? "查询成功" : "查询失败";
        return new Result(code, msg, favoritesList);
    }

    @PostMapping("/insert")
    public Result inser(@RequestBody Favorites favorites) {
        int flag = favoritesService.insert(favorites);
        Integer code = flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag > 0 ? "添加成功" : "添加失败,可能已经收藏";
        return new Result(code, msg, null);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Favorites favorites) {
        int flag = favoritesService.update(favorites);
        Integer code = flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag > 0 ? "更细成功" : "更新失败";
        return new Result(code, msg, null);
    }

    @DeleteMapping("/{cid}")
    public Result delete(@PathVariable int cid) {
        int flag = favoritesService.delete(cid);
        Integer code = flag > 0 ? Code.DELETE_OK : Code.DELETE_ERR;
        String msg = flag > 0 ? "删除成功" : "删除失败,该资源可能已被删除,请刷新";
        return new Result(code, msg, null);
    }
}
