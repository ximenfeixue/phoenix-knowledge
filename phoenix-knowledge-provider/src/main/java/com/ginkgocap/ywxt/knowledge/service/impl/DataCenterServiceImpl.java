package com.ginkgocap.ywxt.knowledge.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.mapper.KnowledgeBaseMapper;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.service.AttachmentService;
import com.ginkgocap.ywxt.knowledge.service.DataCenterService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeReaderService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.Content;
import com.ginkgocap.ywxt.knowledge.util.CopyFile;
import com.ginkgocap.ywxt.knowledge.util.HTTPUrlConfig;
import com.ginkgocap.ywxt.knowledge.util.HTTPUtil;
import com.ginkgocap.ywxt.knowledge.util.process.ExportWatched;
import com.ginkgocap.ywxt.knowledge.util.process.ExportWatcher;
import com.ginkgocap.ywxt.knowledge.util.zip.ZipUtil;
import com.ginkgocap.ywxt.util.DateFunc;
import com.ginkgocap.ywxt.util.Encodes;
import com.ginkgocap.ywxt.util.GetUploadPath;
import com.ginkgocap.ywxt.util.MakePrimaryKey;

@Service("dataCenterService")
public class DataCenterServiceImpl implements DataCenterService {
	@Resource
	private KnowledgeBaseMapper knowledgeBaseMapper;
	@Resource
	private KnowledgeReaderService knowledgeReaderService;
	@Resource
	private AttachmentService attachmentService;
	private static final Logger logger = LoggerFactory
			.getLogger(DataCenterServiceImpl.class);

	@Resource
	private HTTPUrlConfig httpUrlConfig;

