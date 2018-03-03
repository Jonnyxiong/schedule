package com.ucpaas.sms.constant;

/**
 * 任务常量
 * 
 * @author xiejiaan
 */
public class TaskConstant {

	/**
	 * 任务id
	 */
	public enum TaskId {
		/**
		 * 每日建表任务（ucpaas_smsp库）
		 */
		task_id_1(1),
		
		/**
		 * 每日清理历史数据（ucpaas_smsp库）
		 */
		task_id_2(2),
		
		/**
		 * 每3分钟处理短信记录区域统计表（t_sms_channel_area_statistics_yyyyMMdd）
		 */
		task_id_3(3),
		
		/**
		 * 每日处理短信记录日统计表（t_sms_record_area_day_statistics）
		 */
		task_id_4(4),
		
		/**
		 * 每1分钟处理预警任务
		 */
		task_id_5(5),
		
		/**
		 * 每日建表任务（ucpaas_midlog库）
		 */
		task_id_6(6),
		/**
		 * 每日清理历史数据（ucpaas_midlog库）
		 */
		task_id_7(7),
		
		/**
		 * 每3分钟处理验证统计实时表（t_verify_monitor_statistics）
		 */
		task_id_8(8),
		
		/**
		 * 每日处理验证统计日表（t_verify_day_statistics）
		 */
		task_id_9(9);
		
		private int value;

		TaskId(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

}
