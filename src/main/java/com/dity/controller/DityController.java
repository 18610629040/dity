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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dity.common.SysProperties;
import com.dity.common.bootonfig.PassToken;
import com.dity.common.bootonfig.UserLoginToken;
import com.dity.common.utils.FileUtils;
import com.dity.common.utils.IDUtils;
import com.dity.common.utils.SessionUtil;
import com.dity.service.DityService;

import net.coobird.thumbnailator.Thumbnails;

@CrossOrigin
@Controller
@RequestMapping("/dity")
@UserLoginToken
public class DityController {
	
	private static final Logger logger = LoggerFactory.getLogger(DityController.class);
	
	@Autowired
	DityService dityService;
	
	@Autowired
    SysProperties sysProperties;
	
    @RequestMapping(value = "/getUserBySession", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    @PassToken
    public Object getUserBySession(){
        return SessionUtil.getUser();
    }
    
    @RequestMapping(value = "/userIndex", method = { RequestMethod.POST, RequestMethod.GET })
    public String userIndex(){
        return "/sysMgt/userIndex";
    }
    
    @RequestMapping(value = "/userMgt", method = { RequestMethod.POST, RequestMethod.GET })
    public String userMgt(){
        return "/sysMgt/userMgt";
    }
    
    @RequestMapping(value = "/qryUserList", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public List<Object> qryUserList(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "USER_TYPE", required = false) String type){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("USER_TYPE",type);
		List<Object> list = new ArrayList<>();
		try {
			list = dityService.qryUserList(map);
		} catch (Exception e) {
			logger.error("qryUserList:"+map,e);
		}
		return list;
	}
    
	@RequestMapping("/addUser")
	@ResponseBody
    public Object addUser(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = true) String id,
            @RequestParam(value = "USER_NO", required = true) String USER_NO,
            @RequestParam(value = "PASS", required = true) String PASS,
            @RequestParam(value = "USER_NAME", required = false) String USER_NAME,
            @RequestParam(value = "MOBILE_NO", required = false) String MOBILE_NO,
            @RequestParam(value = "USER_ADD", required = false) String USER_ADD,
            @RequestParam(value = "USER_AGE", required = false) String USER_AGE,
            @RequestParam(value = "USER_BIRTH", required = false) String USER_BIRTH,
            @RequestParam(value = "USER_TYPE", required = false) String USER_TYPE,
            @RequestParam(value = "BANK_NO", required = false) String BANK_NO,
            @RequestParam(value = "BANK_NAME", required = false) String BANK_NAME,
            @RequestParam(value = "REAL_NAME", required = false) String REAL_NAME,
            @RequestParam(value = "WX_NO", required = false) String WX_NO,
            @RequestParam(value = "ZFB_NO", required = false) String ZFB_NO,
            @RequestParam(value = "WX_FILE_URL", required = false) String WX_FILE_URL,
            @RequestParam(value = "ZFB_FILE_URL", required = false) String ZFB_FILE_URL) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("PASS",PASS);
        	map.put("USER_NAME",USER_NAME);
        	map.put("MOBILE_NO",MOBILE_NO);
        	map.put("USER_ADD",USER_ADD);
        	map.put("USER_AGE",USER_AGE);
        	map.put("USER_BIRTH",USER_BIRTH);
        	map.put("USER_TYPE",USER_TYPE);
        	map.put("BANK_NO",BANK_NO);
        	map.put("BANK_NAME",BANK_NAME);
        	map.put("REAL_NAME",REAL_NAME);
        	map.put("WX_NO",WX_NO);
        	map.put("ZFB_NO",ZFB_NO);
        	map.put("WX_FILE_URL",WX_FILE_URL);
        	map.put("ZFB_FILE_URL",ZFB_FILE_URL);
        	map.put("TX_FILE_URL","");
        	map.put("CRITE_USER",SessionUtil.getUserNo());
        	map.put("STATUS",0);
        	if(StringUtils.isNotBlank(BANK_NO) 
        			|| StringUtils.isNotBlank(WX_NO) || StringUtils.isNotBlank(WX_FILE_URL)
        			|| StringUtils.isNotBlank(ZFB_NO) || StringUtils.isNotBlank(ZFB_FILE_URL)) {
        		map.put("STATUS",1);
        	}
        	
