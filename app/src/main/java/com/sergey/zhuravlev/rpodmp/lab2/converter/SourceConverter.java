package com.sergey.zhuravlev.rpodmp.lab2.converter;

import com.sergey.zhuravlev.rpodmp.lab2.dto.SourceDto;
import com.sergey.zhuravlev.rpodmp.lab2.model.Source;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SourceConverter {

    public static Source getSource(SourceDto sourceDto) {
        if (sourceDto == null) {
            return null;
        }
        return new Source(
                null,
                sourceDto.getId(),
                sourceDto.getName(),
                sourceDto.getDescription(),
                sourceDto.getUrl(),
                sourceDto.getCategory(),
                sourceDto.getLanguage(),
                sourceDto.getCountry(),
                sourceDto.getUrlsToLogos().getSmall(),
                sourceDto.getUrlsToLogos().getMedium(),
                sourceDto.getUrlsToLogos().getLarge(),
                sourceDto.getSortBysAvailable()
        );
    }

    public static List<Source> getSources(Collection<SourceDto> sources) {
        List<Source> result = new ArrayList<>();
        for (SourceDto sourceDto : sources) {
            result.add(getSource(sourceDto));
        }
        return result;
    }

}
