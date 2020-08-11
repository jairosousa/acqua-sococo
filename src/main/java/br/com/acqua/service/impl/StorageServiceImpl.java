package br.com.acqua.service.impl;

import br.com.acqua.entity.LayoutProd;
import br.com.acqua.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements StorageService {
    @Override
    public LayoutProd store(MultipartFile file) {
        return null;
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Path load(String filename) {
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        return null;
    }
}