        	map.put("USER_NO",USER_NO);
        	List<Map<String, Object>> list= dityService.getUserByNo(map);
        	if(list.size()>0) {
        		map.put("O_RUNSTATUS", -1);
            	map.put("O_MSG", "账号重复，请重新输入！");
            	return map;
        	}
        	if(StringUtils.isBlank(id)) {
        		map.put("ID",IDUtils.createID());
        		dityService.addUser(map);
        	}else {
        		map.put("ID",id);
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
		if(wximg != null) {
			map.put("url", uplodFile2LocalPath(wximg));
		}else {
			map.put("url", uplodFile2LocalPath(zfbimg));
		}
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
	
	//不用了，先留的吧
	@RequestMapping(value = "/getImgByUrl", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public void getQyImg(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "FILE_URL", required = true) String path) {
        Map<String, Object> map = new HashMap<>();
        OutputStream os = null;
        response.addHeader("Content-Disposition",
                "inline;filename=" + FileUtils.toUtf8String("图片") + ".jpeg");
        response.setContentType("image/jpeg");
        InputStream in = null;
        byte[] b = null;
        try {
        	File qyFile = new File(path);
        	if(qyFile.exists()) {
        		b = FileUtils.file2byte(qyFile);
        	}else{
        		b = "图片已丢失".getBytes();
        	}
        	in = new ByteArrayInputStream(b);
        	os = response.getOutputStream();
        	int count;
        	while ((count = in.read(b)) > 0) {
        		os.write(b, 0, count);
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
	
	@RequestMapping(value = "/uploadQyImg", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Object uploadQyImg(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "qyimgFile", required = false) MultipartFile qyimg) throws IOException{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("url", uplodFile2LocalPath(qyimg));
		map.put("O_RUNSTATUS", 1);
    	map.put("O_MSG", "操作成功！");
    	return map;
	}
	
	@RequestMapping(value = "/webuploader", method = { RequestMethod.POST, RequestMethod.GET })
    public String webuploader(){
        return "/sysMgt/webuploader";
    }
    
	@RequestMapping(value = "/uploadLbtImg", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Object uploadLbtImg(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "file", required = false) MultipartFile img) throws IOException{
		Map<String, Object> map = new HashMap<>();
		String id = IDUtils.createID();
		String fileName = img.getOriginalFilename();
		map.put("ID",id);
		map.put("IMAGE_NAME",fileName);
		map.put("FILE_URL",uplodFile2LocalPath(img));
		dityService.addRottn(map);
		map.put("O_RUNSTATUS", 1);
    	return map;
	}
	
	@RequestMapping(value = "/qryRottnChrt", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public List<Map<String,Object>> qryRottnChrt(){
		Map<String,Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			list = dityService.qryRottnChrt(map);
		} catch (Exception e) {
			logger.error("/dity/qryRottnChrt:"+map,e);
		}
 		return list;
	}
	
	@RequestMapping("/delItton")
	@ResponseBody
    public Object delItton(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = true) Object id) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("ID", id);
        	dityService.delItton(map);
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/delItton:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
	
	@RequestMapping("/editIttonOrder")
	@ResponseBody
    public Object editIttonOrder(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = true) Object id,
            @RequestParam(value = "IMAGE_ORDER", required = true) Object der) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("ID", id);
        	map.put("IMAGE_ORDER", der);
        	dityService.editIttonOrder(map);
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/editIttonOrder:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
	
	@RequestMapping(value = "/uploadPdImg", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Object uploadPdImg(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "IMAGE", required = false) MultipartFile image){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("url",uplodFile2LocalPath(image));
		map.put("O_RUNSTATUS", 1);
    	map.put("O_MSG", "操作成功！");
    	return map;
	}
	
	@RequestMapping(value = "/formAdvanced", method = { RequestMethod.POST, RequestMethod.GET })
    public String formAdvanced(){
        return "/sysMgt/formAdvanced";
    }

	
	@RequestMapping(value = "/pdMgt", method = { RequestMethod.POST, RequestMethod.GET })
    public String pdMgt(){
        return "/sysMgt/pdMgt";
    }
	
	@RequestMapping(value = "/qryGoodsList", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public List<Map<String,Object>> qryGoodsList(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "TYPE", required = true) String type){
		Map<String,Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> list = new ArrayList<>();
		try {
			map.put("TYPE",type);
			list = dityService.qryGoodsList(map);
		} catch (Exception e) {
			logger.error("qryGoodsList:"+map,e);
		}
		return list;
	}
    
	@RequestMapping("/addGoods")
	@ResponseBody
    public Object addGoods(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = false) String id,
            @RequestParam(value = "NAME", required = false) String name,
            @RequestParam(value = "PRICE", required = false) String price,
            @RequestParam(value = "INTRDCT", required = false) String intrdct,
            @RequestParam(value = "GOODS_TYPE", required = false) String goods_type,
            @RequestParam(value = "FILE_URL", required = false) String FILE_URL) {
        Map<String, Object> map = new HashMap<>();
        try {
        	if(StringUtils.isBlank(id)) {
        		map.put("ID",IDUtils.createID());
        	}else {
        		map.put("ID",id);
        	}
        	map.put("NAME",name);
        	map.put("PRICE",price);
        	map.put("INTRDCT",intrdct);
        	map.put("GOODS_TYPE",goods_type);
        	map.put("FILE_URL",FILE_URL);
        	if(StringUtils.isBlank(id)) {
        		map.put("CRITE_USER",SessionUtil.getUserNo());
        		map.put("STATUS",1);
            	map.put("OWN_ACNT","");
        		dityService.addGoods(map);
        	}else {
        		dityService.editGoods(map);
        	}
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/addGoods:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
	
	@RequestMapping("/delGoods")
	@ResponseBody
    public Object delGoods(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = true) Object id) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("ID", id);
        	dityService.delGoods(map);
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/delGoods:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
	
	@RequestMapping("/outGoods")
	@ResponseBody
    public Object outGoods(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = true) Object id) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("ID", id);
        	map.put("STATUS", 0);
        	dityService.setGoodsStatus(map);
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/outGoods:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
	
	
	@RequestMapping("/putGoods")
	@ResponseBody
    public Object putGoods(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = true) Object id) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("ID", id);
        	map.put("STATUS", 1);
        	dityService.setGoodsStatus(map);
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/outGoods:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
	
