package com.pathz.web.dvitter.repo;

import com.pathz.web.dvitter.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {
    List<Message> findByTag(String tag);
}
