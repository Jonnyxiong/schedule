package com.ucpaas.sms.service.account;

import com.jsmsframework.common.dto.ResultVO;
import com.jsmsframework.user.entity.JsmsClientInfoExt;
import com.jsmsframework.user.service.JsmsClientInfoExtService;
import com.ucpaas.sms.constant.LogConstant.LogType;
import com.ucpaas.sms.dao.MessageMasterDao;
import com.ucpaas.sms.enums.LogEnum;
import com.ucpaas.sms.exception.ClientInfoException;
import com.ucpaas.sms.exception.SmspBusinessException;
import com.ucpaas.sms.model.PageContainer;
import com.ucpaas.sms.service.CommonSeqService;
import com.ucpaas.sms.service.LogService;
import com.ucpaas.sms.util.ConfigUtils;
import com.ucpaas.sms.util.StrUtils;
import com.ucpaas.sms.util.cache.StaticInitVariable;
import com.ucpaas.sms.util.rest.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 账户管理
 *
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

	@Autowired
	private MessageMasterDao messageMasterDao;
	@Autowired
	private LogService logService;
	@Autowired
	private CommonSeqService commonSeqService;
	@Autowired
	private JsmsClientInfoExtService jsmsClientInfoExtService;
	
	
	@Override
	public PageContainer query(Map<String, String> params) {
		return messageMasterDao.getSearchPage("account.query", "account.queryCount", params);
	}
	
	@Override
	public Map<String, Object> view(String account, String smstype){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("smstype", smstype);
		Map<String, Object> check = messageMasterDao.getOneInfo("account.isClientChannelGroupAssign", params);
		Map<String, Object> resultMap = messageMasterDao.getOneInfo("account.getAccount", account);
		
		if(null != resultMap){
			String ip = Objects.toString(resultMap.get("ip"), "");
			if(ip.equals("*")){
				resultMap.put("ip", "");
			}
			
			if(check != null){
				resultMap.put("isClientIdAssign", 1);//子账号已经配置了通道组
			}else{
				resultMap.put("isClientIdAssign", 0);
			}
		}else{
			resultMap = new HashMap<String, Object>();
		}
		return resultMap;
	}
	
	@Override
	@Transactional
	public ResultVO updateClientLabel(Map<String, String> params, String oprate){
        String clientId = params.get("clientId");
        String clientLabel = params.get("clientLabel");
        if (StringUtils.isBlank(clientId)){
            return ResultVO.failure("请求超时，请稍后再试...");
        }
        JsmsClientInfoExt newModel = new JsmsClientInfoExt();
        newModel.setClientid(clientId);
        newModel.setClientLabel(Objects.toString(clientLabel,""));
        newModel.setUpdateDate(new Date());
        JsmsClientInfoExt oldModel = jsmsClientInfoExtService.getByClientId(clientId);
        ResultVO resultVO;
        if(oldModel == null){
            int insert = jsmsClientInfoExtService.insert(newModel);
            if (insert > 0){
                resultVO = ResultVO.successDefault();
            }else{
                resultVO = ResultVO.failure("添加失败");
            }
        }else{
            int update = jsmsClientInfoExtService.updateSelective(newModel);
            if (update > 0){
                resultVO = ResultVO.successDefault("修改成功");
            }else{
                resultVO = ResultVO.failure("修改失败");
            }
        }
		return resultVO;
	}

	@Override
	public Map<String, Object> save(Map params, String oprate) {
		LOGGER.debug("账户管理-账户管理，添加/修改：" + params);
		Map<String, Object> data = new HashMap<String, Object>();
		
		// 创建时clientid查重
		if("create".equals(oprate)){
			Map<String, Object> checkInfo = messageMasterDao.getOneInfo("account.checkAccount", params);
			if(checkInfo != null){
				data.put("result", "fail");
				data.put("msg", "账号已经存在，请重新输入");
				return data;
			}
		}
		
		params.put("status", "1");
		String ip = Objects.toString(params.get("ip"), "");
		if(ip.equals("")){
			params.put("ip", "*");
		}
		Date now = new Date();
		params.put("now",now);
		if(params.get("ext_value") == null){
		    params.put("ext_value",0);
        }
		int res;
		if( "create".equals(oprate)){
			String id = UUID.randomUUID().toString();
			params.put("id", id);
			res = messageMasterDao.insert("account.insertAccount", params);

			if(res > 0){
                int insert = messageMasterDao.insert("account.insertClientInfoExt", params);
                boolean updateClientIdStatus = commonSeqService.updateClientIdStatus((String) params.get("clientid"));
                if(insert < 1 && !updateClientIdStatus){
					throw new ClientInfoException("子账号信息添加失败,请刷新后再试...");
				}
				// 更新clientid序列表中的状态
                data.put("msg", "成功添加" + res + "个账户");
				data.put("result", "success");
				data.put("id", id);
				
				String agentId = (String) params.get("agent_id");
				String smsfrom = (String) params.get("smsfrom");
				String clientId = (String) params.get("clientid");
				if(isNeedGiveShorMessage(agentId, smsfrom)){
					giveShortMessage(clientId);
				}
			}else{
				data.put("msg", "添加失败");
				data.put("result", "fail");
				return data;
			}
			logService.add(LogType.add, LogEnum.账户管理.getValue(),"账户管理-账户管理，添加：", params, data);
		}else{
			res = messageMasterDao.update("account.updateAccount", params);
			if(res > 0){
				/* // todo 当前是修改 接口密码, accountMapper 中 Line307 
				int update = messageMasterDao.update("account.updateClientInfoExt", params);
				if(update < 1){
					throw new ClientInfoException("子账号信息更新失败,请刷新后再试...");
				}*/
				data.put("msg", "成功修改" + res + "个账户");
				data.put("result", "success");
			}else{
				data.put("msg", "修改失败");
				data.put("result", "fail");
				return data;
			}
			logService.add(LogType.update,LogEnum.账户管理.getValue(), "账户管理-账户管理，修改：", params, data);
		}
		
		
		String extendport = (String) params.get("extendport");
		String extendtype = (String) params.get("extendtype");
		String extendportOld = (String) params.get("extendport_old");
		String extendtypeOld = (String) params.get("extendtype_old");
		boolean isUpdateExtendport = true;
		if(StringUtils.isNotBlank(extendtypeOld)){
			if(!extendport.equals(extendportOld) || !extendtypeOld.equals(extendtype)){
				Map<String, Object> sqlParams = new HashMap<String, Object>();
				sqlParams.put("extendport", extendportOld);
				sqlParams.put("extendtype", extendtypeOld);
				messageMasterDao.update("account.updateExtendportReuseNum2", sqlParams);
				
				isUpdateExtendport = updateExtendportCurrentNum(extendport, extendtype);
			}
		}else{
			isUpdateExtendport = updateExtendportCurrentNum(extendport, extendtype);
		}
		if(isUpdateExtendport == false){
			data.put("msg", "保存时更新用户扩展端口失败");
			data.put("result", "fail");
			
			LOGGER.error("修改子账号时更新用户扩展端口失败");
		}
		
		return data;
	}
	
	@Override
	public Map<String, Object> updateStatus(Map<String, String> params){
		LOGGER.debug("账户管理-账户管理，更新状态：" + params);
		
		Map<String, Object> data = new HashMap<String, Object>();
		int res = messageMasterDao.update("account.updateStatus", params);
		if(res == 1){
			data.put("result", "success");
			data.put("msg", "成功更新账户状态");
		}else{
			data.put("result", "fail");
			data.put("msg", "更新账户状态失败");
		}
		logService.add(LogType.update,LogEnum.账户管理.getValue(), "账户管理-账户管理，更新状态：", params, data);
		return data;
	}
	
	@Override
	public Map<String, Object> isClientChannelGroupAssign(Map<String, String> params){
		return messageMasterDao.getOneInfo("account.isClientChannelGroupAssign", params);
	}

	@Override
	public Map<String, Object> getIdentifyByClientId(Map<String, String> params) {
		if(null != params && params.get("clientId") != null){
			return messageMasterDao.getOneInfo("account.getIdentifyByClientId", params);
		}
		
		return new HashMap<String, Object>();
	}

	@Override
	public Map<String, Object> getExtendPortByExtendType(Map<String, String> formData) {
		Map<String, Object> extendPortInfo = messageMasterDao.getOneInfo("account.getExtendPortByType", formData);
		if(extendPortInfo != null){
			String reusenumber = Objects.toString(extendPortInfo.get("reusenumber"), "");
			
			// reusenumber中保存的是clientid注销后释放出来的用户扩展端口，多个时以“,”分割
			// 如果reusenumber中存在值则优先使用里面的扩展端口
			if(StringUtils.isNotBlank(reusenumber)){
				String[] reusenumberArray = reusenumber.split(",");
				String currentnumber = reusenumberArray[0];
				extendPortInfo.put("currentnumber", currentnumber);
			}
			return extendPortInfo;
		}else{
			return new HashMap<String, Object>();
		}
	}
	
	/**
	 * 更新用户端口分配表
	 * @param extendport
	 * @param extendtype
	 * @return
	 */
	private synchronized boolean updateExtendportCurrentNum(String extendport, String extendtype){
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		Map<String, Object> extendPortInfo = messageMasterDao.getOneInfo("account.getExtendPortByType", extendtype);
		String currentnumber = extendPortInfo.get("currentnumber").toString();
		Integer currentnumberIntValue = Integer.valueOf(currentnumber);
		String endnumber = extendPortInfo.get("endnumber").toString();
		Integer endnumberIntValue = Integer.valueOf(endnumber);
		String reusenumber = Objects.toString(extendPortInfo.get("reusenumber"), "");
		
		// 使用的端口（extendport）在重用端口（reusenumber）中存在时更新重用端口
		if(reusenumber.indexOf(extendport) != -1){
			if(reusenumber.equals(extendport)){
				reusenumber = "";
			}else{
				reusenumber = reusenumber.replace(extendport + ",", "");
			}
			sqlParams.put("reusenumber", reusenumber);
			sqlParams.put("extendtype", extendtype);
			int i = messageMasterDao.update("account.updateExtendportReuseNum", sqlParams);
			if (i < 0) {
				LOGGER.debug("扩展端口类型 extendtype ={}， 更新 reusenumber失败，extendport = {}", extendtype, extendport);
				return false;
			}
			LOGGER.debug("扩展端口类型 extendtype = {}，更新 reusenumber成功，extendport = {}", extendtype, extendport);
		}else if((currentnumberIntValue <= endnumberIntValue) && extendport.equals(currentnumber)){
			// 扩展端口分配表currentnumber + 1;
			currentnumberIntValue = currentnumberIntValue + 1;
			sqlParams.put("extendtype", extendtype);
			int i = messageMasterDao.update("account.updateExtendportCurrentNum", sqlParams);
			if (i < 0) {
				LOGGER.debug("扩展端口类型 extendtype ={} currentNum加1失败", extendtype);
				return false;
			}
			LOGGER.debug("扩展端口类型 extendtype = {}，currentnumber加1后 = {} ", extendtype, currentnumberIntValue);
		}else{
			LOGGER.debug("更新扩展端口信息失败 extendtype = {}，extendport = {}，端口已经被使用", extendtype, extendport);
			throw new SmspBusinessException("当前用户扩展端口已经被占用，请重新分配端口");
		}
		
		// currentnumber 大于 endnumber 且重用端口为空的时候当前类型的端口不可用了
		if((currentnumberIntValue > endnumberIntValue) && StringUtils.isBlank(reusenumber)){ 
			// 修改当前扩展端口类型状态为禁用
			sqlParams.put("status", 1);
			sqlParams.put("extendtype", extendtype);
			int i = messageMasterDao.update("account.updateExtendtypeStatus", sqlParams);
			if (i < 0) {
				LOGGER.debug("msg", "修改扩展端口类型extendtype = {} 状态失败", extendtype);
				return false;
			}
			LOGGER.debug("扩展端口类型 extendtype = {}，已经分配完，状态修改为禁用", extendtype);
		}
		
		return true;
	}
	

	@Override
	public Map<String, Object> validateAgentIdType(Map<String, String> formData) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> sqlParam = new HashMap<String, Object>();
		String agentIdOld = Objects.toString(formData.get("agent_id_old"), "");
		String agentId = Objects.toString(formData.get("agent_id"), "");
		
		sqlParam.put("agent_id", agentIdOld);
		String agentTypeOld = messageMasterDao.getOneInfo("account.queryAgentType", agentIdOld);
		sqlParam.put("agent_id", agentId);
		String agentType = messageMasterDao.getOneInfo("account.queryAgentType", agentId);
		if(agentType == null && agentTypeOld == null){
			result.put("result", "success");
		}else if(agentType.equals(agentTypeOld)){
			result.put("result", "success");
		}else{
			result.put("result", "fail");
		}
		return result;
	}

	@Override
	public Map<String, Object> getAgentBelongSale(String agentId) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 查询代理商的所属销售
		String agentBelongSale = messageMasterDao.getOneInfo("account.getAgentBelongSale", agentId);
		result.put("agentBelongSale", Objects.toString(agentBelongSale, ""));
		return result;
	}

    @Override
    public Map<String, Object> getOneByClientId(String clientId) {
        return messageMasterDao.getOneInfo("account.getAccount", clientId);
    }

    /**
	 * 判断是否可以赠送测试短信
	 * @param agentId
	 * @param smsfrom
	 * @return
	 */
	private boolean isNeedGiveShorMessage(String agentId, String smsfrom){
		
		if(StringUtils.isBlank(agentId) || StringUtils.isBlank(smsfrom)){
			return false;
		}
		
		String agentType = messageMasterDao.getOneInfo("account.getAgentType", agentId);
		
		// OEM代理商用户&短信协议是非HTTPS协议的时候可以赠送短信
		if("5".equals(agentType) && !"6".equals(smsfrom)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * OEM代理商&非HTTP协议用户进行赠送测试短信
	 * @param client_id
	 */
	private void giveShortMessage(String client_id){

		LOGGER.debug("测试短信赠送,方法为--->{},参数为---->{}","GiveShortMessage",client_id);

		Map<String,Object> clientIdParams = new HashMap<>();
		clientIdParams.put("clientid",client_id);

		String agent_id = messageMasterDao.getOneInfo("account.getAgentIdByClientId",clientIdParams);

		Map<String,Object> agentIdParams = new HashMap<>();
		agentIdParams.put("agent_id",agent_id);

		int agent_type = messageMasterDao.getOneInfo("account.getAgentTypeByAgentId",agentIdParams);

		//品牌代理/销售代理的客户,不赠送测试短信(只有OEM代理商赠送短信)
		if(agent_type != 5){
			LOGGER.debug("代理商类型为--->{}，不是oem代理商，不能赠送短信",agent_type);
			return;
		}

		//不赠送短信产品
		Map<String,Object> oemDataMap = messageMasterDao.getOneInfo("account.getOemDataConfig",agentIdParams);

		if(oemDataMap == null || oemDataMap.get("id") == null){
			LOGGER.debug("代理商：{}------->没有对应的oem资料",agent_id);
			return;
		}

		String oemDataId = oemDataMap.get("id").toString();

		if(oemDataMap.get("test_product_id") == null || oemDataMap.get("test_sms_number") == null || "0".equals(oemDataMap.get("test_sms_number").toString())){
			LOGGER.debug("oemid:{}资料----->为空、或者条数--->为空、或者-->条数",oemDataId);
			return;
		}

		String test_product_id = oemDataMap.get("test_product_id").toString();
		String test_sms_number = oemDataMap.get("test_sms_number").toString();
		//测试产品包已经下架或者已经过期
		Map<String,Object> productIdMap = new HashMap<>();
		productIdMap.put("product_id",test_product_id);

		Map<String,Object> oemProductInfoMap = messageMasterDao.getOneInfo("account.getOemProductInfoByProductId",productIdMap);
		int productStatus = (int) oemProductInfoMap.get("status"); //状态，0：待上架，1：已上架，2：已下架
		String unit_price = oemProductInfoMap.get("unit_price").toString();
		String product_type = oemProductInfoMap.get("product_type").toString();//产品类型,0:行业,1:营销,2:国际
		String due_time = oemProductInfoMap.get("due_time").toString();
		String product_id = oemProductInfoMap.get("product_id").toString();
		String product_code = oemProductInfoMap.get("product_code").toString();
		String product_name = oemProductInfoMap.get("product_name").toString();
		String operator_code = oemProductInfoMap.get("operator_code").toString();
		String area_code = oemProductInfoMap.get("area_code").toString();

		if(productStatus != 1){
			LOGGER.debug("产品id:{}--------->为待上架或者已下架",test_product_id);
			return;
		}

		
		//满足赠送短信的条件
		LOGGER.debug("满足赠送短信的条件=============================================================");

		Date now = new Date();

		Map<String,Object> agentAccount = messageMasterDao.getOneInfo("account.getAgentAccountByAgentId",agentIdParams);
		String balance = agentAccount.get("balance").toString();
		BigDecimal oldBgBalance = new BigDecimal(balance);

		BigDecimal bgTestNum = new BigDecimal(test_sms_number);
		BigDecimal bgUnitPrice = new BigDecimal(unit_price);
		BigDecimal bgAmount = bgTestNum.multiply(bgUnitPrice);

		BigDecimal newBgBalance = oldBgBalance.add(bgAmount);

		//生成余额账单(代理商入账)
		Map<String,Object> agentBalanceBillParams = new HashMap<>();
		agentBalanceBillParams.put("id",null);
		agentBalanceBillParams.put("agent_id",agent_id);
		agentBalanceBillParams.put("payment_type","5");//业务类型，0：充值，1：扣减，2：佣金转余额，3：购买产品包，4：退款，5：赠送
		agentBalanceBillParams.put("financial_type","0");//财务类型，0：入账，1：出账
		agentBalanceBillParams.put("amount",bgAmount.toString());

		agentBalanceBillParams.put("balance",newBgBalance.toString());
		agentBalanceBillParams.put("create_time",now);
		agentBalanceBillParams.put("order_id",null);   //充值操作订单id为null
		agentBalanceBillParams.put("admin_id",0);
		agentBalanceBillParams.put("client_id",client_id);
		agentBalanceBillParams.put("remark","赠送短信充值");

		int i = messageMasterDao.insert("account.createAgentBalanceBill",agentBalanceBillParams);
		if(i <= 0){
//			throw new OperationException("赠送短信，生成余额入账账单失败");
			throw new RuntimeException("赠送短信，生成余额入账账单失败");
		}

		//判断OEM代理商短信池(t_sms_oem_agent_pool)是否存在记录(获取agent_pool_id)
		String agent_pool_id = null;

		Map<String,Object> agentPoolParams = new HashMap<>();
		agentPoolParams.put("agent_id",agent_id);
		agentPoolParams.put("product_type",product_type); //产品类型，0：行业，1：营销，2：国际
		agentPoolParams.put("due_time",due_time); //到期时间
		agentPoolParams.put("unit_price",unit_price);
		agentPoolParams.put("status","0"); //状态，0：正常，1：停用
		agentPoolParams.put("operator_code", operator_code);
		agentPoolParams.put("area_code", area_code);

		Map<String,Object> params = messageMasterDao.getOneInfo("account.getAgentPoolIdByCondition",agentPoolParams);
		if(params != null && params.get("agent_pool_id") != null){
			agent_pool_id = params.get("agent_pool_id").toString();
		}else{

			Map<String,Object> agentPoolMap = new HashMap<>();
			agentPoolMap.put("agent_pool_id",null);
			agentPoolMap.put("agent_id",agent_id);
			agentPoolMap.put("product_type",product_type);
			agentPoolMap.put("operator_code",operator_code);
			agentPoolMap.put("area_code",area_code);
			agentPoolMap.put("due_time",due_time);
			agentPoolMap.put("status","0"); //状态，0：正常，1：停用
			agentPoolMap.put("remain_number","0");
			agentPoolMap.put("unit_price",unit_price);
			agentPoolMap.put("remain_amount",null);
			agentPoolMap.put("update_time",now);
			agentPoolMap.put("remark",null);

			int j = messageMasterDao.insert("account.createOemAgentPool",agentPoolMap);
			if(j <= 0){
//				throw new OperationException("生成代理商短信池记录失败！");
				throw new RuntimeException("生成代理商短信池记录失败！");
			}
			agent_pool_id = agentPoolMap.get("agent_pool_id").toString();
		}

		//生成代理商订单(购买记录)
		Map<String,Object> oemAgentOrderMapForBuy = new HashMap<>();

		String orderId = this.getOemAgentOrderId().toString();
		oemAgentOrderMapForBuy.put("order_id",orderId);
		oemAgentOrderMapForBuy.put("order_no",orderId);
		oemAgentOrderMapForBuy.put("order_type",0); //订单类型，0：OEM代理商购买，1：OEM代理商分发，2：OEM代理商回退
		oemAgentOrderMapForBuy.put("product_id",product_id);
		oemAgentOrderMapForBuy.put("product_code",product_code);

		oemAgentOrderMapForBuy.put("product_type",product_type);
		oemAgentOrderMapForBuy.put("operator_code",operator_code);
		oemAgentOrderMapForBuy.put("area_code",area_code);
		oemAgentOrderMapForBuy.put("product_name",product_name);
		oemAgentOrderMapForBuy.put("unit_price",unit_price);
		oemAgentOrderMapForBuy.put("order_number",test_sms_number); //赠送的短信条数
		oemAgentOrderMapForBuy.put("order_amount",bgAmount.toString());

		oemAgentOrderMapForBuy.put("product_price","0");
		oemAgentOrderMapForBuy.put("agent_id",agent_id);
		oemAgentOrderMapForBuy.put("client_id",client_id);
		oemAgentOrderMapForBuy.put("name","云之讯");
		oemAgentOrderMapForBuy.put("agent_pool_id",agent_pool_id);

		oemAgentOrderMapForBuy.put("due_time",due_time);
		oemAgentOrderMapForBuy.put("create_time",now);
		oemAgentOrderMapForBuy.put("remark",null);

		int k = messageMasterDao.insert("account.insertOemAgentOrder",oemAgentOrderMapForBuy);
		if(k <= 0){
			throw new RuntimeException("生成代理商订单（购买记录）失败！");
		}

		//生成余额账单(代理商出账)
		Map<String,Object> agentBalanceBillOutParams = new HashMap<>();
		agentBalanceBillOutParams.put("id",null);
		agentBalanceBillOutParams.put("agent_id",agent_id);
		agentBalanceBillOutParams.put("payment_type","3");//业务类型，0：充值，1：扣减，2：佣金转余额，3：购买产品包，4：退款，5：赠送
		agentBalanceBillOutParams.put("financial_type","1");//财务类型，0：入账，1：出账
		agentBalanceBillOutParams.put("amount",bgAmount.toString());

		agentBalanceBillOutParams.put("balance",oldBgBalance.toString());
		agentBalanceBillOutParams.put("create_time",now);
		agentBalanceBillOutParams.put("order_id",orderId);
		agentBalanceBillOutParams.put("admin_id",0);
		agentBalanceBillOutParams.put("client_id",client_id);
		agentBalanceBillOutParams.put("remark","赠送短信充值");

		int m = messageMasterDao.insert("account.createAgentBalanceBill",agentBalanceBillOutParams);
		if(m <= 0){
			throw new RuntimeException("赠送短信，生成余额出账账单失败");
		}

		//======================================给客户充值===========================================

		//生成代理商订单(分发记录)
		Map<String,Object> oemAgentOrderMapForDistribute = new HashMap<>();
		
		String realName = messageMasterDao.getOneInfo("account.getRealName", clientIdParams);

		String orderId2 = this.getOemAgentOrderId().toString();
		oemAgentOrderMapForDistribute.put("order_id",orderId2);
		oemAgentOrderMapForDistribute.put("order_no",orderId2);
		oemAgentOrderMapForDistribute.put("order_type",1); //订单类型，0：OEM代理商购买，1：OEM代理商分发，2：OEM代理商回退
		oemAgentOrderMapForDistribute.put("product_id",product_id);
		oemAgentOrderMapForDistribute.put("product_code",product_code);

		oemAgentOrderMapForDistribute.put("product_type",product_type);
		oemAgentOrderMapForDistribute.put("operator_code",operator_code);
		oemAgentOrderMapForDistribute.put("area_code",area_code);
		oemAgentOrderMapForDistribute.put("product_name",product_name);
		oemAgentOrderMapForDistribute.put("unit_price",unit_price);
		oemAgentOrderMapForDistribute.put("order_number",test_sms_number); //赠送的短信条数
		oemAgentOrderMapForDistribute.put("order_amount",bgAmount.toString());

		oemAgentOrderMapForDistribute.put("product_price","0");
		oemAgentOrderMapForDistribute.put("agent_id",agent_id);
		oemAgentOrderMapForDistribute.put("client_id",client_id);
		oemAgentOrderMapForDistribute.put("name",realName);
		oemAgentOrderMapForDistribute.put("agent_pool_id",agent_pool_id);

		oemAgentOrderMapForDistribute.put("due_time",due_time);
		oemAgentOrderMapForDistribute.put("create_time",now);
		oemAgentOrderMapForDistribute.put("remark",null);

		int n = messageMasterDao.insert("account.insertOemAgentOrder",oemAgentOrderMapForDistribute);
		if(n <= 0){
			throw new RuntimeException("生成代理商订单（分发记录）失败！");
		}

		//判断oem客户短信池是否存在记录(获取client_pool_id)

		//判断OEM代理商短信池(t_sms_oem_agent_pool)是否存在记录(获取agent_pool_id)
		String client_pool_id = null;

		Map<String,Object> clientPoolParams = new HashMap<>();
		clientPoolParams.put("client_id",client_id);
		clientPoolParams.put("product_type",product_type); //产品类型，0：行业，1：营销，2：国际
		clientPoolParams.put("due_time",due_time); //到期时间
		clientPoolParams.put("unit_price",unit_price);
		clientPoolParams.put("status","0"); //状态，0：正常，1：停用
		clientPoolParams.put("operator_code",operator_code);
		clientPoolParams.put("area_code",area_code);

		Map<String,Object> clientPoolIdMap = messageMasterDao.getOneInfo("account.getClientPoolIdByCondition",clientPoolParams);
		if(clientPoolIdMap != null && clientPoolIdMap.get("client_pool_id") != null){
			client_pool_id = clientPoolIdMap.get("client_pool_id").toString();

			Map<String,Object> updateClientPoolMap = new HashMap<>();
			updateClientPoolMap.put("client_pool_id",client_pool_id);
			updateClientPoolMap.put("test_num",test_sms_number);

			int o = messageMasterDao.update("account.updateClientPoolByCondition",updateClientPoolMap);
			if( o <= 0){
				throw new RuntimeException("更新客户短信池的测试条数失败！");
			}

		}else{

			Map<String,Object> clientPoolMap = new HashMap<>();
			clientPoolMap.put("client_pool_id",null);
			clientPoolMap.put("client_id",client_id);
			clientPoolMap.put("product_type",product_type);
			clientPoolMap.put("due_time",due_time);
			clientPoolMap.put("status",0); //状态，0：正常，1：停用

			clientPoolMap.put("total_number",test_sms_number);
			clientPoolMap.put("remain_number",test_sms_number);
			clientPoolMap.put("unit_price",unit_price);
			clientPoolMap.put("total_amount",0);
			clientPoolMap.put("remain_amount",0);
			clientPoolMap.put("operator_code",operator_code);
			clientPoolMap.put("area_code",area_code);

			clientPoolMap.put("remain_test_number",test_sms_number);
			clientPoolMap.put("update_time",now);
			clientPoolMap.put("remark",null);

			int p = messageMasterDao.insert("account.createOemClientPool",clientPoolMap);
			if(p <= 0){
				throw new RuntimeException("生成客户短信池失败！");
			}

			client_pool_id = clientPoolMap.get("client_pool_id").toString();
		}

		//给客户订单增加分发记录(生成oem客户订单)
		Map<String,Object> oemClientOrderMap = new HashMap<>();
		String oemClientOrderId = getClientOrderId().toString();

		oemClientOrderMap.put("order_id",oemClientOrderId);
		oemClientOrderMap.put("order_no",oemClientOrderId);
		oemClientOrderMap.put("product_type",product_type); //产品类型，0：行业，1：营销，2：国际
		oemClientOrderMap.put("order_type","1"); //订单类型，1：OEM代理商分发，2：OEM代理商回退
		oemClientOrderMap.put("order_number",test_sms_number);//赠送的短信条数

		oemClientOrderMap.put("unit_price",unit_price);
		oemClientOrderMap.put("order_price",bgAmount.toString());
		oemClientOrderMap.put("client_id",client_id);
		oemClientOrderMap.put("agent_id",agent_id);
		oemClientOrderMap.put("client_pool_id",client_pool_id);

		oemClientOrderMap.put("due_time",due_time);
		oemClientOrderMap.put("create_time",now);
		oemClientOrderMap.put("remark",null);
		oemClientOrderMap.put("operator_code",operator_code);
		oemClientOrderMap.put("area_code",area_code);

		int q = messageMasterDao.insert("account.insertOemClientOrder",oemClientOrderMap);
		if(q <= 0){
			throw new RuntimeException("生成oem客户订单失败！");
		}

	}
	
	
	//组装表t_sms_oem_agent_order的orderID
	private synchronized Long getOemAgentOrderId(){

		Date date = new Date();
		int num = 0;
		String orderIdPre = DateUtil.dateToStr(date,"yyMMdd") + DateUtil.dateToStr(date, "HHmm")
				+ ConfigUtils.platform_oem_agent_order_identify;// 运营平台下单标识，oem代理商订单标识4
		if(orderIdPre.equals(StaticInitVariable.OEM_AGENT_ORDERID_PRE)){
			num = StaticInitVariable.OEM_AGENT_ORDER_NUM;
			StaticInitVariable.OEM_AGENT_ORDER_NUM = num + 1;
		}else{
			StaticInitVariable.OEM_AGENT_ORDERID_PRE = orderIdPre;
			num = 1;
			StaticInitVariable.OEM_AGENT_ORDER_NUM = num + 1;
		}

		//拼成订单号
		String orderIdStr = orderIdPre + StrUtils.addZeroForNum(num, 4, "0");
		Long orderId = Long.valueOf(orderIdStr);

		System.out.println("生成的代理商orderId------------->"+orderId);
		LOGGER.debug("生成的代理商orderId------------->"+orderId);

		return orderId;
	}

	//组装表t_sms_oem_client_order的orderID
	private synchronized Long getClientOrderId(){

		Date date = new Date();
		int num = 0;
		String orderIdPre = DateUtil.dateToStr(date,"yyMMdd") + DateUtil.dateToStr(date, "HHmm")
				+ ConfigUtils.platform_oem_agent_order_identify;// oem代理商订单标识3

		if(orderIdPre.equals(StaticInitVariable.OEM_CLIENT_ORDERID_PRE)){
			num = StaticInitVariable.OEM_CLIENT_ORDER_NUM;
			StaticInitVariable.OEM_CLIENT_ORDER_NUM = num + 1;
		}else{
			StaticInitVariable.OEM_CLIENT_ORDERID_PRE = orderIdPre;
			num = 1;
			StaticInitVariable.OEM_CLIENT_ORDER_NUM = num + 1;
		}

		//拼成订单号
		String orderIdStr = orderIdPre + StrUtils.addZeroForNum(num, 4, "0");
		Long orderId = Long.valueOf(orderIdStr);

		System.out.println("生成的客户的订单id:=========="+orderId);
		LOGGER.debug("生成的客户的订单id:=========="+orderId);

		return orderId;
	}

}
