package com.lt.service;

import com.lt.controller.utils.Result;
import com.lt.doadmin.admins;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminsServiceTest {
    @Autowired
    private AdminsService adminsService;
    @Test
    void getAll(){
        System.out.println(adminsService.getAdmAll());
    }
    @Test
    void getBySid(){
        System.out.println(adminsService.getByAid(1));
    }
    @Test
    void getByAdmNumber(){
        String number = "1001";
        System.out.println(adminsService.getByAdmnumber(number));
    }
    @Test
    void registeTest(){
        admins adm = new admins();
        adm.setAdmname("管理员002");
        adm.setAnumber("123");
        adm.setAdmpassword("123456");
        adm.setAsex(0);
        Result result = adminsService.registe(adm);
        System.out.println(result.getMsg());
    }
    @Test
    void loginCheck(){
        admins admins = new admins();
        admins.setAnumber("123");
        admins.setAdmpassword("123456");
        System.out.println(adminsService.loginCheck(admins));
    }
    @Test
    void delete(){
        System.out.println(adminsService.deleteById(2));
    }
    @Test
    void update(){
        admins adm = new admins();
        adm.setAdmname("管理员002-测试");
        adm.setAnumber("123");
        adm.setAdmpassword("123456");
        adm.setAsex(0);
        System.out.println(adminsService.update(adm));
    }
    @Test
    void OkTea(){
        String teaNumber = "100101";
        System.out.println(adminsService.OkTea(teaNumber));
    }
    @Test
    void NoTea(){
        String teaNumber = "100101";
        System.out.println(adminsService.NoTea(teaNumber));

    }
}
