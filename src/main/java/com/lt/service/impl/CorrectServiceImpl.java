package com.lt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.dao.correctDao;
import com.lt.domain.Correct;
import com.lt.service.CorrectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 试题批改Service，
 * 1.“导入数据”:用于创建试题批改任务（处理上传过程中的原始corpus、标准exercises,correct_type = 2）
 * 2.批量上传学生作答（使用exercises接口，correct_type = 0）
 * 3.查询所有的correct类（需要新建一个展示 “类” -> 根据前端展示页面选择字段）
 * 4.删除功能（假删除，更改当前correct.state 值）
 * 5.“编辑”-信息（暂时前端没明确要更新什么信息）
 */
@Service
@Slf4j
public class CorrectServiceImpl extends ServiceImpl<correctDao, Correct> implements CorrectService {
}
