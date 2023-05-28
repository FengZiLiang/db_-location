package cn.td.aiot.location.service.impl;

import cn.td.aiot.location.dao.EnclosureRuleMapper;
import cn.td.aiot.location.domain.EnclosureRule;
import cn.td.aiot.location.service.EnclosureRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * ClassName  EnclosureRuleService <br/>
 * Description 电子围栏规则服务层 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/8/5 14:36<br/>
 * @since JDK 1.8
 */
@Service
public class EnclosureRuleServiceImpl extends ServiceImpl<EnclosureRuleMapper, EnclosureRule> implements EnclosureRuleService {
    private final EnclosureRuleMapper enclosureRuleMapper;

    public EnclosureRuleServiceImpl(EnclosureRuleMapper enclosureRuleMapper) {
        this.enclosureRuleMapper = enclosureRuleMapper;
    }

}
