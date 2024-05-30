package com.lt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.lt.domain.Favorites;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class FavoritesServiceTest {
    @Autowired
    private FavoritesService favoritesService;

    @Test
    void getAllStuTest() {
        IPage<Favorites> page = favoritesService.getAllStu(1, 3, "123321");
        List<Favorites> favoritesList = page.getRecords();
        System.out.println(favoritesList);
    }

    @Test
    void getByTag_ids() {
        IPage<Favorites> page = favoritesService.getByTag_ids(1, 1, 2, 3, "123321");
        System.out.println(page.getRecords());
    }

    @Test
    void getByTitle() {
        IPage<Favorites> page = favoritesService.getByTitle(1, 1, 2, "测试", "123321");
        System.out.println(page.getRecords());
    }

    @Test
    void insert() {
        Favorites favorites = new Favorites();
        favorites.setCid(4);
        favorites.setCtitle("我是Java测试标题");
        favorites.setWho(3);
        favorites.setUserid("123321");
        favorites.setTagids(2);
        int flag = favoritesService.insert(favorites);
        System.out.println(flag);
    }

    /*
    更新操作
     */
    @Test
    void update() {
        Favorites favorites = new Favorites();
        favorites.setFid(60);
        favorites.setCid(4);
        favorites.setCtitle("我S更新内容");
        favorites.setWho(3);
        favorites.setUserid("123321");
        favorites.setTagids(2);
        int flag = favoritesService.update(favorites);
        System.out.println(flag);
    }

    @Test
    void delete() {
        int flag = favoritesService.delete(7);
        System.out.println(flag);
    }
    @Test
    void isFavor(){
        System.out.println(favoritesService.isFavor("210001",4));
    }
}
