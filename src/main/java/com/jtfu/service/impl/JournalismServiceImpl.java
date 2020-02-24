package com.jtfu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jtfu.entity.Journalism;
import com.jtfu.mapper.JournalismMapper;
import com.jtfu.service.IJournalismService;
import org.springframework.stereotype.Service;

@Service
public class JournalismServiceImpl extends ServiceImpl<JournalismMapper, Journalism> implements IJournalismService {
}
