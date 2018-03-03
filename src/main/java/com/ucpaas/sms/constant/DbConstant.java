package com.ucpaas.sms.constant;

/**
 * 数据库常量
 * 
 * @author xiejiaan
 */
public class DbConstant {
	
	/**
	 * 数据库类型
	 * 
	 */
	public enum DbType {
		/**
		 * 短信业务 主库
		 */
		ucpaas_message,
		
		/**
		 * ACCESS流水库  主库
		 * ucpaas_message_statistics(即ucpaas_message_access)
		 */
		ucpaas_message_access_master,
		
		/**
		 * REOCRD流水库 主库
		 */
		ucpaas_message_record_master,
		
		/**
		 * ACCESS流水库 从库
		 * ucpaas_message_statistics(即ucpaas_message_access)
		 */
		ucpaas_message_access_slave,
		
		/**
		 * REOCRD流水库 从库
		 */
		ucpaas_message_record_slave,
		
		/**
		 * 统计流水库 主库
		 */
		ucpaas_message_stats_master,
		
		/**
		 * 通道测试数据库主库
		 */
		smsp_message_test_master;
		
		public static DbType getInstance(int value){
			switch (value) {
			case 1:
				return ucpaas_message;
			case 2:
				return ucpaas_message_access_master;
			case 3:
				return ucpaas_message_record_master;
			case 4:
				return ucpaas_message_access_slave;
			case 5:
				return ucpaas_message_record_slave;
			case 6:
				return ucpaas_message_stats_master;
			case 7: // 这个顺序不能修改，否则会影响页面 select 组件
				return smsp_message_test_master;
			default:
				return null;
			}
		}
		 
	}

	/**
	 * 表格前缀
	 * 
	 * @author xiejiaan
	 */
	public enum TablePrefix {
		/**
		 * 短信记录流水表（t_sms_record_yyyyMMdd）
		 */
		t_sms_record_,
		
		/**
		 * 短信记录区域统计表（t_sms_record_area_statistics_yyyyMMdd）
		 */
		t_sms_record_area_statistics_,
		
		/**
		 * 短信接收表（t_sms_access_yyyyMMdd）
		 */
		t_sms_access_,
		
		/**
		 * 短信账号发送速率统计表（t_sms_client_speed_stat_yyyyMMdd）
		 */
		t_sms_client_speed_stat_,
		
		/**
		 * 短信通道实时质量统计表（t_sms_channel_indexes_stat_yyyyMMdd）
		 */
		t_sms_channel_indexes_stat_,
		
		/**
		 * 短信通道历史质量统计表（t_sms_channel_indexes_stat_history）
		 */
		t_sms_channel_indexes_stat_history,
		
		/**
		 * 通道状态统计表（t_sms_channel_status_graph_stat_yyyyMMdd）
		 */
		t_sms_channel_status_graph_stat_,
		
		/**
		 * 通道错误分析表（t_sms_channel_error_stat_yyyyMMdd）
		 */
		t_sms_channel_error_stat_,
		
		/**
		 * ACCESS队列消息数统计（t_sms_access_queue_stat_yyyyMMdd）
		 */
		t_sms_access_queue_stat_,
		
		/**
		 * 客户质量指数统计表（t_sms_client_indexes_stat_yyyyMMdd）
		 */
		t_sms_client_indexes_stat_;

	}
	
}
