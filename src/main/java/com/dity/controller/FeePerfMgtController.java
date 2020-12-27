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

import com.alibaba.fastjson.JSONObject;
import com.dity.common.SysProperties;
import com.dity.common.bootonfig.UserLoginToken;
import com.dity.common.utils.IDUtils;
import com.dity.common.utils.SessionUtil;
import com.dity.service.DityService;
import com.dity.service.FeePerfMgtService;

import net.coobird.thumbnailator.Thumbnails;

@CrossOrigin
@Controller
@RequestMapping("/dity/feePerfMgt")
@UserLoginToken
public class FeePerfMgtController {

	private static final Logger logger = LoggerFactory.getLogger(FeePerfMgtController.class);
	
	@Autowired
	FeePerfMgtService feePerfMgtService;
	
	@Autowired
	DityService dityService;
	
	@Autowired
    SysProperties sysProperties;
	
	/**
	 * 费率管理首页
	 */
	@RequestMapping(value = "/feePerfIndex", method = { RequestMethod.POST, RequestMethod.GET })
    public String feePerfIndex(){
        return "/sysMgt/feePerfMgt";
    }
	
	/**
	 * 费率数据-查询
	 */
	@RequestMapping(value = "/srchFeePerfData", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public List<Object> srchFeePerfData(){
		Map<String,Object> map = new HashMap<String, Object>();
		List<Object> list = new ArrayList<>();
		try {
			list = feePerfMgtService.srchFeePerfData(map);
		} catch (Exception e) {
			logger.error("/dity/feePerfMgt/srchFeePerfData:"+map,e);
		}
		return list;
	}

	/**
	 * 费率数据-操作
	 */
	@RequestMapping(value = "/optFeePerfData", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Object optFeePerfData(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		String postData = request.getParameter("postData");
		JSONObject jObj = JSONObject.parseObject(postData);
		String RATE_NAME = String.valueOf(jObj.get("RATE_NAME"));
		String RATE_VAL = String.valueOf(jObj.get("RATE_VAL"));
		String oper = String.valueOf(jObj.get("oper"));
		String msg = "操作成功！";
		int count = 0;
		try {
			map.put("RATE_NAME",RATE_NAME);
			map.put("RATE_VAL",RATE_VAL);
			map.put("CRITE_USER",SessionUtil.getUserNo());
			if("add".equals(oper)){
				map.put("ID",IDUtils.createID());
				count = feePerfMgtService.addFeePerfData(map);
			}else if("edit".equals(oper)) {
				map.put("ID",String.valueOf(jObj.get("ID")));
				count = feePerfMgtService.modfyFeePerfData(map);
			}else if("del".equals(oper)){
				map.put("ID",String.valueOf(jObj.get("ID")));
				count = feePerfMgtService.delFeePerfData(map);
			}
			map.put("O_RUNSTATUS",count);
			if(count<1) {
				msg = "操作失败，请联系管理员！";
			}
		} catch (Exception e) {
			map.put("O_RUNSTATUS",0);
			msg = "操作失败，请联系管理员！";
			logger.error("/dity/feePerfMgt/optFeePerfData:"+map,e);
		}
		map.put("O_MSG",msg);
		return map;
	}

	/**
	 * 商品类别维护管理首页
	 */
	@RequestMapping(value = "/prdtLbIndex", method = { RequestMethod.POST, RequestMethod.GET })
    public String prdtLbIndex(){
        return "/sysMgt/prdtLbMgt";
    }
	
	@RequestMapping(value = "/pdType", method = { RequestMethod.POST, RequestMethod.GET })
    public String pdType(){
        return "/sysMgt/pdType";
    }
	
	/**
	 * 商品类别数据-查询
	 */
	@RequestMapping(value = "/srchPrdtLbData", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public List<Object> srchPrdtLbData(){
		Map<String,Object> map = new HashMap<String, Object>();
		List<Object> list = new ArrayList<>();
		try {
			list = feePerfMgtService.srchPrdtLbData(map);
		} catch (Exception e) {
			logger.error("/dity/feePerfMgt/srchPrdtLbData:"+map,e);
		}
		return list;
	}
	
	@RequestMapping("/addQy")
	@ResponseBody
    public Object addQy(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = false) String id,
            @RequestParam(value = "TYPE_NAME", required = false) String TYPE_NAME,
            @RequestParam(value = "TYPE_ORDER", required = false) String TYPE_ORDER,
            @RequestParam(value = "STATUS", required = false) String STATUS,
            @RequestParam(value = "BGN_TIME", required = false) String BGN_TIME,
            @RequestParam(value = "END_TIME", required = false) String END_TIME,
            @RequestParam(value = "FILE_URL", required = false) String FILE_URL) {
        Map<String, Object> map = new HashMap<>();
        try {
        	if(StringUtils.isBlank(FILE_URL)) {
        		map.put("O_RUNSTATUS", -1);
                map.put("O_MSG", "请选择图片");
                return map;
        	}
        	if(StringUtils.isBlank(id)) {
        		map.put("ID",IDUtils.createID());
        	}else {
        		map.put("ID",id);
        	}
        	map.put("TYPE_NAME",TYPE_NAME);
        	map.put("TYPE_ORDER",TYPE_ORDER);
        	map.put("FILE_URL",FILE_URL);
        	map.put("STATUS",STATUS);
        	map.put("BGN_TIME",BGN_TIME);
        	map.put("END_TIME",END_TIME);
        	map.put("CRITE_USER",SessionUtil.getUserNo());
        	if(StringUtils.isBlank(id)) {
        		feePerfMgtService.addPrdtLbData(map);
        	}else {
        		feePerfMgtService.modfyPrdtLbData(map);
        	}
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
	 * 商品类别数据-操作
	 */
	@RequestMapping(value = "/optPrdtLbData", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Object optPrdtLbData(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		String postData = request.getParameter("postData");
		JSONObject jObj = JSONObject.parseObject(postData);
		String TYPE_ORDER = String.valueOf(jObj.get("TYPE_ORDER"));
		String TYPE_NAME = String.valueOf(jObj.get("TYPE_NAME"));
		String oper = String.valueOf(jObj.get("oper"));
		String msg = "操作成功！";
		int count = 0;
		try {
			map.put("TYPE_ORDER",TYPE_ORDER);
			map.put("TYPE_NAME",TYPE_NAME);
			map.put("CRITE_USER",SessionUtil.getUserNo());
			if("add".equals(oper)){
				map.put("ID",IDUtils.createID());
				count = feePerfMgtService.addPrdtLbData(map);
			}else if("edit".equals(oper)) {
				map.put("ID",String.valueOf(jObj.get("ID")));
				count = feePerfMgtService.modfyPrdtLbData(map);
			}else if("del".equals(oper)){
				map.put("ID",String.valueOf(jObj.get("ID")));
				count = feePerfMgtService.delPrdtLbData(map);
			}else if("updtStat".equals(oper)) {
				map.put("ID",String.valueOf(jObj.get("ID")));
				count = feePerfMgtService.updtPrdtLbStatData(map);
			}
			map.put("O_RUNSTATUS",count);
			if(count<1) {
				msg = "操作失败，请联系管理员！";
			}
		} catch (Exception e) {
			map.put("O_RUNSTATUS",0);
			msg = "操作失败，请联系管理员！";
			logger.error("/dity/feePerfMgt/optPrdtLbData:"+map,e);
		}
		map.put("O_MSG",msg);
		return map;
	}

	/**
	 * 信息提示管理首页
	 */
	@RequestMapping(value = "/msgInfo", method = { RequestMethod.POST, RequestMethod.GET })
    public String infoTsIndex(){
        return "/sysMgt/msgInfo";
    }
	
	/**
	 * 信息提示数据-查询
	 */
	@RequestMapping(value = "/srchInfoTsData", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public List<Object> srchInfoTsData(){
		Map<String,Object> map = new HashMap<String, Object>();
		List<Object> list = new ArrayList<>();
		try {
			list = feePerfMgtService.srchInfoTsData(map);
		} catch (Exception e) {
			logger.error("/dity/feePerfMgt/srchInfoTsData:"+map,e);
		}
		return list;
	}
	
	@RequestMapping("/ggMgt")
	@ResponseBody
    public Object addgg(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = false) String id,
            @RequestParam(value = "CONTATE_NAME", required = false) String CONTATE_NAME,
            @RequestParam(value = "CONTATE_INFO", required = false) String CONTATE_INFO) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("CONTATE_NAME",CONTATE_NAME);
        	map.put("CONTATE_INFO",CONTATE_INFO);
        	if(StringUtils.isBlank(id)) {
        		map.put("ID",IDUtils.createID());
        		feePerfMgtService.addInfoTsData(map);
        	}else {
        		map.put("ID",id);
        		feePerfMgtService.modfyInfoTsData(map);
        	}
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/ggMgt:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
	
	@RequestMapping("/delGg")
	@ResponseBody
    public Object delUser(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = true) String id) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("ID", id);
        	feePerfMgtService.delInfoTsData(map);
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/delGg:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
	
	@RequestMapping(value = "/uploadGgImg", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Object uploadGgImg(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "ggFile", required = false) MultipartFile ggFile){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("url", uplodFile2LocalPath(ggFile));
		map.put("O_RUNSTATUS", 1);
    	map.put("O_MSG", "操作成功！");
    	return map;
	}
	
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
	
	/**
	 * 全部订单
	 */
	@RequestMapping(value = "/pdOrderIndex", method = { RequestMethod.POST, RequestMethod.GET })
    public String pdOrderIndex(){
        return "/sysMgt/pdOrderMgt";
    } 
	
	/**
	 * 委托订单
	 */
	@RequestMapping(value = "/wtPdOrderMgt", method = { RequestMethod.POST, RequestMethod.GET })
    public String wtPdOrderMgt(){
        return "/sysMgt/wtPdOrderMgt";
    } 
	
	/**
	 * 订单管理-查询
	 */
	@RequestMapping(value = "/srchPdOrderData", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public List<Map<String, Object>> srchPdOrderData(){
		Map<String,Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			list = dityService.qryOrder(map);
		} catch (Exception e) {
			logger.error("/dity/feePerfMgt/srchPdOrderData:"+map,e);
		}
		return list;
	}
}
