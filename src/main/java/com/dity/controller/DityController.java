package com.dity.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dity.common.bootonfig.UserLoginToken;
import com.dity.common.utils.FileUtils;
import com.dity.common.utils.IDUtils;
import com.dity.common.utils.SessionUtil;
import com.dity.service.DityService;

@CrossOrigin
@Controller
@RequestMapping("/dity")
@UserLoginToken
public class DityController {
	
	private static final Logger logger = LoggerFactory.getLogger(DityController.class);
	
	@Autowired
	DityService dityService;
	
	
    @RequestMapping(value = "/index", method = { RequestMethod.POST, RequestMethod.GET })
    public String index(){
        return "/welcom/welcomIndex";
    }
    
    @RequestMapping(value = "/userIndex", method = { RequestMethod.POST, RequestMethod.GET })
    public String userIndex(){
        return "/sysMgt/userIndex";
    }
    
    @RequestMapping(value = "/qryUserList", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public List<Object> srchFeePerfData(){
		Map<String,Object> map = new HashMap<String, Object>();
		List<Object> list = new ArrayList<>();
		try {
			list = dityService.qryUserList(map);
		} catch (Exception e) {
			logger.error("/dity/feePerfMgt/srchFeePerfData:"+map,e);
		}
		return list;
	}
    
	@RequestMapping("/addUser")
	@ResponseBody
    public Object addUser(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = false) String id,
            @RequestParam(value = "USER_NO", required = false) String USER_NO,
            @RequestParam(value = "PASS", required = false) String PASS,
            @RequestParam(value = "USER_NAME", required = false) String USER_NAME,
            @RequestParam(value = "MOBILE_NO", required = false) String MOBILE_NO,
            @RequestParam(value = "USER_ADD", required = false) String USER_ADD,
            @RequestParam(value = "USER_AGE", required = false) String USER_AGE,
            @RequestParam(value = "USER_BIRTH", required = false) String USER_BIRTH,
            @RequestParam(value = "USER_TYPE", required = false) String USER_TYPE,
            @RequestParam(value = "BANK_NO", required = false) String BANK_NO,
            @RequestParam(value = "WX_NO", required = false) String WX_NO,
            @RequestParam(value = "ZFB_NO", required = false) String ZFB_NO,
            @RequestParam(value = "wxFileName", required = false) String wxFileName,
            @RequestParam(value = "zfbFileName", required = false) String zfbFileName) {
        Map<String, Object> map = new HashMap<>();
        try {
        	if(StringUtils.isBlank(id)) {
        		map.put("ID",IDUtils.createID());
        	}else {
        		map.put("ID",id);
        	}
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
        	map.put("USER_TYPE",USER_TYPE);
        	map.put("BANK_NO",BANK_NO);
        	map.put("WX_NO",WX_NO);
        	map.put("ZFB_NO",ZFB_NO);
        	String path = ResourceUtils.getURL("classpath:static").getPath() + File.separator +"tempFile"+ File.separator;
        	
        	if(StringUtils.isNotBlank(wxFileName)) {
        		File wxFile = new File(path,wxFileName);
        		map.put("WX_IMAGE",FileUtils.file2byte(wxFile));
        		new File(path,wxFileName).delete();
        	}
			if(StringUtils.isNotBlank(zfbFileName)) {
				File zfbFile = new File(path,zfbFileName);
				map.put("ZFB_IMAGE",FileUtils.file2byte(zfbFile));
				new File(path,zfbFileName).delete();
			}
        	map.put("STATUS",1);
        	map.put("CRITE_USER",SessionUtil.getUserNo());
        	if(StringUtils.isBlank(id)) {
        		dityService.addUser(map);
        	}else {
        		dityService.editUser(map);
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
	
	@RequestMapping("/delUser")
	@ResponseBody
    public Object delUser(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = true) Object id) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("ID", id);
        	dityService.delUser(map);
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/login:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
	
//	@SuppressWarnings("unchecked")
//	@RequestMapping("/userMgt")
//	@ResponseBody
//    public Object userMgt(HttpServletRequest request,HttpServletResponse response, 
//            @RequestParam(value = "postData", required = false) Object postData) {
//        Map<String, Object> map = new HashMap<>();
//        try {
//        	Map<String, Object> data = (Map<String, Object>)JSON.parse(String.valueOf(postData));
//        	map.put("O_RUNSTATUS", 1);
//        	map.put("O_MSG", "操作成功！");
//        } catch (Exception e) {
//            logger.error("/login:" + map, e);
//            map.put("O_RUNSTATUS", -1);
//            map.put("O_MSG", "system error");
//        }
//        return map;
//    }
	
	@RequestMapping(value = "/uploadUserImg", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Object uploadUserImg(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "wximgFile", required = false) MultipartFile wximg,
			@RequestParam(value = "zfbimgFile", required = false) MultipartFile zfbimg){
		Map<String,Object> map = new HashMap<String, Object>();
		if((wximg!=null && wximg.getSize() >61440) || (zfbimg!=null && zfbimg.getSize() >61440)) {
    		map.put("O_RUNSTATUS", -1);
        	map.put("O_MSG", "请选择60K以下的二维码图片！");
        	return map;
    	}
		InputStream in = null;
		FileOutputStream os = null;
		try {
			String fileName = "";
			if(wximg != null) {
				in = wximg.getInputStream();
				fileName = wximg.getOriginalFilename();
			}else {
				in = zfbimg.getInputStream();
				fileName = zfbimg.getOriginalFilename();
			}
			String path = ResourceUtils.getURL("classpath:static").getPath() + File.separator +"tempFile"+ File.separator;
//			String fileType = fileName.split("\\.")[1];
			File tempFile = new File(path,fileName);
			tempFile.deleteOnExit();
			tempFile.createNewFile();
			os = new FileOutputStream(tempFile);
			byte temp[] = new byte[1024];
			int size = -1;
			while ((size = in.read(temp)) != -1) {
				os.write(temp, 0, size);
			}
			map.put("url", "/tempFile/"+fileName);
			map.put("fileName", fileName);
			map.put("O_RUNSTATUS", 1);
	    	map.put("O_MSG", "操作成功！");
		} catch (IOException e) {
			logger.error("文件上传失败", e);
			map.put("O_RUNSTATUS", -1);
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
    	return map;
	}
	
	@RequestMapping(value = "/getUserFkmImg", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public void getUserFkmImg(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "USER_NO", required = true) String no,
            @RequestParam(value = "TYPE", required = true) String type) {
        Map<String, Object> map = new HashMap<>();
        OutputStream os = null;
        response.addHeader("Content-Disposition",
                "inline;filename=" + FileUtils.toUtf8String("付款码") + ".jpeg");
        response.setContentType("image/jpeg");
        InputStream in = null;
        byte[] b = null;
        try {
            map.put("USER_NO", no);
            List<Map<String, Object>> list = (List<Map<String, Object>>) dityService.getUserByNo(map);
            Map<String, Object> result = list.get(0);
            if (result != null && result.size() > 0) {
            	if ("WX".equals(type)) {
            		b=(byte[]) result.get("WX_IMAGE");
            	} else {
            		b=(byte[]) result.get("ZFB_IMAGE");
            	}
            	in = new ByteArrayInputStream(b);
                os = response.getOutputStream();
                int count;
                while ((count = in.read(b)) > 0) {
                    os.write(b, 0, count);
                }
            }
        } catch (Exception e) {
        	logger.error("文件下载失败", e);
            map.put("O_RUNSTATUS", -1);
        } finally {
            if (in != null) {
                try {
                    in.close();
                    in = null;
                } catch (IOException e) {
                }
            }
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
