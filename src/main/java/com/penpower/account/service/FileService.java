package com.penpower.account.service;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	void upload(MultipartFile source, UUID guid) throws Exception;

	Resource download(HttpServletResponse response, UUID guid) throws Exception;

	void removeFile(UUID guid) throws Exception;
}
