package cn.td.aiot.location.service;

import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.common.exception.TdException;
import cn.td.aiot.location.domain.Terminal;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * ClassName  TerminalService <br/>
 * Description 终端Service <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/24 10:27<br/>
 * @since JDK 1.8
 */
@DS("location")
public interface TerminalService extends IService<Terminal> {

    /**
     * 查询所有终端
     *
     * @param request    分页查询
     * @param deviceType 设备类型 如果为null 则查询所有
     * @return
     */
    IPage<Terminal> findAllTerminal(QueryRequest request,Integer deviceType) ;
}
