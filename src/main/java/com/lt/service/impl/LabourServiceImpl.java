package com.lt.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.dao.labourDao;
import com.lt.domain.Labour;
import com.lt.service.LabourService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 对应“发起人工批改”功能
 * 1.创建“批改”表单，就是数据库的“增”。但是重点：教师需要List<Teacher> teachers类来实现（单独存放到labour_teacher表中）
 * 2.查询接口：根据当前的correct_id、labour_id作为条件查询exercises表，返回数据（5个维度、批改时间、批改者teacherid）
 * 3.进度接口：根据correct_id、labour_id查询exercise表，统计exercise.correct_type = 4 的数量（表示还未批改的数量）
 * 4.时间范围：查询labour中的开始、结束时间
 * 5.一键智能批改（这个放在文心一言那边比好），主要传入correctID（将correct_type = 0的进行智能评分）
 * 6.智能批改-批改记录查询-平均分：查询接口，根据correct_id对exercises.correct_type != 3 查询出来。
 */
@Service
@Slf4j
public class LabourServiceImpl extends ServiceImpl<labourDao, Labour> implements LabourService {

}
