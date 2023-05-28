package cn.td.aiot.decoder.utils.commandManager.handler;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import cn.td.aiot.decoder.utils.commandManager.TdFFmpegManagerImpl;
import cn.td.aiot.decoder.utils.commandManager.data.CommandTasker;
import cn.td.aiot.decoder.utils.commandManager.data.TaskDao;
import cn.td.aiot.decoder.utils.commandManager.util.ExecUtil;
import cn.td.aiot.system.domain.SysLog;

/**
 * 任务保活处理器（一个后台保活线程，用于处理异常中断的持久任务）
 *
 *
 */
public class KeepAliveHandler extends Thread{
	/**待处理队列*/
	private static Queue<String> queue=null;
	
	public int err_index=0;//错误计数
	
	public volatile int stop_index=0;//安全停止线程标记
	
	/** 任务持久化器*/
	private TaskDao taskDao = null;
	
	public KeepAliveHandler(TaskDao taskDao) {
		super();
		this.taskDao=taskDao;
		queue=new ConcurrentLinkedQueue<>();
	}

	public static void add(String id ) {
		if(queue!=null) {
			queue.offer(id);
		}
	}
	
	public boolean stop(Process process) {
		if (process != null) {
			process.destroy();
			return true;
		}
		return false;
	}
	
	@Override
	public void run() {
		System.err.println("保活启动");
		for(;stop_index==0;) {
			if(queue==null) {
				continue;
			}
			String id=null;
			CommandTasker task=null;
			
			try {
				while(queue.peek() != null) {
					System.err.println("准备重启任务："+queue);
					id=queue.poll();
					task=taskDao.get(id);
					//重启任务
					ExecUtil.restart(task);
					//建立观察体系
					new ObservationTask(task).start();
				}
			}catch(IOException e) {
                System.err.println(id+" 任务重启失败，详情："+task);
                //重启任务失败
                err_index++;
			}catch(Exception e) {
				
			}
		}
	}

	
	@Override
	public void interrupt() {
		stop_index=1;
	}

	public class ObservationTask extends Thread{
		/** 任务持久化器*/
		private CommandTasker commandTasker = null;

		public ObservationTask(CommandTasker commandTasker){
			super();
			this.commandTasker = commandTasker;
		}

		@Override
		public void run() {
			//超时次数限制
			for (int i = 0 ; i<3 ; i++){
				try {
					sleep(10000);
					if(commandTasker.getThread() != null && commandTasker.getThread().isAlive()){
						if (i>=2){

							System.err.println("没救了退出");
							TdFFmpegManagerImpl.getInstance().stop(commandTasker.getId());
							return;
						}
						System.err.println("观察：" + commandTasker.getId());
						if (System.currentTimeMillis()-commandTasker.getThread().getOutTime() > 5000){
							System.err.println("重启并再次观察：" + commandTasker.getId());
							//保活确认为超时继续重启
							ExecUtil.restart(commandTasker);
							//再次观察
//							new ObservationTask(commandTasker).start();
						}else {
							System.err.println("线程正常退出观察：" + commandTasker.getId());
							break;
						}
					}

				} catch (InterruptedException | IOException e) {
                    System.err.println(commandTasker.getId()+" 任务重启失败，详情："+commandTasker);
                    err_index++;
                }
			}




		}
	}
}
