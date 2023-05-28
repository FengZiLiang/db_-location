package cn.td.aiot.location.service.impl;

import cn.td.aiot.common.domain.QueryRequest;
import cn.td.aiot.location.dao.TerminalMapper;
import cn.td.aiot.location.domain.Terminal;
import cn.td.aiot.location.service.TerminalService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * ClassName  TerminalServiceImpl <br/>
 * Description 终端服务实现类 <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/7/24 10:37<br/>
 * @since JDK 1.8
 */
@Service
public class TerminalServiceImpl extends ServiceImpl<TerminalMapper, Terminal> implements TerminalService {

    private final TerminalMapper terminalMapper;

    public TerminalServiceImpl(TerminalMapper terminalMapper) {
        this.terminalMapper = terminalMapper;
    }

    @Override
    public IPage<Terminal> findAllTerminal(QueryRequest request, Integer deviceType) {
        try {
            Page<Terminal> page = new Page<>(request.getPageNum(), request.getPageSize());
            if (deviceType != null) {
                //根据设备类型查询
                QueryWrapper<Terminal> deviceWrapper = new QueryWrapper<>();
                deviceWrapper.eq("device_type", deviceType);
                return terminalMapper.selectPage(page, deviceWrapper);
            } else {
                // 查询所有
                return terminalMapper.selectPage(page, null);
            }

        } catch (Exception e) {
            log.error("查询终端失败");
            return null;
        }
    }
}
