package com.buaa.chat.converter;

import com.buaa.chat.module.message.MessageTypeEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class String2MessageTypeConverter implements Converter<String, MessageTypeEnum> {

    @Override
    public MessageTypeEnum convert(String s) {
        return MessageTypeEnum.getEnum(s);
    }
}
