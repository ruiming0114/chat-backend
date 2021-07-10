package com.buaa.chat.converter;

import com.buaa.chat.module.message.ContentTypeEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class ContentType2StringConverter implements Converter<ContentTypeEnum,String> {

    @Override
    public String convert(ContentTypeEnum contentTypeEnum) {
        return contentTypeEnum.getType();
    }
}
