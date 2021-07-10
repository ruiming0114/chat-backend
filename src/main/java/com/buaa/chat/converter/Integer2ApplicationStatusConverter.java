package com.buaa.chat.converter;

import com.buaa.chat.module.contacts.ApplicationStatusEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class Integer2ApplicationStatusConverter implements Converter<Integer, ApplicationStatusEnum> {

    @Override
    public ApplicationStatusEnum convert(Integer integer) {
        return ApplicationStatusEnum.valueOf(integer);
    }
}
