package bepicky.bot.client.feign;

import bepicky.common.domain.dto.ReaderDto;
import bepicky.common.domain.dto.StatusReaderDto;
import bepicky.common.domain.request.ReaderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "bepicky-service", contextId = "reader-client", configuration = FeignClientConfiguration.class)
public interface ReaderServiceClient {

    @PostMapping("/reader/register")
    ReaderDto register(ReaderRequest reader);

    @GetMapping("/reader/{chatId}")
    ReaderDto find(@PathVariable("chatId") Long chatId);

    @PutMapping("/reader/enable/{chatId}")
    ReaderDto enableReader(@PathVariable("chatId") long id);

    @PutMapping("/reader/disable/{chatId}")
    ReaderDto disableReader(@PathVariable("chatId") long id);

    @GetMapping("/reader/status/{chatId}")
    StatusReaderDto getStatus(@PathVariable("chatId") long id);
}
