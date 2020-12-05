package com.dity.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dity.common.SysProperties;
import com.dity.common.bootonfig.UserLoginToken;
import com.dity.common.utils.IDUtils;
import com.dity.common.utils.SessionUtil;
import com.dity.service.DityService;
import com.dity.service.FeePerfMgtService;
import com.dity.service.MobileService;

import net.coobird.thumbnailator.Thumbnails;

@CrossOrigin
@Controller
@RequestMapping("/dity/mobile")
@UserLoginToken
public class MobileController {
	
	private static final Logger logger = LoggerFactory.getLogger(MobileController.class);
	
	@Autowired
	MobileService mobileService;
	
	@Autowired
	DityService dityService;
	
	@Autowired
	FeePerfMgtService feePerfMgtService;
	
    @RequestMapping(value = "/getUserBySession", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public Object getUserBySession(){
        return SessionUtil.getUser();
    }
    
    @RequestMapping(value = "/uploadUserImg", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Object uploadLbtImg(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "file", required = true) MultipartFile img) throws IOException{
		Map<String, Object> map = new HashMap<>();
		map.put("ID",id);
		String url = uplodFile2LocalPath(img);
		if("1".equals(type)) {
			map.put("TX_FILE_URL",url);
		}else if("2".equals(type)) {
			map.put("WX_FILE_URL",url);
		}else if("3".equals(type)) {
			map.put("ZFB_FILE_URL",url);
		}
		dityService.editUser(map);
		map.put("O_DATA",url);
		map.put("O_RUNSTATUS", 1);
		map.put("O_MSG","success");
    	return map;
	}
    
    @RequestMapping(value = "/qryRottnChrt", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String,Object> qryRottnChrt(){
    	Map<String,Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			list = dityService.qryRottnChrt(map);
			map.put("O_DATA",list);
			map.put("O_RUNSTATUS",1);
			map.put("O_MSG","success");
		} catch (Exception e) {
			logger.error("/qryRottnChrt:"+map,e);
			map.put("O_RUNSTATUS",-1);
			map.put("O_MSG","system error");
		}
 		return map;
	}
    
    @RequestMapping(value = "/getUserByNo", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Object getUserByNo(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "USER_NO", required = true) String no){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("USER_NO",no);
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			list = dityService.getUserByNo(map);
			if(list.size()>0){
				map.put("O_DATA",list.get(0));
				map.put("O_RUNSTATUS",1);
				map.put("O_MSG","success");
			} else {
				map.put("O_RUNSTATUS",0);
				map.put("O_MSG","用户不存在！");
			}
		} catch (Exception e) {
			logger.error("getUserByNo:"+map,e);
			map.put("O_RUNSTATUS",-1);
			map.put("O_MSG","system error");
		}
		return map;
	}
    
