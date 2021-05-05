package cmcc.com.spring_boot_mocker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.hibernate.mapping.Map;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.IOUtils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Controller
@RestController
@Component
@ConfigurationProperties(prefix= "sleep")


public class Mocker {
	
	private int time;
	
	public int GetTime(){
        return time;
	}
	public void setTime(int time){
        this.time = time;
    }
	private Logger logger = LoggerFactory.logger(getClass());

	@GetMapping(value = "/hello")
	public String hello(String header) {

		return "{\"key\":\"ddd\"}";
	}

	// post 获取header和body信息
	@RequestMapping(value = "/post", method = RequestMethod.POST)
	@ResponseBody
	public String postProcess(HttpServletRequest request, @RequestBody String info) {
		String retrunValue = "Hello, Angus! This is POST Request!";

		String dataUrl = "";
		String taskId;
		logger.info("request body=" + info);
		String result = "";

		JSONObject object = JSON.parseObject(info);
		// 利用键值对的方式获取到值
		try {
			dataUrl = object.get("dataUrl").toString();
			taskId = object.get("taskId").toString();
			logger.info("dataUrl=" + dataUrl);
			logger.info("taskId=" + taskId);

		} catch (Exception e) {
			result = "{\"return_code\":\"-400\",\"error\":\"invalid para\"}";
			return result;

		}

		Enumeration<String> requestHeader = request.getHeaderNames();

		System.out.println("------- header -------");
		while (requestHeader.hasMoreElements()) {
			String headerKey = requestHeader.nextElement().toString();
			// 打印所有Header值

			System.out.println("headerKey=" + headerKey + ";value=" + request.getHeader(headerKey));
		}

		return retrunValue;
	}

	// get 获取header信息
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public String get(HttpServletRequest request) {
		String retrunValue = "";
		System.out.println("=======GET Process=======");

		java.util.Map<String, String[]> requestMsg = request.getParameterMap();
		Enumeration<String> requestHeader = request.getHeaderNames();

		System.out.println("------- header -------");
		while (requestHeader.hasMoreElements()) {
			String headerKey = requestHeader.nextElement().toString();
		    // 打印所有Header值
			System.out.println("headerKey=" + headerKey + ";value=" + request.getHeader(headerKey));
		}

		System.out.println("------- parameter -------");
		for (String key : requestMsg.keySet()) {
			for (int i = 0; i < requestMsg.get(key).length; i++) {
			// 打印所有请求参数值
				System.out.println("key=" + key + ";value=" + requestMsg.get(key)[i].toString());
			}
		}
		return retrunValue;
	}
	
	//获取accessToken
	// mock /platform/json/auth/accessToken
	@RequestMapping(value = "/platform/json/auth/accessToken", method = RequestMethod.GET)
	@ResponseBody
	public String accessToken() {
		String retrunValue = "{\n" + "    \"result\":0,\n"
				+ "    \"accessToken\":\"PbcsjdQ1w_VFzHu08rqk4CNnGUV2URKG\",\n" + "    \"desc\":\"获取accessToken成功\"\n"
				+ "}\n" + "";
		JSONObject object1 = JSON.parseObject(retrunValue);
		return String.valueOf(object1);
	}

	
	//获取设备信息
	// mock /platform/json/cloudgateway/devices/{deviceId}
	@RequestMapping(value = "/platform/json/cloudgateway/devices/{deviceId}", method = RequestMethod.GET)
	@ResponseBody
	public String deviceId(@PathVariable String deviceId) {
		String retrunValue = "{\n" + "  \"resultCode\": 0,\n" + "  \"device\": {\n" + "    \"deviceId\": \"" + deviceId
				+ "\",\n" + "    \"deviceType\": \"30187\",\n" + "    \"deviceToken\": \"qKF9lnapRHykPdPJ\",\n"
				+ "    \"firmwareVersion\": \"XHY-X3\",\n" + "    \"softwareVersion\": \"v3.01.10\",\n"
				+ "    \"ipAddress\": \"192.168.0.106\",\n" + "    \"onlineStatus\": 1,\n"
				+ "    \"registTime\": 734354788,\n" + "    \"XData\": \"****\"\n" + "  },\n"
				+ "  \"childDevices\": [\n" + "    {\n" + "      \"deviceId\": \"testestese\",\n"
				+ "      \"deviceType\": \"301877\",\n" + "     \"deviceToken\": \"qKF9lnapRHykPdPJ\",\n"
				+ "     \"firmwareVersion\": \"XHY-X3\",\n" + "     \"softwareVersion\": \"v3.01.10\",\n"
				+ "     \"ipAddress\": \"192.168.0.106\",\n" + "     \"onlineStatus\": 1,\n"
				+ "     \"registTime\": 734354788,\n" + "     \"XData\": \"****\"\n" + "    }\n" + "  ]\n" + "}\n" + "";
		// System.out.println("deviceId="+deviceId);
		JSONObject object1 = JSON.parseObject(retrunValue);
		return String.valueOf(object1);
	}
	
	
	//获取设备类型
	// mock /platform/json/deviceTypes?deviceType=30187
	@RequestMapping(value = "/platform/json/deviceTypes", method = RequestMethod.GET)
    @ResponseBody
    public String deviceType(@RequestParam(name = "deviceType")  String deviceType) {
		String retrunValue = "{\n" + "  \"resultCode\": 0,\n" + "  \"deviceTypeInfo\": [\n" + "    {\n"
				+ "      \"deviceType\":\""+deviceType+"\",\n" + "      \"productToken\":\"Hk0RzEUXOErzcl6J\",\n"
				+ "      \"andlinkToken\":\"ZCcDV3Jo9BaeE7tP\",\n" + "      \"typeStatus\":123\n" + "    }\n" + "  ]\n"
				+ "}"; 
		JSONObject object1 = JSON.parseObject(retrunValue);
		return String.valueOf(object1);
    }
	
