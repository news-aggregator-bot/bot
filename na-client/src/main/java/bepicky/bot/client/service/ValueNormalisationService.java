package bepicky.bot.client.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ValueNormalisationService implements IValueNormalisationService {

    @Override
    public String normalise(String value) {
        return StringUtils.isBlank(value) ? "" : value;
    }
}
