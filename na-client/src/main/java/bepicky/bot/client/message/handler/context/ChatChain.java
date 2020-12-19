package bepicky.bot.client.message.handler.context;

import java.util.Arrays;
import java.util.List;

public class ChatChain {

    private final List<ChatChainLink> chain;

    private int currentPos = 0;

    public ChatChain(ChatChainLink... links) {
        this.chain = Arrays.asList(links);
    }

    public ChatChainLink current() {
        return chain.get(currentPos);
    }

    public ChatChainLink next() {
        if (chain.size() <= currentPos + 1) {
            return null;
        }
        return chain.get(currentPos + 1);
    }

    public ChatChainLink goNext() {
        if (chain.size() <= currentPos + 1) {
            return null;
        }
        return chain.get(++currentPos);
    }

    public ChatChainLink previous() {
        if (0 > currentPos - 1) {
            return null;
        }
        return chain.get(currentPos - 1);
    }

    public ChatChainLink goPrevious() {
        if (0 > currentPos - 1) {
            return null;
        }
        return chain.get(--currentPos);
    }
}
