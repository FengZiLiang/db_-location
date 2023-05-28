package cn.td.aiot.decoder.utils.commandManager.handler;

import cn.td.aiot.common.properties.TdProperties;
import cn.td.aiot.decoder.utils.commandManager.CommandManager;
import cn.td.aiot.decoder.utils.commandManager.TdFFmpegManagerImpl;

/**
 * 默认任务消息输出处理
 *
 */
public class DefaultOutHandlerMethod implements OutHandlerMethod{

	/**
	 * 任务是否异常中断，如果
	 */
	public boolean isBroken=false;

	public void errCont(String msg){
		System.err.println(msg);
		System.err.println("失败，设置中断状态");
		isBroken=true;

	}
	
	@Override
	public void parse(String id,String msg) {
		//过滤消息
		if (msg.indexOf("fail") != -1) {
			errCont(id + "任务可能发生故障：" + msg);
		}else if(msg.indexOf("Failed")!=-1){
			errCont(id + "任务可能发生故障：" + msg);
		}else if(msg.indexOf("miss")!= -1) {
			System.err.println(id + "任务可能发生丢包：" + msg);
//			System.err.println("失败，设置中断状态");
//			isBroken=true;
/*		}else if(msg.indexOf("corrupt decoded frame in stream 0")!=-1){
			System.err.println(id + "任务可能发生丢包：" + msg);
			System.err.println("失败，设置中断状态");
			isBroken=true;*/
		}else if(msg.indexOf("Qavg: 1180" )!= -1){
			errCont(id + "任务可能发生丢包：" + msg);
		}else if(msg.indexOf("Unknown error" )!= -1){
			errCont(id + "任务可能发生故障：" + msg);
		} else {

			isBroken=false;
			if (CommandManager.config.isDebug()){
				System.err.println(id + "消息：" + msg);
			}
		}

	}
	@Override
	public boolean isbroken() {
		return isBroken;
	}
	
}
