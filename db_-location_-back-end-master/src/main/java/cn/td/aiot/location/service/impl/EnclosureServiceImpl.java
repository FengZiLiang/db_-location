package cn.td.aiot.location.service.impl;

import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.common.domain.TiduConstant;
import cn.td.aiot.common.utils.SortUtil;
import cn.td.aiot.location.dao.EnclosureMapper;
import cn.td.aiot.location.domain.Enclosure;
import cn.td.aiot.location.service.EnclosureService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * ClassName  EnclosureService <br/>
 * Description  电子围栏服务层<br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/8/5 14:32<br/>
 * @since JDK 1.8
 */
@Service
public class EnclosureServiceImpl extends ServiceImpl<EnclosureMapper, Enclosure> implements EnclosureService {

    private final EnclosureMapper enclosureMapper;

    public EnclosureServiceImpl(EnclosureMapper enclosureMapper) {
        this.enclosureMapper = enclosureMapper;
    }

    @Override
    public IPage<Enclosure> findAllEnclosure(QueryRequest request) {
        Page<Enclosure> enclosurePage = new Page<>();
        SortUtil.handlePageSort(request, enclosurePage, "createTime", TiduConstant.ORDER_DESC, true);
        return enclosureMapper.selectPage(enclosurePage,null);
    }
}
