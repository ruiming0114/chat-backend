package com.buaa.chat.util;

public class RegExpUtil {

    public static String escapeRegExpSpecialWord(String keyword) {
        if (keyword != null) {
            String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }

}