	//第三方风控返回
    //mock /api/v1/risk/service
	@RequestMapping(value = "/api/v1/risk/service", method = RequestMethod.POST)
    @ResponseBody
	public String device3(HttpServletRequest request, @RequestBody String info) {
		String retrunValue = "";
		
		String eventID = "";
		JSONObject object = JSON.parseObject(info);
		// 利用键值对的方式获取到值
		try {
			Thread.sleep(time);
			eventID = object.get("eventID").toString();
			logger.info("eventID=" + eventID);
		    retrunValue = "{\"status\":0,\"data\":{\"responseTime\":\"2019-03-27 06:05:17\",\"eventID\":\""+eventID+"\",\"riskScore\":0,\"riskLevel\":0,\"suggestion\":0,\"riskType\":\"\"},\"message\":\"SUCCESS\"}";
			
		} catch (Exception e) {
			retrunValue = "{\"return_code\":\"-400\",\"error\":\"invalid para\"}";
		}
		
		JSONObject object1 = JSON.parseObject(retrunValue);
		return String.valueOf(object1);
    }
	
	
	//小溪压测
	// mock m2m/rest/device
	@RequestMapping(value = "/front/msggw/sendMessageAllInOne", method = RequestMethod.POST)
	@ResponseBody
	public String device(HttpServletRequest request, @RequestBody String info) {
		String retrunValue = "";
		String childDeviceId = "";
		 GetTime();
		 System.out.println("Time="+time);
		try {
			Thread.sleep(90) ;
			retrunValue = "{\"msg\":\"OK\",\"errcode\":0}";

		} catch (Exception e) {
			try {
				retrunValue = "{\n" + "    \"timestamp\":1524034744,\n" + "    \"heartBeatTime\":30001,\n"
						+ "    \"deviceId\":\"" + childDeviceId + "\",\n"
						+ "    \"deviceToken\":\"OxtEVd/Iw5SuV11s\",\n" + "    \"andlinkToken\":\"ZCcDV3Jo9BaeE7tP\"\n"
						+ "}";
				logger.info(retrunValue);
			} catch (Exception e1) {

			}

		}

		JSONObject object1 = JSON.parseObject(retrunValue);
		return String.valueOf(object1);
	}

	public String create_screen_shot_dir() {
		String log_path = "";
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String test_log_folder = formatter.format(date);
		File dir = new File(System.getProperty("user.dir") + "\\output", test_log_folder);

		log_path = dir.getAbsolutePath();
		if (dir.mkdirs()) {
			logger.info("Test log path is:" + log_path);
			return log_path;
		} else {
			logger.info("创建目录" + log_path + "失败！");
			return log_path;
		}

	}
}
