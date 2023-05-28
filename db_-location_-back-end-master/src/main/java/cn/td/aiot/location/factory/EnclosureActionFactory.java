package cn.td.aiot.location.factory;

import cn.td.aiot.common.utils.SpringContextUtil;
import cn.td.aiot.location.action.EnclosureAction;
import cn.td.aiot.location.domain.CharacterType;
import cn.td.aiot.location.handler.EnclosureActionHandler;
import cn.td.aiot.location.service.impl.CharacterTypeServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName  EnterEnclosureFactory <br/>
 * Description  进入电子围栏触发事件的factory <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/8/11 10:00<br/>
 * @since JDK 1.8
 */
public class EnclosureActionFactory {


    private static Map<String, EnclosureAction> handle = new HashMap<>(12);

    static {
        CharacterTypeServiceImpl typeService = (CharacterTypeServiceImpl) SpringContextUtil.getBean(CharacterTypeServiceImpl.class);
        List<CharacterType> types = typeService.list();
        for (CharacterType cty : types) {
            //
            switch (cty.getCharacterType()) {
                case 0:
                    handle.put(String.valueOf(cty.getCharacterType()), new EnclosureActionHandler());
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 得到ActionHandler
     *
     * @param action 行为
     * @return EnclosureAction
     */
    public EnclosureAction getActionHandler(String action) {
        return handle.get(action);
    }
}
