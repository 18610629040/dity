package com.dity.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.dity.common.SysProperties;
import com.dity.common.aop.annotation.Log;
import com.dity.common.bootonfig.PassToken;
import com.dity.common.bootonfig.UserLoginToken;
import com.dity.common.utils.IDUtils;
import com.dity.common.utils.QRCodeUtil;
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
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/qryGoodsList", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> qryGoodsList(HttpServletRequest request,
            @RequestParam(value = "GOODS_TYPE", required = false) String type,
            @RequestParam(value = "ID", required = false) String id,
            @RequestParam(value = "PAGENUM", required = false) String pagenum,
            @RequestParam(value = "PAGESIZE", required = false) String pagesize){
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> rmap = new HashMap<String, Object>();
		try {
			map.put("GOODS_TYPE",type);
			map.put("ID",id);
			if(StringUtils.isNotBlank(pagenum) && StringUtils.isNotBlank(pagesize)){
				map.put("PAGENUM",Integer.parseInt(pagenum));
				map.put("PAGESIZE",Integer.parseInt(pagesize));
			}
			List<Map<String,Object>> list = dityService.qryGoodsList(map);
			if(StringUtils.isNotBlank(pagenum) && StringUtils.isNotBlank(pagesize)) {
				int count = dityService.qryGoodsListCt(map);
				rmap.put("O_COUNT",count);
			}
			map.clear();
			if(StringUtils.isNotEmpty(id) && StringUtils.isEmpty(type)) {
				Map<String,Object> pd= list.get(0);
				String typeId = String.valueOf(pd.get("GOODS_TYPE"));
				
				map.put("ID",typeId);
				List<Object> tpList = feePerfMgtService.srchPrdtLbData(map);
				Map<String,Object> tp = (Map<String, Object>) tpList.get(0);
				String status = String.valueOf(tp.get("STATUS"));
				if("2".equals(status)) {
					Long timestemp = (long) 0;
					String flag = "活动未开始";
					
					String bgnTime = String.valueOf(tp.get("BGN_TIME"));
					Date now = new Date();
					SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd") ;
					String nowStr = df.format(new Date());
					String bgn = nowStr +" "+ bgnTime +":00";
					String endTime = String.valueOf(tp . get("END_TIME"));
					String end = nowStr +" "+ endTime +":00";
					SimpleDateFormat df2 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
					Date bDate = df2.parse(bgn);
					Date eDate = df2.parse(end);
					
					if(now.before(bDate)) {
						//活动还未开始，计算出剩余毫秒数
						timestemp = bDate.getTime( )-now.getTime();
					} else if(now.after(eDate)){
						flag = "活动已结束";
					} else {
						flag = "活动进行中";
						timestemp = eDate.getTime( )-now.getTime();
					}
					for (Map<String, Object> good : list) {
						good.put("HD_FLAG",flag);
						good.put("TIMESTEMP",timestemp);
					}
				}
			}
			rmap.put("O_DATA",list);
			rmap.put("O_RUNSTATUS",1);
			rmap.put("O_MSG","success");
		} catch (Exception e) {
			logger.error("srchPrdtLbData:"+map,e);
			rmap.put("O_RUNSTATUS",0);
			rmap.put("O_MSG","system error");
		}
		return rmap;
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
        	dityService.setUserDzMr(map);
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
    		@RequestParam(value = "WTF", required = false) String WTF,
    		@RequestParam(value = "YJSY", required = false) String YJSY,
    		@RequestParam(value = "DDXJ", required = false) String DDXJ,
    		@RequestParam(value = "ORDER_PD", required = true) String ORDER_PD,
            @RequestParam(value = "USER_NO", required = true) String USER_NO,
            @RequestParam(value = "ORDER_PD_TYPE", required = false) String ORDER_PD_TYPE) {
        Map<String, Object> map = new HashMap<>();
        try {
        	Map<String, Object> param = new HashMap<>();
    		param.put("ID",ORDER_PD);
    		List<Map<String,Object>> list = dityService.qryGoodsList(param);
    		if(list.size()>0) {
    			Map<String,Object> pd = list.get(0);
    			if(!"1".equals(String.valueOf(pd.get("STATUS")))) {
            		map.put("O_RUNSTATUS", -1);
        			map.put("O_MSG", "该商品已售出、或已下架，暂时无法购买！");
        			return map;
            	}
    			map.put("ORDER_PRICE",pd.get("PRICE"));
    			map.put("ORDER_USER_SEL_NO",pd.get("OWN_ACNT"));
    			map.put("ORDER_PD_NAME",pd.get("NAME"));
    			map.put("PD_FILE_URL",pd.get("FILE_URL"));
    		}else {
    			map.put("O_RUNSTATUS", -1);
                map.put("O_MSG", "该藏品已被下架或删除，无法继续下单！");
                return map;
    		}
        	
        	map.put("ID",IDUtils.createID());
        	map.put("ORDER_NO",ORDER_NO);
        	map.put("ORDER_PD",ORDER_PD);
        	map.put("ORDER_PD_TYPE",ORDER_PD_TYPE);
        	
        	map.put("ORDER_USER_BUY_NO",USER_NO);
        	map.put("ORDER_CVAL",YJSY);
        	map.put("ORDER_CFEE",WTF);
        	map.put("ORDER_INCOM",DDXJ);
        	map.put("OPTION_USER",USER_NO);
        	dityService.addOrder(map);
        	
        	Map<String, Object> map2 = new HashMap<>();
        	map2.put("ID",ORDER_PD);
        	dityService.editGoodsYsc(map2);
        	
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/addOrder:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "下单失败，请重试！");
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
			List<Map<String,Object>> list = new ArrayList<>();
			map.put("TYPE",type);
			map.put("USER_NO",userNo);
			switch (type) {
			case "0":
				//付款确认页签，查当前人下完的单，Status 0的订单
				list = dityService.qryOrder(map);
				for (Map<String, Object> map2 : list) {
					map2.put("STATUS_NAME","待我确认已付款");
				}
				break;
			case "1":
				//收款确认
				list = dityService.qryOrder(map);
				for (Map<String, Object> map2 : list) {
					map2.put("STATUS_NAME","待我确认已收款");
				}
				break;
			case "2":
				//下的单已委托的
				list = dityService.qryOrder(map);
				break;
			case "3":
				//下的单,已付款的
				list = dityService.qryOrder(map);
				break;
			case "4":
				//已卖出的订单
				list = dityService.qryOrder(map);
				for (Map<String, Object> map2 : list) {
					map2.put("STATUS_NAME","已卖出");
				}
				break;
			case "5":
				//粉丝订单
				list = dityService.qryFsOrder(map);
				break;
			case "6":
				//全部订单
				list = dityService.qryMyOrder(map);
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
        	map.put("STATUS",2);
        	dityService.setOrder(map);
        	map.clear();
        	map.put("ORDERID",id);
        	dityService.editGoodsOwnAcnt(map);
        	
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
        	map.clear();
        	map.put("ORDERID",id);
        	dityService.setGoodPrice(map);
        	dityService.editGoodsPdStatus(map);
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
        	dityService.setOrderExpress(map);
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/comfirmPay:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
    
    /**
     * 根据 url 生成二维码
     */
    @PassToken
    @RequestMapping(value = "/createLogoQRCode")
    public void createLogoQRCode(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "URL", required = true) String url) throws Exception {
        ServletOutputStream stream = null;
        try {
            stream = response.getOutputStream();
            String logoPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() 
                    + "static" + File.separator + "img" + File.separator + "wweew.png";
            //使用工具类生成二维码
            QRCodeUtil.encode(url, logoPath, stream, true);
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            if (stream != null) {
                stream.flush();
                stream.close();
            }
        }
    }
    
    @RequestMapping(value = "/qryFsUser", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> qryFsUser(HttpServletRequest request,
            @RequestParam(value = "USER_NO", required = true) String userNo){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			map.put("USER_NO",userNo);
			List<Map<String,Object>> list = dityService.qryFsUser(map);
			map.put("O_DATA",list);
			map.put("O_RUNSTATUS",1);
			map.put("O_MSG","success");
		} catch (Exception e) {
			logger.error("qryFsUser:"+map,e);
			map.put("O_RUNSTATUS",0);
			map.put("O_MSG","system error");
		}
		return map;
	}
    
    @RequestMapping(value = "/qryWtSkInfo", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> qryWtSkInfo(){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> list = dityService.qryWtSkInfo(map);
			map.put("O_DATA",list.get(0));
			map.put("O_RUNSTATUS",1);
			map.put("O_MSG","success");
		} catch (Exception e) {
			logger.error("qryWtSkInfo:"+map,e);
			map.put("O_RUNSTATUS",0);
			map.put("O_MSG","system error");
		}
		return map;
	}
    
    @RequestMapping(value = "/qryOrderSkInfo", method = { RequestMethod.POST, RequestMethod.GET })
   	@ResponseBody
   	public Map<String, Object> qryOrderSkInfo(HttpServletRequest request,
            @RequestParam(value = "GOODID", required = true) String GOODID){
   		Map<String,Object> map = new HashMap<String, Object>();
   		try {
   			map.put("GOODID",GOODID);
   			List<Map<String, Object>> list = dityService.qryOrderSkInfo(map);
   			map.put("O_DATA",list.get(0));
   			map.put("O_RUNSTATUS",1);
   			map.put("O_MSG","success");
   		} catch (Exception e) {
   			logger.error("qryWtSkInfo:"+map,e);
   			map.put("O_RUNSTATUS",0);
   			map.put("O_MSG","system error");
   		}
   		return map;
   	}
    
    
	@RequestMapping("/getAuthMessage")
    @ResponseBody
	@PassToken
    public Object getAuthMessage(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> user = new HashMap<>();
        HttpSession session = request.getSession();
		String token = (String) session.getAttribute("token");
		if (token == null) {
			user.put("STATUS_AUTH",0);
			map.put("O_DATA",user);
			map.put("O_RUNSTATUS", 1);
			map.put("O_MSG", "");
            return map;
        }
        // 验证 token
        try {
        	// 获取 token 中的 userNo
        	String userNo = JWT.decode(token).getAudience().get(0);
        	Map<String, Object> map2 = new HashMap<>();
        	map2.put("USER_NO", userNo);
            List<Map<String, Object>> list = (List<Map<String, Object>>) dityService.getUserByNo(map2);
            user = list.get(0);
        	JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256((String)user.get("PASS"))).build();
            jwtVerifier.verify(token);
            user.put("STATUS_AUTH",1);
    		map.put("O_DATA",user);
    		map.put("O_RUNSTATUS", 1);
    		map.put("O_MSG", "");
        } catch (Exception e) {
        	//token验证失败，或token已过期，TokenService中设置有效时间，或用户被删了，改了密码。
        	user.put("STATUS_AUTH",0);
			map.put("O_DATA",user);
			map.put("O_RUNSTATUS", 1);
			map.put("O_MSG", "");
            return map;
        }
        return map;
    }
	
	@RequestMapping(value = "/getYjInfo", method = { RequestMethod.POST, RequestMethod.GET })
   	@ResponseBody
   	public Map<String, Object> getYjInfo(HttpServletRequest request,
            @RequestParam(value = "USER_NO", required = true) String USER_NO){
   		Map<String,Object> map = new HashMap<String, Object>();
   		try {
   			map.put("USER_NO",USER_NO);
   			List<Map<String, Object>> list = dityService.getYjInfo(map);
   			map.put("O_DATA",list.get(0));
   			map.put("O_RUNSTATUS",1);
   			map.put("O_MSG","success");
   		} catch (Exception e) {
   			logger.error("qryWtSkInfo:"+map,e);
   			map.put("O_RUNSTATUS",0);
   			map.put("O_MSG","system error");
   		}
   		return map;
   	}
	
	@RequestMapping(value = "/getYjList", method = { RequestMethod.POST, RequestMethod.GET })
   	@ResponseBody
   	public Map<String, Object> getYjList(HttpServletRequest request,
            @RequestParam(value = "USER_NO", required = true) String USER_NO){
   		Map<String,Object> map = new HashMap<String, Object>();
   		try {
   			map.put("USER_NO",USER_NO);
   			List<Map<String, Object>> list = dityService.getYjList(map);
   			map.put("O_DATA",list);
   			map.put("O_RUNSTATUS",1);
   			map.put("O_MSG","success");
   		} catch (Exception e) {
   			logger.error("qryWtSkInfo:"+map,e);
   			map.put("O_RUNSTATUS",0);
   			map.put("O_MSG","system error");
   		}
   		return map;
   	}
	
	@RequestMapping("/addYJ")
	@ResponseBody
    public Object addYJ(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "USER_NO", required = true) String USER_NO,
            @RequestParam(value = "JE", required = true) String JE) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("USER_NO",USER_NO);
        	map.put("JE",JE);
        	map.put("ID",IDUtils.createID());
        	dityService.addYJ(map);
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/addYJ:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
}
