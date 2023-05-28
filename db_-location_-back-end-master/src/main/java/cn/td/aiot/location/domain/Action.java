package cn.td.aiot.location.domain;

/**
 * ClassName  Action <br/>
 * Description 动作枚举类  <br/>
 *
 * @author YeYouGui
 * @version 1.0
 * @date 2020/8/18 17:00<br/>
 * @since JDK 1.8
 */
public enum Action {
    /**
     * 规则枚举
     */
    ILLEGAL_ENTER(0, "非法越界");

    private final Integer status;
    private final String actionName;

    Action(Integer status, String actionName) {
        this.status = status;
        this.actionName = actionName;
    }

    public Integer getStatus() {
        return status;
    }

    public String getActionName() {
        return actionName;
    }
}