	@RequestMapping("/qrfqwt")
	@ResponseBody
    public Object qrfqwt(HttpServletRequest request,HttpServletResponse response, 
    		@RequestParam(value = "ID", required = false) String id) {
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
            logger.error("/qrfqwt:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
	
	@RequestMapping(value = "/qryOrder", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public List<Map<String, Object>> qryOrder(HttpServletRequest request,
			@RequestParam(value = "STATUS", required = false) String STATUS,
			@RequestParam(value = "ORDER_USER_NO", required = false) String ORDER_USER_NO,
            @RequestParam(value = "ID", required = false) String id){
		Map<String,Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
		try {
			map.put("ID",id);
			map.put("ORDER_USER_NO",ORDER_USER_NO);
			map.put("STATUS",STATUS);
			list = dityService.qryOrder(map);
		} catch (Exception e) {
			logger.error("qryOrder:"+map,e);
		}
		return list;
	}
	
	
	@RequestMapping("/setUserWtsk")
	@ResponseBody
    public Object setUserWtsk(HttpServletRequest request,HttpServletResponse response, 
    		@RequestParam(value = "ID", required = false) String ID) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("ID",ID);
        	dityService.clearUserWtsk(map);
        	dityService.setUserWtsk(map);
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/setUserWtsk:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
	
	@RequestMapping("/clearUserSk")
	@ResponseBody
    public Object clearUserSk(HttpServletRequest request,HttpServletResponse response, 
    		@RequestParam(value = "ID", required = false) String ID) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("ID",ID);
        	dityService.clearUserSk(map);
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/claerUserSk:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
	
	@RequestMapping("/updateYjStatus")
	@ResponseBody
    public Object updateYjStatus(HttpServletRequest request,HttpServletResponse response, 
    		@RequestParam(value = "ID", required = false) String ID) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("ID",ID);
        	dityService.updateYjStatus(map);
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/updateYjStatus:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
	
	@RequestMapping(value = "/userYj", method = { RequestMethod.POST, RequestMethod.GET })
    public String userYj(){
        return "/sysMgt/userYj";
    }
	
	@RequestMapping(value = "/getYjList", method = { RequestMethod.POST, RequestMethod.GET })
   	@ResponseBody
   	public List<Map<String, Object>> getYjList(HttpServletRequest request){
   		Map<String,Object> map = new HashMap<String, Object>();
   		List<Map<String, Object>> list = new ArrayList<>();
   		try {
   			list = dityService.getYjList(map);
   		} catch (Exception e) {
   			logger.error("getYjList:"+map,e);
   		}
   		return list;
   	}
	
	@RequestMapping("/delOrder")
	@ResponseBody
    public Object delOrder(HttpServletRequest request,HttpServletResponse response, 
            @RequestParam(value = "ID", required = true) Object id) {
        Map<String, Object> map = new HashMap<>();
        try {
        	map.put("ID", id);
        	dityService.delOrder(map);
        	map.put("O_RUNSTATUS", 1);
        	map.put("O_MSG", "操作成功！");
        } catch (Exception e) {
            logger.error("/delOrder:" + map, e);
            map.put("O_RUNSTATUS", -1);
            map.put("O_MSG", "system error");
        }
        return map;
    }
}