    @RequestMapping("/editUser")
	@ResponseBody
    public Object editUser(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = true) String id,
            @RequestParam(value = "USER_NO", required = false) String USER_NO,
            @RequestParam(value = "PASS", required = false) String PASS,
            @RequestParam(value = "USER_NAME", required = false) String USER_NAME,
            @RequestParam(value = "MOBILE_NO", required = false) String MOBILE_NO,
            @RequestParam(value = "USER_AGE", required = false) String USER_AGE,
            @RequestParam(value = "USER_BIRTH", required = false) String USER_BIRTH,
            @RequestParam(value = "BANK_NO", required = false) String BANK_NO,
            @RequestParam(value = "BANK_NAME", required = false) String BANK_NAME,
            @RequestParam(value = "REAL_NAME", required = false) String REAL_NAME,
            @RequestParam(value = "WX_NO", required = false) String WX_NO,
            @RequestParam(value = "ZFB_NO", required = false) String ZFB_NO,
            @RequestParam(value = "WX_FILE_URL", required = false) String WX_FILE_URL,
            @RequestParam(value = "ZFB_FILE_URL", required = false) String ZFB_FILE_URL,
            @RequestParam(value = "TX_FILE_URL", required = false) String TX_FILE_URL) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("ID",id);
        	map.put("USER_NO",USER_NO);
        	if(StringUtils.isNotBlank(USER_NO)) {
        		List<Map<String, Object>> list= dityService.getUserByNo(map);
            	if(list.size()>0) {
            		map.put("O_RUNSTATUS", -1);
                	map.put("O_MSG", "账号重复，请重新输入！");
                	return map;
            	}
        	}
        	map.put("PASS",PASS);
        	map.put("USER_NAME",USER_NAME);
        	map.put("MOBILE_NO",MOBILE_NO);
        	map.put("USER_AGE",USER_AGE);
        	map.put("USER_BIRTH",USER_BIRTH);
        	map.put("BANK_NO",BANK_NO);
        	map.put("BANK_NAME",BANK_NAME);
        	map.put("REAL_NAME",REAL_NAME);
        	map.put("WX_NO",WX_NO);
        	map.put("ZFB_NO",ZFB_NO);
        	map.put("WX_FILE_URL",WX_FILE_URL);
        	map.put("ZFB_FILE_URL",ZFB_FILE_URL);
        	map.put("TX_FILE_URL",TX_FILE_URL);
        	map.put("STATUS",0);
        	if(StringUtils.isNotBlank(BANK_NO) 
        			|| StringUtils.isNotBlank(WX_NO) || StringUtils.isNotBlank(WX_FILE_URL)
        			|| StringUtils.isNotBlank(ZFB_NO) || StringUtils.isNotBlank(ZFB_FILE_URL)) {
        		map.put("STATUS",1);
        	}
        	map.put("CRITE_USER",SessionUtil.getUserNo());
        	dityService.editUser(map);
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/addUser:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
    
    
    
    @RequestMapping(value = "/qryGoodsMsList", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> qryGoodsMsList(){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			List<Map<String,Object>> list = dityService.qryGoodsMsList(map);
			map.put("O_DATA",list);
			map.put("O_RUNSTATUS",1);
			map.put("O_MSG","success");
		} catch (Exception e) {
			logger.error("qryGoodsMsList:"+map,e);
			map.put("O_RUNSTATUS",0);
			map.put("O_MSG","system error");
		}
		return map;
	}
    
    /**
	 * 商品类别数据-查询
	 */
	@RequestMapping(value = "/srchPrdtLbData", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> srchPrdtLbData(){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			List<Object> list = feePerfMgtService.srchPrdtLbData(map);
			map.put("O_DATA",list);
			map.put("O_RUNSTATUS",1);
			map.put("O_MSG","success");
		} catch (Exception e) {
			logger.error("srchPrdtLbData:"+map,e);
			map.put("O_RUNSTATUS",0);
			map.put("O_MSG","system error");
		}
		return map;
	}
    
    @RequestMapping(value = "/qryGoodsList", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> qryGoodsList(HttpServletRequest request,
            @RequestParam(value = "GOODS_TYPE", required = true) String id){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			map.put("GOODS_TYPE",id);
			List<Map<String,Object>> list = dityService.qryGoodsList(map);
			map.put("O_DATA",list);
			map.put("O_RUNSTATUS",1);
			map.put("O_MSG","success");
		} catch (Exception e) {
			logger.error("srchPrdtLbData:"+map,e);
			map.put("O_RUNSTATUS",0);
			map.put("O_MSG","system error");
		}
		return map;
	}
    
    
    @Autowired
    SysProperties sysProperties;
    public String uplodFile2LocalPath(MultipartFile file){
		
		String url = "";
		InputStream in = null;
		FileOutputStream os = null;
		String fileName = "";
		String path = "";
		String fileId = "";
		try {
			fileName = file.getOriginalFilename();
			in = file.getInputStream();
			path = sysProperties.getLocation();//都上传到本地目录下
			fileId = IDUtils.createID();
			File tempFile = new File(path,fileId+fileName);
			tempFile.deleteOnExit();
			tempFile.createNewFile();
			os = new FileOutputStream(tempFile);
			byte temp[] = new byte[1024];
			int size = -1;
			while ((size = in.read(temp)) != -1) {
				os.write(temp, 0, size);
			}
	    	url = "/tempFile"+File.separator+"small"+fileId+fileName;
		} catch (IOException e) {
			logger.error("文件上传失败", e);
		}finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			Thumbnails.of(path+File.separator+fileId+fileName).scale(1f).outputQuality(0.25f).toFile(path+File.separator+"small"+fileId+fileName);
		} catch (IOException e) {
			logger.error("error:",e);
			url = "/tempFile"+File.separator+fileId+fileName;
		}
		return url;
    }
    
    @RequestMapping(value = "/qryUserDz", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> qryUserDz(HttpServletRequest request,
			@RequestParam(value = "USER_NO", required = false) String USER_NO,
            @RequestParam(value = "ID", required = false) String id){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			map.put("ID",id);
			map.put("USER_NO",USER_NO);
			List<Map<String,Object>> list = dityService.qryUserDz(map);
			map.put("O_DATA",list);
			map.put("O_RUNSTATUS",1);
			map.put("O_MSG","success");
		} catch (Exception e) {
			logger.error("qryUserDz:"+map,e);
			map.put("O_RUNSTATUS",0);
			map.put("O_MSG","system error");
		}
		return map;
	}
    
    @RequestMapping("/addUserDz")
	@ResponseBody
    public Object addUserDz(HttpServletRequest request,HttpServletResponse response, 
    		@RequestParam(value = "ID", required = false) String ID,
    		@RequestParam(value = "USER_NO", required = false) String USER_NO,
            @RequestParam(value = "SH_NAME", required = false) String SH_NAME,
            @RequestParam(value = "PHTONENO", required = false) String PHTONENO,
            @RequestParam(value = "DIQU", required = false) String DIQU,
            @RequestParam(value = "XXDZ", required = false) String XXDZ,
            @RequestParam(value = "ISMOREN", required = false) String ISMOREN,
            @RequestParam(value = "YOUBIAN", required = false) String YOUBIAN) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("ID",ID);
        	if(StringUtils.isEmpty(ID)) {
        		map.put("ID",IDUtils.createID());
        	}
        	map.put("USER_NO",USER_NO);
        	map.put("SH_NAME",SH_NAME);
        	map.put("PHTONENO",PHTONENO);
        	map.put("DIQU",DIQU);
        	map.put("XXDZ",XXDZ);
        	map.put("YOUBIAN",YOUBIAN);
        	map.put("ISMOREN",ISMOREN);
        	if(StringUtils.isEmpty(ID)) {
        		map.put("O_RUNSTATUS", dityService.addUserDz(map));
        	}else {
        		map.put("O_RUNSTATUS", dityService.editUserDz(map));
        	}
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/addUserDz:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
    
    @RequestMapping("/setUserDzMr")
	@ResponseBody
    public Object setUserDzMr(HttpServletRequest request,HttpServletResponse response, 
    		@RequestParam(value = "ID", required = false) String ID,
    		@RequestParam(value = "USER_NO", required = false) String USER_NO) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("ID",ID);
        	map.put("USER_NO",USER_NO);
        	dityService.setUserDzMr2(map);
        	map.put("O_RUNSTATUS", dityService.setUserDzMr(map));
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/setUserDzMr:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
    
    @RequestMapping("/delUserDz")
	@ResponseBody
    public Object delUserDz(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = false) String ID) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("ID",ID);
        	map.put("O_RUNSTATUS", dityService.delUserDz(map));
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/delUserDz:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
    
    
    @RequestMapping(value = "/qryFeePerf", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> qryFeePerf(){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			List<Object> list = feePerfMgtService.srchFeePerfData(map);
			map.put("O_DATA",list);
			map.put("O_RUNSTATUS",1);
			map.put("O_MSG","success");
		} catch (Exception e) {
			logger.error("qryFeePerf:"+map,e);
			map.put("O_RUNSTATUS",0);
			map.put("O_MSG","system error");
		}
		return map;
	}
    
    @RequestMapping(value = "/qryOrder", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> qryOrder(HttpServletRequest request,
			@RequestParam(value = "STATUS", required = false) String STATUS,
			@RequestParam(value = "ORDER_USER_NO", required = false) String ORDER_USER_NO,
            @RequestParam(value = "ID", required = false) String id){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			map.put("ID",id);
			map.put("ORDER_USER_NO",ORDER_USER_NO);
			map.put("STATUS",STATUS);
			List<Map<String,Object>> list = dityService.qryOrder(map);
			map.put("O_DATA",list);
			map.put("O_RUNSTATUS",1);
			map.put("O_MSG","success");
		} catch (Exception e) {
			logger.error("qryOrder:"+map,e);
			map.put("O_RUNSTATUS",0);
			map.put("O_MSG","system error");
		}
		return map;
	}
    
    @RequestMapping("/addOrder")
	@ResponseBody
    public Object addOrder(HttpServletRequest request,HttpServletResponse response, 
    		@RequestParam(value = "ORDER_NO", required = false) String ORDER_NO,
    		@RequestParam(value = "ORDER_PD", required = false) String ORDER_PD,
            @RequestParam(value = "ORDER_PD_NAME", required = false) String ORDER_PD_NAME,
            @RequestParam(value = "ORDER_PRICE", required = false) String ORDER_PRICE,
            @RequestParam(value = "ORDER_CVAL", required = false) String ORDER_CVAL,
            @RequestParam(value = "ORDER_CFEE", required = false) String ORDER_CFEE,
            @RequestParam(value = "ORDER_INCOM", required = false) String ORDER_INCOM,
            @RequestParam(value = "ORDER_EXPRESS", required = false) String ORDER_EXPRESS,
            @RequestParam(value = "ORDER_USER_NO", required = false) String ORDER_USER_NO,
            @RequestParam(value = "STATUS", required = false) String STATUS) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("ID",IDUtils.createID());
        	map.put("ORDER_NO",ORDER_NO);
        	map.put("ORDER_PD",ORDER_PD);
        	map.put("ORDER_PD_NAME",ORDER_PD_NAME);
        	map.put("ORDER_PRICE",ORDER_PRICE);
        	map.put("ORDER_CVAL",ORDER_CVAL);
        	map.put("ORDER_CFEE",ORDER_CFEE);
        	map.put("ORDER_INCOM",ORDER_INCOM);
        	map.put("ORDER_EXPRESS",ORDER_EXPRESS);
        	map.put("ORDER_USER_NO",ORDER_USER_NO);
        	map.put("STATUS",STATUS);
        	map.put("O_RUNSTATUS", dityService.addOrder(map));
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/addOrder:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
    
    @RequestMapping("/setOrder")
	@ResponseBody
    public Object setOrder(HttpServletRequest request,HttpServletResponse response, 
    		@RequestParam(value = "ID", required = false) String ID,
            @RequestParam(value = "STATUS", required = false) String STATUS) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("ID",ID);
        	map.put("STATUS",STATUS);
        	map.put("O_RUNSTATUS", dityService.setOrder(map));
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/setOrder:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
    
    @RequestMapping("/delOrder")
	@ResponseBody
    public Object delOrder(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = false) String ID) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("ID",ID);
        	map.put("O_RUNSTATUS", dityService.delOrder(map));
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/delOrder:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
    
    @RequestMapping(value = "/qryInfoTs", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> qryInfoTs(){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			List<Object> list = feePerfMgtService.srchInfoTsData(map);
			map.put("O_DATA",list);
			map.put("O_RUNSTATUS",1);
			map.put("O_MSG","success");
		} catch (Exception e) {
			logger.error("qryInfoTs:"+map,e);
			map.put("O_RUNSTATUS",0);
			map.put("O_MSG","system error");
		}
		return map;
	}
    
    @RequestMapping(value = "/qryOrderList", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> qryOrderList(HttpServletRequest request,
            @RequestParam(value = "TYPE", required = true) String type,
            @RequestParam(value = "USER_NO", required = true) String userNo){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			
			/**
			 * STATUS 
			 * 
			 *  0 买家刚下单，草稿状态的，这时候买家可以点击（已确认付款按钮）
			 * 
			 * 	买家点击确认付款按钮后，订单状态变为1，卖家可以对这条订单去确认收款（点击确认已收款）
			 * 	
			 * 	卖家确认收款完后，status 变为2   表示买家买下的藏品，卖家已确认收款，当前藏品持有人变成了买家
			 * 	买家可以发起委托  或者 卖家去点击发货
			 * 
			 * 	① 卖家点发货按钮，填写了发货信息，状态status变为3。这件藏品已发货了，订单结束了
			 * 
			 * 	② 买家还可以不让卖家发货，发起委托（点击委托按钮），status状态变成4，（待确认委托）
			 * 	管理员操作确认委托，状态变成5  
			 * 	（代表买家买下的藏品，卖家不用发货、这件藏品重新上架到藏品列表里卖，订单流程结束）
			 */
			List<Map<String,Object>> list = new ArrayList<>();
			map.put("TYPE",type);
			map.put("USER_NO",userNo);
			switch (type) {
			case "1":
				//付款确认页签，查当前人下完的单，Status 0的订单
				list = dityService.qryOrder(map);
				break;
			case "2":
				//收款确认
				list = dityService.qryOrder(map);
				break;
			case "3":
				//下的单已发货的
				list = dityService.qryOrder(map);
				break;
			case "4":
				//下的单,已付款的
				list = dityService.qryOrder(map);
				break;
			case "5":
				//已卖出的订单
				list = dityService.qryOrder(map);
				break;
			case "6":
				//粉丝订单
//				list = dityService.qryOrder(map);
				break;
			default:
				break;
			}
			map.put("O_DATA",list);
			map.put("O_RUNSTATUS",1);
			map.put("O_MSG","success");
		} catch (Exception e) {
			logger.error("qryOrderList:"+map,e);
			map.put("O_RUNSTATUS",0);
			map.put("O_MSG","system error");
		}
		return map;
	}
    
    //买家确认已付款
    @RequestMapping("/comfirmPay")
	@ResponseBody
    public Object comfirmPay(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = false) String id) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("ID",id);
        	List<Map<String,Object>> list = dityService.qryOrder(map);
        	Map<String,Object> order = list.get(0);
        	String ORDER_PD = String.valueOf(order.get("ORDER_PD"));
        	
        	map.clear();
        	map.put("STATUS",2);
        	map.put("ORDER_PD",ORDER_PD);
        	List<Map<String,Object>> list2 = dityService.qryOrder(map);
        	if(list2.size()>0) {
        		 map.put("O_RUNSTATUS", -1);
                 map.put("O_MSG", "该商品已被其他买家购得，请联系卖家发起退款！");
                 return map;
        	}
        	
        	map.clear();
        	map.put("ID",id);
        	map.put("STATUS",1);
        	dityService.setOrder(map);
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/comfirmPay:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
    
    //卖家确认已收款
    @RequestMapping("/comfirmIncom")
	@ResponseBody
    public Object comfirmIncom(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = false) String id) {
        Map<String, Object> map = new HashMap<>();
        try {
        	//改变商品所属人
        	map.put("ID",id);
        	List<Map<String,Object>> list = dityService.qryOrder(map);
        	Map<String,Object> order = list.get(0);
        	String ORDER_PD = String.valueOf(order.get("ORDER_PD"));
        	
        	map.clear();
        	map.put("STATUS",2);
        	map.put("ORDER_PD",ORDER_PD);
        	List<Map<String,Object>> list2 = dityService.qryOrder(map);
        	if(list2.size()>0) {
        		 map.put("O_RUNSTATUS", -1);
                 map.put("O_MSG", "您的此商品已卖出，请勿重复收款！");
                 return map;
        	}
        	
        	map.clear();
        	map.put("ID",id);
        	map.put("STATUS",2);
        	dityService.setOrder(map);
        	String ORDER_PD_TYPE = String.valueOf(order.get("ORDER_PD_TYPE"));
        	if("1".equals(ORDER_PD_TYPE)) {
//        		秒杀商品的订单
        		map.clear();
        		map.put("ORDERID",id);
        		dityService.editGoodsMsOwnAcnt(map);
        	}else {
        		map.clear();
        		map.put("ORDERID",id);
        		dityService.editGoodsOwnAcnt(map);
        	}
        	
        	map.put("ID",id);
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/comfirmPay:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
    
    //发起委托
    @RequestMapping("/fqwt")
	@ResponseBody
    public Object fqwt(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = false) String id) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("STATUS",4);
        	map.put("ID",id);
        	dityService.setOrder(map);
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/comfirmPay:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
    
    //确认委托
    @RequestMapping("/qrfqwt")
	@ResponseBody
    public Object qrfqwt(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = true) String id) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("STATUS",5);
        	map.put("ID",id);
        	dityService.setOrder(map);
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/comfirmPay:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
    
    //发货
    @RequestMapping("/setOrderExpress")
	@ResponseBody
    public Object setOrderExpress(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = true) String id,
            @RequestParam(value = "ORDER_EXPRESS", required = false) String ORDER_EXPRESS) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("STATUS",3);
        	map.put("ID",id);
        	map.put("ORDER_EXPRESS",ORDER_EXPRESS);
        	dityService.setOrder(map);
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/comfirmPay:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
    
}
