package com.buaa.chat.converter;

import com.buaa.chat.module.contacts.ApplicationStatusEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class ApplicationStatus2IntegerConverter implements Converter<ApplicationStatusEnum,Integer> {

    @Override
    public Integer convert(ApplicationStatusEnum applicationStatusEnum) {
        return applicationStatusEnum.getCode();
    }
}
