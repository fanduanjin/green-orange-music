package cn.fan.service;

import cn.fan.api.service.ISingerService;
import cn.fan.dao.SingerRepository;
import cn.fan.model.Singer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author fanduanjin
 * @Description
 * @Date 2022/7/8
 * @Created by fanduanjin
 */
@Service
public class SingerServiceImpl implements ISingerService {
    @Autowired
    SingerRepository singerRepository;

    @Override
    public Singer addSinger(Singer singer) {
        return singerRepository.save(singer);
    }

    @Override
    public Singer getSingerById(Long id) {
        return singerRepository.getSingerById(id);
    }
}
