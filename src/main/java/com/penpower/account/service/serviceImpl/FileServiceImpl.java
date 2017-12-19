package com.penpower.account.service.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.penpower.account.dao.AccountDao;
import com.penpower.account.exceptionhandler.exception.NotFoundException;
import com.penpower.account.entity.Account;
import com.penpower.account.service.FileService;

@Service
@Transactional
public class FileServiceImpl implements FileService {

	@Autowired
	private AccountDao accountDao;

	private static final String FILE_NOT_FOUND_MESSAGE = "File not found";
	private static final String ACCOUNT_NOTFOUND_MESSAGE_WITH_ID = "Account not exist(id = %d)";
	private static final String FILE_EXTENTION = "_selfie.jpg";
	private static final String FILE_UPLOAD_PATH = "E:\\upload_target\\";

	private void saveFilePathToDb(UUID guid, String completeFilePath) {
		Account account = accountDao.findByGuid(guid);
		account.setFilePath(completeFilePath);
		accountDao.save(account);
	}

	public void upload(MultipartFile source, UUID guid) throws Exception {
		if (accountDao.existsByGuid(guid)) {
			if (source.isEmpty()) {
				throw new NotFoundException(FILE_NOT_FOUND_MESSAGE);
			}
			String completeFilePath = FILE_UPLOAD_PATH + guid + FILE_EXTENTION;

			Path target = Paths.get(completeFilePath);
			Files.copy(source.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
			saveFilePathToDb(guid, completeFilePath);
		} else {
			throw new NotFoundException(String.format(ACCOUNT_NOTFOUND_MESSAGE_WITH_ID, guid));
		}
	}

	public Resource download(HttpServletResponse response, UUID guid) throws Exception {
		if (accountDao.existsByGuid(guid)) {
			String filePath = accountDao.findByGuid(guid).getFilePath();
			if (null == filePath) {
				throw new NotFoundException();
			}
			File source = new File(filePath);
			response.setContentType(MediaType.IMAGE_JPEG_VALUE);
			response.setHeader("Content-Disposition", "attachment; filename=" + source.getName());
			response.setHeader("Content-Length", String.valueOf(source.length()));
			return new FileSystemResource(source);

		} else {
			throw new NotFoundException(String.format(ACCOUNT_NOTFOUND_MESSAGE_WITH_ID, guid));
		}
	}

	public void removeFile(UUID guid) throws Exception {
		if (accountDao.existsById(guid)) {
			Account account = accountDao.findByGuid(guid);
			String fileBase64 = account.getFilePath();
			if (null == fileBase64) {
				throw new NotFoundException(FILE_NOT_FOUND_MESSAGE);
			}
			Path path = Paths.get(FILE_UPLOAD_PATH + guid + FILE_EXTENTION);
			Files.delete(path);
			account.setFilePath(null);
			accountDao.save(account);
		} else {
			throw new NotFoundException(String.format(ACCOUNT_NOTFOUND_MESSAGE_WITH_ID, guid));
		}
	}
}
