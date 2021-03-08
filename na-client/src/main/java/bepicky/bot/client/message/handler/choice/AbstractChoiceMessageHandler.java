package bepicky.bot.client.message.handler.choice;

import bepicky.bot.core.message.button.InlineMarkupBuilder;
import bepicky.bot.core.cmd.ChatCommand;
import bepicky.bot.core.cmd.CommandManager;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.client.message.handler.context.ChatChainLink;
import bepicky.bot.client.message.handler.context.ChatChainManager;
import bepicky.bot.client.message.template.ButtonNames;
import bepicky.bot.core.message.handler.EntityCallbackMessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.bot.client.service.ICategoryService;
import bepicky.bot.client.service.IReaderService;
import bepicky.bot.core.message.template.TemplateUtils;
import bepicky.common.domain.dto.CategoryDto;
import bepicky.common.domain.dto.ReaderDto;
import bepicky.common.domain.response.CategoryListResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractChoiceMessageHandler implements EntityCallbackMessageHandler {

    private final Map<Long, Queue<CategoryDto>> readersRegions = new ConcurrentHashMap<>();

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IReaderService readerService;

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    private CommandManager commandManager;

    @Autowired
    protected ChatChainManager chainManager;

    @Override
    public HandleResult handle(ChatCommand command) {
        Queue<CategoryDto> regions = getQueue(command.getChatId());
        int size = regions.size();

        CategoryDto region = regions.poll();
        ReaderDto reader = readerService.find(command.getChatId());

        String yesText = templateContext.processTemplate(ButtonNames.YES, reader.getLang());
        String noText = templateContext.processTemplate(ButtonNames.NO, reader.getLang());
        InlineMarkupBuilder markup = new InlineMarkupBuilder();
        String pickCmd = commandManager.pick(entityType(), region.getId());

        ChatChainLink current = chainManager.current(command.getChatId());
        ChatChainLink nextLink = chainManager.next(command.getChatId());
        if (size > 1) {
            String choiceCmd = current.getCommand();
            markup.addButtons(Arrays.asList(
                markup.button(yesText, pickCmd, choiceCmd),
                markup.button(noText, choiceCmd)
            ));
        } else {
            readersRegions.remove(command.getChatId());
            markup.addButtons(
                Arrays.asList(
                    markup.button(yesText, pickCmd, commandManager.goNext(), nextLink.getCommand()),
                    markup.button(noText, commandManager.goNext(), nextLink.getCommand())
                )
            );
        }

        String doneText = templateContext.processEmojiTemplate(
            ButtonNames.DIR_DONE,
            reader.getLang()
        );
        String messageText = templateContext.processTemplate(
            current.getMsgKey(),
            reader.getLang(),
            TemplateUtils.params("size", size, "name", buildName(region))
        );

        markup.addButton(markup.button(doneText, commandManager.goNext(), nextLink.getCommand()));
        return new HandleResult(messageText, markup.build());
    }

    @Override
    public CommandType commandType() {
        return CommandType.CHOICE;
    }

    protected abstract String categoryType();

    private Queue<CategoryDto> getQueue(long chatId) {
        if (!readersRegions.containsKey(chatId)) {
            CategoryListResponse response = categoryService.list(chatId, categoryType());
            List<CategoryDto> sortedList = response.getList().stream()
                .limit(30)
                .sorted(Comparator.comparing(Function.identity(), this::compareNames))
                .collect(Collectors.toList());
            readersRegions.put(chatId, new LinkedList<>(sortedList));
        }
        return readersRegions.get(chatId);
    }

    private int compareNames(CategoryDto d1, CategoryDto d2) {
        return buildName(d1).compareTo(buildName(d2));
    }

    private String buildName(CategoryDto dto) {
        return dto.getParent() != null ? buildName(dto.getParent()) + "/" + dto.getLocalised() : dto.getLocalised();
    }
}