	@Override
	public Map<String, Object> getCaseDataFromDataCenter(String path,
			String type) {
		logger.info("进入转换经典案例请求");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (StringUtils.isBlank(path)) {
				result.put(Constants.status, Constants.ResultType.fail.v());
				result.put(Constants.errormessage,
						Constants.ErrorMessage.paramNotBlank.c());
				return result;
			}
			// 封装请求参数
			Map<String, String> params = new HashMap<String, String>();
			params.put("path", path);// 请求路径
			params.put("imgPath", GetUploadPath.getNginxRoot()
					+ "/knowledge/import");
			if (StringUtils.equalsIgnoreCase(type, "doc")
					|| StringUtils.equalsIgnoreCase(type, "docx")
					|| StringUtils.equalsIgnoreCase(type, "rtf")) {
				params.put("type", "word");// 文件类型
			} else if (StringUtils.equalsIgnoreCase(type, "pdf")) {
				params.put("type", "pdf");// 文件类型
			}
			// 返回请求结果
			String str = HTTPUtil.post(httpUrlConfig.getParseUrl() + "data/",
					params);
			// 为空则转换错误
			if (StringUtils.isBlank(str)) {
				logger.error("转换错误,转换返回值为空!");
				result.put(Constants.status, Constants.ResultType.fail.v());
				result.put(Constants.errormessage,
						Constants.ErrorMessage.parseError.c());
				return result;
			}
			ObjectMapper mapper = new ObjectMapper();
			result = mapper.readValue(str, Map.class);
			if (StringUtils.isBlank(result.get("responseData") + "")) {
				result.put(Constants.status, Constants.ResultType.fail.v());
				result.put(Constants.errormessage,
						Constants.ErrorMessage.parseError.c());
				return result;
			} else {
				System.out.println("导出图片:" + str);
				JSONObject json = JSONObject.fromObject(result
						.get("responseData"));
				String ss = "";
				if (json.get("imgdata") != null) {
					ss = json.getString("imgdata");
				}
				if (StringUtils.isNotBlank(ss)) {
					String zipPath = Content.WEBSERVERPATH
							+ Content.EXPORTDOCPATH + File.separator + "IMGZIP"
							+ File.separator
							+ StringUtils.replace(DateFunc.getDate(), ":", "_");
					createDir(zipPath);
					byte[] html = Encodes.decodeBase64(ss);
					File file = new File(zipPath + File.separator
							+ MakePrimaryKey.getPrimaryKey() + ".zip");
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(html);
					fos.close();
					ZipUtil util = new ZipUtil(Content.WEBSERVERPATH
							+ Content.EXPORTDOCPATH + File.separator + "IMGZIP"
							+ "test.zip");
					util.putZip(file.getPath(), Content.EXPORTMOUNTPATH
							+ "/import", 1);
				}
			}
			result.put(Constants.status, Constants.ResultType.success.v());
		} catch (Exception e) {
			logger.error("搜转换经典案例失败{}", e.toString());
			e.printStackTrace();
		}
		logger.info("转换经典案例成功,返回值:{}", path);
		return result;
	}

	@Override
	public Map<String, Object> noticeDataCenterWhileColumnChange(long columnId) {
		logger.info("进入添加栏目通知数据中心请求");
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", columnId + "");

		String str = HTTPUtil.post(httpUrlConfig.getPushUrl() + "column/add",
				params);
		if (StringUtils.isBlank(str)) {
			logger.error("通知失败!栏目ID:{}", columnId);
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.parseError.c());
			return result;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			result = mapper.readValue(str, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("通知失败!栏目ID:{}", columnId);
		}

		result.put(Constants.status, Constants.ResultType.success.v());

		return result;
	}

	/**
	 * 导出知识成文件
	 * 
	 * @param knowledgeIds
	 *            知识ids
	 * @param group
	 *            导出内容 1 所选文章 2 所选附件 3 所选文章及附件
	 * @return liubang
	 */
	@Override
	public Map<String, Object> getExportFile(long uid, String knowledgeIds,
			String group, String nfsHome, String taskId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String[] knowledgeIdArr = StringUtils.split(knowledgeIds, ",");
		// 已导出正确的文章列表
		List<String> exportArticleList = new ArrayList<String>();
		// 导出时出错的文章列表
		List<String> errorArticleList = new ArrayList<String>();
		// 需要压缩的路径
		String zipPath = Content.WEBSERVERPATH + Content.EXPORTDOCPATH
				+ File.separator + "GENPATH_" + uid + File.separator
				+ StringUtils.replace(DateFunc.getDate(), ":", "_");

		if (knowledgeIdArr != null && knowledgeIdArr.length > 0) {
			int taskNum = knowledgeIdArr.length + 1 + 1;
			// 执行到当前的序号
			int currentNum = 1;
			// 被监听对象
			ExportWatched watched = new ExportWatched();
			// 设置监听对象的任务标识
			watched.setTaskId(taskId);
			// 设置监听任务的工作总数 加1为压缩的任务
			watched.setTotal(taskNum);
			// 将监听任务存放的Hash表中
			Content.MAP.put(taskId, watched);
			watched.changeData(currentNum, "导出准备", false);
			for (String id : knowledgeIdArr) {
				currentNum++;
				if (StringUtils.isNotBlank(id)) {
					List<String> pathList = new ArrayList<String>();
					logger.info("获取导出的知识对象：");
					KnowledgeBase b = knowledgeBaseMapper
							.selectByPrimaryKey(Long.parseLong(id));
					if (b != null) {
						File articleDir = createDir(zipPath + File.separator
								+ b.getTitle(), 0);
						copyFile(b, articleDir, group, nfsHome,
								exportArticleList, errorArticleList,
								currentNum, watched);
					} else {
						watched.changeData(currentNum, "知识不存在", false);
					}
				}
			}
			// 压缩文件
			try {
				String zipOutPath = Content.EXPORTMOUNTPATH;
				String downloadPath = uid + "_exportfile_multiple.zip";
				// 创建压缩输出的路径
				createDir(zipOutPath);
				// 初始化压缩工具
				ZipUtil util = new ZipUtil(zipOutPath + File.separator + uid
						+ "_exportfile_multiple.zip");
				// 压缩文件夹
				util.put(new String[] { zipPath });
				// 压缩文件说明
				String rem = "\nfiles:" + util.getFileCount()
						+ " ! sources:www.gintong.com ";
				util.comment.append(rem);
				// 设置压缩文件说明
				util.setComment(util.comment.toString());
				// 关闭压缩工具流
				util.close();
				// 设置监听为压缩成功
				watched.changeData(taskNum, "文件压缩", true);
				// 设置压缩路径到监听对象以便下载使用
				watched.setDownloadPath(downloadPath);
				// 设置整个任务成功
				map.put("result", "success");
			} catch (IOException e) {
				// 设置监听为压缩失败
				watched.changeData(taskNum, "文件压缩", false);
				// 设置整个任务失败
				map.put("result", "error");
				e.printStackTrace();
			}
			// 设置整个任务完成
			watched.setDone(true);
		}
		// 导出的文章
		map.put("export", exportArticleList);
		// 错误未导出的文章
		map.put("errexport", errorArticleList);
		return map;
	}

	@Override
	public Map<String, Object> processView(String taskId) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 从hash表中得到被监听的对象
		ExportWatched watched = (ExportWatched) Content.MAP.get(taskId);
		// 通过被监听对象初始化监听对象
		ExportWatcher w = new ExportWatcher(watched);
		// 得到被监听对象状态
		w.update(watched, "");
		// 假如事务完成则从Hash表中清楚被监听对象
		if (watched.isDone()) {
			Content.MAP.remove(taskId);
			System.out.println("监听列表移除:" + (Content.MAP.get(taskId) == null));
		}
		map.put("mes", w.getMes());
		map.put("downloadPath", w.getProgen());
		return map;
	}

	private void copyFile(KnowledgeBase b, File articleDir, String group,
			String nfsHome, List<String> exportArticleList,
			List<String> errorArticleList, int currentNum, ExportWatched watched) {
		// 当导出文章时生成word的名称为文章标题的名称，当导出附件时，多个附件时一个文章有多个附件时用文件夹存放，文件夹命名为文章
		String title = b.getTitle();
		try {
			// 生成文章包及内容
			if (StringUtils.equals("1", group)
					|| StringUtils.equals("3", group)) {
				// 生成附件的目录
				File knowledgeDir = createDir(articleDir.getPath()
						+ File.separator + "正文", 0);
				// 获取大数据生成的知识文档
				// TODO
				Knowledge k = knowledgeReaderService.getKnowledgeById(
						b.getKnowledgeId(), String.valueOf(b.getColumnType()));
				Map<String, String> params = new HashMap<String, String>();
				params.put("path", k.getContent());
				params.put("type", "html");
				params.put("imgPath", GetUploadPath.getNginxRoot()
						+ "/knowledge/import");
				String str = HTTPUtil.post(httpUrlConfig.getParseUrl()
						+ "data/", params);
				Map<String, Object> result = new HashMap<String, Object>();
				if (StringUtils.isBlank(str)) {
					logger.error("转换错误,转换返回值为空!");
					result.put(Constants.status, Constants.ResultType.fail.v());
					result.put(Constants.errormessage,
							Constants.ErrorMessage.parseError.c());
				}
				ObjectMapper mapper = new ObjectMapper();
				result = mapper.readValue(str, Map.class);
				System.out.println("导出附件:" + str);
				if (result != null && result.get("responseData") != null) {
					JSONObject json = JSONObject.fromObject(result
							.get("responseData"));
					String ss = "";
					if (json.get("data") != null) {
						ss = json.getString("data");
					}
					if (StringUtils.isBlank(ss)) {
						ss = result.get("responseData").toString();
					}
					byte[] html = Encodes.decodeBase64(ss);
					File file = new File(knowledgeDir.getPath()
							+ File.separator + b.getTitle() + ".doc");
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(html);
					fos.close();
				}
			}
			// 生成附件包及内容
			if (StringUtils.equals("2", group)
					|| StringUtils.equals("3", group)) {
				// 生成附件的目录
				File fileDir = createDir(articleDir.getPath() + File.separator
						+ "附件", 0);
				// 通过文章得到附件列表
				if (StringUtils.isNotBlank(b.getTaskid())) {
					Map<String, Object> attMap = attachmentService
							.queryAttachmentByTaskId(b.getTaskid());
					if (attMap != null && attMap.get("attList") != null) {
						List<FileIndex> attList = (List<FileIndex>) attMap
								.get("attList");
						if (attList != null && attList.size() > 0) {
							for (FileIndex att : attList) {
								// 将挂载点的文件拷贝到包中
								File source = new File(nfsHome
										+ att.getFilePath());
								File target = new File(fileDir.getPath()
										+ File.separator + att.getFileTitle());
								// 将文件拷贝到目录中
								CopyFile.copyFile(source, target);
							}
						}
					}
				}
			}
			// 将转换完毕的文章对象存放到列表中
			exportArticleList.add(title);
			watched.changeData(currentNum, title, true);
		} catch (Exception e) {
			errorArticleList.add(title);
			watched.changeData(currentNum, title, false);
			e.printStackTrace();
			logger.error("拷贝附件出错", e);
		}
	}

	private File createDir(String path, int index) {
		String oldPath = path;
		if (index != 0) {
			path = path + "(" + index + ")";
		}
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
			return f;
		} else {
			index++;
			return createDir(oldPath, index);
		}
	}

	private File createDir(String path) {
		File f = new File(path);
		if (!f.exists())
			f.mkdirs();
		return f;
	}

	@Override
	public Map<String, Object> noticeDataCenterWhileKnowledgeChange(String kId,
			String oper, String type) {
		logger.info("进入知识通知数据中心请求,知识Id:{},知识类型:{}", kId, type);
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", kId);
		params.put("oper", oper);
		params.put("type", type);

		String str = HTTPUtil.post(httpUrlConfig.getSearchUrl()
				+ "knowledge/operation.json", params);
		if (StringUtils.isBlank(str)) {
			logger.error("通知失败!知识ID:{}", kId);
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.parseError.c());
			return result;
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			result = mapper.readValue(str, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("通知失败!知识ID:{}", kId);
		}

		result.put(Constants.status, Constants.ResultType.success.v());
		logger.error("通知成功!知识ID:{},知识类型:{}", kId, type);

		return result;
	}

	@Override
	public Map<String, Object> noticeDataCenterWhileFileChange(
			Map<String, Object> paramsMap) {
		logger.info("进入通知转换经典案例请求,知识Id:{}", paramsMap.get("kid"));
		Map<String, Object> result = new HashMap<String, Object>();

		if (StringUtils.isBlank(paramsMap.get("path") + "")) {
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage,
					Constants.ErrorMessage.paramNotBlank.c());
			return result;
		}

		try {
			// 封装请求参数
			Map<String, String> params = new HashMap<String, String>();
			params.put("path", paramsMap.get("path") + "");// 请求路径
			params.put("kid", paramsMap.get("kid") + "");
			params.put("docType", paramsMap.get("docType") + "");
			params.put("type", paramsMap.get("type") + "");
			params.put("imgPath", GetUploadPath.getNginxRoot()
					+ "/knowledge/import");
			// 返回请求结果
			String str = HTTPUtil.post(httpUrlConfig.getParseUrl() + "async_data/",
					params);
			// 为空则转换错误
			if (StringUtils.isBlank(str)) {
				logger.error("转换错误,转换返回值为空!知识ID为:{}", paramsMap.get("kid"));
				result.put(Constants.status, Constants.ResultType.fail.v());
				result.put(Constants.errormessage,
						Constants.ErrorMessage.parseError.c());
				return result;
			}

			ObjectMapper mapper = new ObjectMapper();
			result = mapper.readValue(str, Map.class);

		} catch (Exception e) {
			logger.error("搜转换经典案例失败{}", e.toString());
			e.printStackTrace();
		}
		if (Integer.parseInt(result.get("status") + "") == Constants.ResultType.success
				.v()) {

			logger.info("通知成功,id为：{}", paramsMap.get("id"));
		}
		return result;
	}

	@Override
	public Map<String, Object> getHotDataSortByType(String type) {
		logger.info("进入获取最热排行请求,知识type:{}", type);
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			// 封装请求参数
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", type);
			// 返回请求结果
			String str = HTTPUtil.post(httpUrlConfig.getHotSortUrl()
					+ "knowledge/hot/all.json", params);
			// 为空则转换错误
			if (StringUtils.isBlank(str)) {
				logger.error("获取最热排行请求失败,", type);
				result.put(Constants.status, Constants.ResultType.fail.v());
				result.put(Constants.errormessage,
						Constants.ErrorMessage.parseError.c());
				return result;
			}

			List<Map<String, Object>> list = getKnowledgeList(str);

			result.put("list", list);
			result.put(Constants.status, Constants.ResultType.success.v());

		} catch (Exception e) {
			logger.error("最热排行请求失败{}", e.toString());
			e.printStackTrace();
		}
		if ((result.get("result") + "").equals("true")) {

			logger.info("获取最热排行请求成功,type为：{}", type);
		}
		return result;
	}

	@Override
	public Map<String, Object> getCommentDataSortByType(String type) {
		logger.info("进入获取评论排行请求,知识type:{}", type);
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			// 封装请求参数
			Map<String, String> params = new HashMap<String, String>();
			params.put("type", type);
			// 返回请求结果
			String str = HTTPUtil.post(httpUrlConfig.getCommentSortUrl()
					+ "knowledge/hot/comment.json", params);
			// 为空则转换错误
			if (StringUtils.isBlank(str)) {
				logger.error("获取评论排行请求失败,", type);
				result.put(Constants.status, Constants.ResultType.fail.v());
				result.put(Constants.errormessage,
						Constants.ErrorMessage.parseError.c());
				return result;
			}

			List<Map<String, Object>> list = getKnowledgeList(str);
			result.put("list", list);
			result.put(Constants.status, Constants.ResultType.success.v());
		} catch (Exception e) {
			logger.error("评论排行请求失败{}", e.toString());
			e.printStackTrace();
		}
		if ((result.get("result") + "").equals("true")) {

			logger.info("获取评论排行请求成功,type为：{}", type);
		}
		return result;
	}

	/**
	 * 获取知识列表
	 * @param str json字符串
	 * @return
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 */
	private List<Map<String, Object>> getKnowledgeList(String str)
			throws IOException, JsonParseException, JsonMappingException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ObjectMapper mapper = new ObjectMapper();
		// 转换bsn为MAP
		Map<String, Object> returnParams = mapper.readValue(str, Map.class);
		String bsn = returnParams.get("bsn") + "";
		JSONObject bsnObjects = JSONObject.fromObject(bsn);
		Map<String, Object> bsnMaps = (Map<String, Object>) bsnObjects;

		// 解析bsnMaps
		for (int i = 1; i < bsnMaps.size() + 1; i++) {
			String bsnStr = bsnMaps.get(i + "") + "";
			if (StringUtils.isBlank(bsnStr)
					|| StringUtils.equalsIgnoreCase(bsnStr, "null")) {
				continue;
			}
			JSONObject bsnMap = JSONObject.fromObject(bsnStr);
			// 将字符串生成单个MAP
			Map<String, Object> singleMap = (Map<String, Object>) bsnMap;
			if (singleMap != null) {
				list.add(singleMap);
			}
		}
		return list;
	}

}