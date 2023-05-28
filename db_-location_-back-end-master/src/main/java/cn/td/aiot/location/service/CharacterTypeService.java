package cn.td.aiot.location.service;

import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.location.domain.CharacterType;
import cn.td.aiot.location.domain.Enclosure;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * ClassName  CharacterTypeService <br/>
 * Description 特性类型服务层 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/8/5 14:35<br/>
 * @since JDK 1.8
 */
@DS("location")
public interface CharacterTypeService extends IService<CharacterType> {


}
