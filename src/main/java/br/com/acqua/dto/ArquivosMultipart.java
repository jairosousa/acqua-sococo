package br.com.acqua.dto;

import org.springframework.web.multipart.MultipartFile;

public class ArquivosMultipart {

    MultipartFile fileAvt;

    MultipartFile fileLayout;

    public MultipartFile getFileAvt() {
        return fileAvt;
    }

    public void setFileAvt(MultipartFile fileAvt) {
        this.fileAvt = fileAvt;
    }

    public MultipartFile getFileLayout() {
        return fileLayout;
    }

    public void setFileLayout(MultipartFile fileLayout) {
        this.fileLayout = fileLayout;
    }

}
