package cn.td.aiot.location.service.impl;

import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.common.domain.TiduConstant;
import cn.td.aiot.common.utils.SortUtil;
import cn.td.aiot.location.dao.ParkMapper;
import cn.td.aiot.location.domain.Park;
import cn.td.aiot.location.service.ParkService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * ClassName  ParkServiceImpl <br/>
 * Description 园区Service实现 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/24 10:36<br/>
 * @since JDK 1.8
 */
@Slf4j
@Service
public class ParkServiceImpl extends ServiceImpl<ParkMapper, Park> implements ParkService {

    private final ParkMapper parkMapper;

    public ParkServiceImpl(ParkMapper parkMapper) {
        this.parkMapper = parkMapper;
    }


    @Override
    public IPage<Park> findAllPark(QueryRequest queryRequest) {
        try {
            Page<Park> parkPage = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
            SortUtil.handlePageSort(queryRequest, parkPage, "park_id", TiduConstant.ORDER_DESC, false);
            return parkMapper.selectPage(parkPage, null);
        } catch (Exception e) {
            log.error("查询园区失败");
            return null;
        }
    }
}
