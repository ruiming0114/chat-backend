package com.buaa.chat.converter;

import com.buaa.chat.module.message.MessageTypeEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class MessageType2StringConverter implements Converter<MessageTypeEnum,String> {

    @Override
    public String convert(MessageTypeEnum messageTypeEnum) {
        return messageTypeEnum.getType();
    }
}
