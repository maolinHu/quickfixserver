package com.gatebe.quickfixserver.controller;

import io.allune.quickfixj.spring.boot.starter.template.QuickFixJTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quickfix.*;
import quickfix.field.*;

import java.util.HashMap;
import java.util.Map;

import static java.util.UUID.randomUUID;
import static quickfix.FixVersions.BEGINSTRING_FIX41;
import static quickfix.FixVersions.BEGINSTRING_FIXT11;


/**
 * @author: huml
 * @date: 2021/7/13 14:30
 * @description: test
 */
@Slf4j
@RequestMapping("/fix")
@RestController
public class TestController {
    private static final Map<String, Map<String, Message>> messageMap = createMessageMap();
    @Autowired
    private QuickFixJTemplate serverQuickFixJTemplate;
    @Autowired
    private Acceptor serverAcceptor;

    @GetMapping("/send-message-to-client")
    public String toClient() throws Exception {
        Map<String, Message> stringMessageMap = messageMap.get(BEGINSTRING_FIXT11);
        Message message = stringMessageMap.get("Quote");
        message.setField(new StringField(Text.FIELD, "Text: " + randomUUID().toString()));

        SessionID sessionID = serverAcceptor.getSessions().stream()
                .filter(id -> id.getBeginString().equals(BEGINSTRING_FIXT11))
                .findFirst()
                .orElseThrow(RuntimeException::new);

        return "success: " + serverQuickFixJTemplate.send(message, sessionID);
    }


    @GetMapping("/test")
    public String test() throws Exception {
        log.info("test");
        return "success";
    }

    private static HashMap<String, Map<String, Message>> createMessageMap() {
        HashMap<String, Map<String, Message>> stringMapHashMap = new HashMap<>();
        stringMapHashMap.put(BEGINSTRING_FIX41, initialiseFix41MessageMap());
        stringMapHashMap.put(BEGINSTRING_FIXT11, initialiseFix50MessageMap());
        return stringMapHashMap;
    }

    private static Map<String, Message> initialiseFix41MessageMap() {
        Map<String, Message> messageMap = new HashMap<>();
        messageMap.put("OrderCancelRequest", new quickfix.fix41.OrderCancelRequest(
                new OrigClOrdID("123"),
                new ClOrdID("321"),
                new Symbol("LNUX"),
                new Side(Side.BUY)));
        return messageMap;
    }

    private static Map<String, Message> initialiseFix50MessageMap() {
        Map<String, Message> messageMap = new HashMap<>();
        messageMap.put("Quote", new quickfix.fix50.Quote(new QuoteID("123")));
        return messageMap;
    }

}
