package cn.td.aiot.decoder.utils.commandManager.callback;

import cn.td.aiot.decoder.utils.commandManager.data.CommandTasker;

/**
 * 事件回调
 *
 *
 *
 */
public interface EventCallBack {
	
	/**
	 * 命令行执行开始事件
	 * @param t -事件类型
	 * @param tasker -任务信息
	 */
	boolean callback(EventCallBackType t, CommandTasker tasker);
}
