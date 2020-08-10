package br.com.acqua.service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.acqua.entity.AvatarProd;
import br.com.acqua.repository.AvatarProdRepository;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class AvatarProdService {

	private static final int IMG_WIDTH = 100;
	private static final int IMG_HEIGHT = 100;

	@Autowired
	private AvatarProdRepository avatarRepository;

	@Transactional(readOnly = false)
	public void saveOrUpdate(AvatarProd avatar) {
		avatarRepository.save(avatar);
	}

	public AvatarProd getAvatarByUpload(MultipartFile file) {
		AvatarProd avatar = new AvatarProd();

		if (file != null && file.getSize() > 0) {
			try {
				avatar.setTitulo(file.getOriginalFilename());
				avatar.setTipo(file.getContentType());
				avatar.setAvatar(file.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {
				avatar.setTipo("image/png");
				avatar.setTitulo("default.png");
				avatar.setAvatar(this.imageToByte());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return avatar;
	}


	public AvatarProd findById(Long id) {
		return avatarRepository.findOne(id);
	}
	
	public byte[] imageToByte() throws IOException {
	    InputStream is = null;
	    byte[] buffer = null;
	    is = new FileInputStream("src/main/resources/static/imagens/default.png");
	    buffer = new byte[is.available()];
	    is.read(buffer);
	    is.close();
	    return buffer;
	}

}
