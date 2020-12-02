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
			logger.error("qryUserList:"+map,e);
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
            @RequestParam(value = "USER_ADD", required = false) String USER_ADD,
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
        	if(StringUtils.isBlank(USER_NO) || StringUtils.isBlank(PASS)) {
        		map.put("O_RUNSTATUS", -1);
            	map.put("O_MSG", "账号密码不可为空！");
            	return map;
        	}
        	
        	map.put("ID",id);
        	map.put("USER_NO",USER_NO);
        	List<Map<String, Object>> list= dityService.getUserByNo(map);
        	if(list.size()>0) {
        		map.put("O_RUNSTATUS", -1);
            	map.put("O_MSG", "账号重复，请重新输入！");
            	return map;
        	}
        	map.put("PASS",PASS);
        	map.put("USER_NAME",USER_NAME);
        	map.put("MOBILE_NO",MOBILE_NO);
        	map.put("USER_ADD",USER_ADD);
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
	    	url = "/tempFile"+File.separator+"small"+fileId;
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
			e.printStackTrace();
		}
		return url;
	}
}
