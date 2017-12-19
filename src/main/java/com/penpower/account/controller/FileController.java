package com.penpower.account.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.penpower.account.service.FileService;
import com.penpower.account.service.serviceImpl.FileServiceImpl;

@CrossOrigin
@RestController
@RequestMapping("/file")
public class FileController {

	@Autowired
	FileService fileServiceImpl;
	private static final String SUCCESS_MESSAGE = "success";

	@RequestMapping(value = "/upload/{guid}", method = RequestMethod.POST)
	public String upload(@RequestParam("file") MultipartFile file, @PathVariable("guid") UUID guid) throws Exception {
		fileServiceImpl.upload(file, guid);
		return SUCCESS_MESSAGE;
	}

	@RequestMapping(value = "/download/{guid}", method = RequestMethod.GET, produces = { MediaType.IMAGE_JPEG_VALUE })
	public Resource download(HttpServletResponse response, @PathVariable("id") UUID guid) throws Exception {
		return fileServiceImpl.download(response, guid);
	}

	@RequestMapping(value = "/removefile/{guid}", method = RequestMethod.POST)
	public String removeFile(@PathVariable("guid") UUID guid) throws Exception {
		fileServiceImpl.removeFile(guid);
		return SUCCESS_MESSAGE;
	}

}
