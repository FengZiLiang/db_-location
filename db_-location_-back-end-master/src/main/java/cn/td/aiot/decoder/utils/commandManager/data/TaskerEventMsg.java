package cn.td.aiot.decoder.utils.commandManager.data;

import cn.td.aiot.decoder.utils.commandManager.callback.EventCallBackType;

/**
 * 命令行事件消息
 *
 *
 *
 */
public class TaskerEventMsg {
	EventCallBackType ecbt;
	CommandTasker tasker;

	public TaskerEventMsg(EventCallBackType ecbt, CommandTasker tasker) {
		super();
		this.ecbt = ecbt;
		this.tasker = tasker;
	}

	public EventCallBackType getEcbt() {
		return ecbt;
	}

	public void setEcbt(EventCallBackType ecbt) {
		this.ecbt = ecbt;
	}

	public CommandTasker getTasker() {
		return tasker;
	}

	public void setTasker(CommandTasker tasker) {
		this.tasker = tasker;
	}

	@Override
	public String toString() {
		return "CommandEventMsg [ecbt=" + ecbt + ", tasker=" + tasker + "]";
	}

}
