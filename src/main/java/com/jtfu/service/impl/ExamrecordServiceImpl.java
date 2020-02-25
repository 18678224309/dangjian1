package com.jtfu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jtfu.entity.Examrecord;
import com.jtfu.mapper.ExamrecordMapper;
import com.jtfu.service.IExamrecordService;
import org.springframework.stereotype.Service;

@Service
public class ExamrecordServiceImpl extends ServiceImpl<ExamrecordMapper, Examrecord> implements IExamrecordService {
}
