package com.jtfu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jtfu.entity.Exam;
import com.jtfu.mapper.ExamMapper;
import com.jtfu.service.IExamService;
import org.springframework.stereotype.Service;

@Service
public class ExamServiceImpl  extends ServiceImpl<ExamMapper, Exam> implements IExamService {
}
