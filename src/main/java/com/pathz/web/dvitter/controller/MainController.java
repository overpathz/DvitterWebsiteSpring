package com.pathz.web.dvitter.controller;

import com.pathz.web.dvitter.domain.Message;
import com.pathz.web.dvitter.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {

    private final MessageRepo messageRepo;

    @Autowired
    public MainController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @GetMapping
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/messages")
    public String getMessages(Model model) {
        model.addAttribute("messages", messageRepo.findAll());
        return "list";
    }

    @PostMapping("/messages")
    public String addMessages(@RequestParam String text,
                              @RequestParam String tag) {

        messageRepo.save(Message.builder().text(text).tag(tag).build());

        return "redirect:/messages";
    }

    @PostMapping("/delete")
    public String deleteAllMessages() {
        messageRepo.deleteAll();
        return "redirect:/messages";
    }

    @PostMapping("/filter")
    public String getFilteredMessages(@RequestParam String filter, Model model) {
        List<Message> filteredMessageList = messageRepo.findByTag(filter);

        if (!filteredMessageList.isEmpty()) {
            model.addAttribute("messages", filteredMessageList);
            return "list";
        }

        model.addAttribute("messages", messageRepo.findAll());
        return "list";
    }
}
