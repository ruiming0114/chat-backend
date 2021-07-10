package com.buaa.chat.converter;

import com.buaa.chat.module.message.ContentTypeEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class String2ContentTypeConverter implements Converter<String, ContentTypeEnum> {

    @Override
    public ContentTypeEnum convert(String s) {
        return ContentTypeEnum.getEnum(s);
    }
}
