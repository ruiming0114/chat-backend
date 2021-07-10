package com.buaa.chat.module.image.service;

import com.buaa.chat.exception.MyException;
import com.buaa.chat.response.StatusEnum;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import net.coobird.thumbnailator.Thumbnails;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;

@Service
public class ImageService {

    @Resource
    private GridFsTemplate gridFsTemplate;

    @Resource
    private GridFSBucket gridFSBucket;

    public String uploadWithCompress(MultipartFile file, int size) throws MyException {
        try {
            InputStream inputStream = compress(file, size);
            ObjectId id = gridFsTemplate.store(inputStream, Objects.requireNonNull(file.getOriginalFilename()));
            return id.toString();
        } catch (IOException e) {
            throw new MyException(StatusEnum.IMAGE_UPLOAD_ERROR);
        }
    }

    public String uploadWithoutCompress(MultipartFile file) throws MyException {
        try {
            InputStream inputStream = file.getInputStream();
            ObjectId id = gridFsTemplate.store(inputStream, Objects.requireNonNull(file.getOriginalFilename()));
            return id.toString();
        } catch (IOException e) {
            throw new MyException(StatusEnum.IMAGE_UPLOAD_ERROR);
        }
    }

    public byte[] download(String id) throws Exception {
        GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(id)));
        if (file != null) {
            GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(file.getObjectId());
            GridFsResource resource = new GridFsResource(file, downloadStream);
            InputStream inputStream = resource.getInputStream();
            return getBytes(inputStream);
        }
        return null;
    }

    private InputStream compress(MultipartFile file, int size) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream()).scale(1.0f).outputQuality(0.9f).outputFormat("jpeg").toOutputStream(bos);
        while (bos.toByteArray().length > size * 1024){
            System.out.println(bos.toByteArray().length);
            ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
            Thumbnails.of(new ByteArrayInputStream(bos.toByteArray())).scale(0.6f).outputQuality(0.6f).outputFormat("jpeg").toOutputStream(bos2);
            bos = bos2;
        }
        return new ByteArrayInputStream(bos.toByteArray());
    }

    private byte[] getBytes(InputStream inputStream) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int i = 0;
        while (-1 != (i = inputStream.read(b))) {
            bos.write(b, 0, i);
        }
        return bos.toByteArray();
    }
}
