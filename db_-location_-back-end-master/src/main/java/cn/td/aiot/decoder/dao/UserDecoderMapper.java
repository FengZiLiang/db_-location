package cn.td.aiot.decoder.dao;

import cn.td.aiot.decoder.domain.UserDecoder;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@DS("decoder")
@Mapper
public interface UserDecoderMapper extends BaseMapper<UserDecoder> {
    public List<UserDecoder> findAllByUserId(Long userId);
    List<UserDecoder> findAllByDecoderId(Long decoderId);
    List<UserDecoder> findAllByUserIdAndDecoderId(Long decoderId,Long userId);
    void updateByUserIdAndDecoderId(UserDecoder userDecoder);
}
