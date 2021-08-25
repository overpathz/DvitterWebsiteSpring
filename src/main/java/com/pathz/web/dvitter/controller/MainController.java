package com.pathz.web.dvitter.controller;

import com.pathz.web.dvitter.domain.entity.Message;
import com.pathz.web.dvitter.domain.entity.User;
import com.pathz.web.dvitter.repo.MessageRepo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class MainController {

    private final MessageRepo messageRepo;

    public MainController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @GetMapping
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/messages")
    public String getMessages(Model model, @AuthenticationPrincipal User user, HttpServletResponse httpServletResponse) {
        model.addAttribute("messages", messageRepo.findAll());
        model.addAttribute("current_user", user.getUsername());
        return "list";
    }

    @PostMapping("/messages")
    public String addMessages(@RequestParam String text,
                              @RequestParam String tag,
                              @AuthenticationPrincipal User user) {

        Message messageToSave = Message.builder().text(text).tag(tag).build();

        messageRepo.save(messageToSave);

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
        model.addAttribute("no_such_messages", "There're no messages with given filter");
        return "list";
    }
}
