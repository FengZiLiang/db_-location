package cn.td.aiot.location.service;

import cn.td.aiot.location.domain.EnclosureRule;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * ClassName  EnclosureRuleService <br/>
 * Description 电子围栏规则服务层 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/8/5 14:36<br/>
 * @since JDK 1.8
 */
@DS("location")
public interface EnclosureRuleService extends IService<EnclosureRule> {

}
