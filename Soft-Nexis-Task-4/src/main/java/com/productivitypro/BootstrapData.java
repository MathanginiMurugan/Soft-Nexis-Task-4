package com.productivitypro;

import com.productivitypro.model.Task;
import com.productivitypro.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BootstrapData implements CommandLineRunner {
    private final TaskRepository repo;

    public BootstrapData(TaskRepository repo) { this.repo = repo; }

    @Override
    public void run(String... args) throws Exception {
        repo.save(new Task("Finish report", "Monthly report", LocalDate.now().plusDays(3)));
        repo.save(new Task("Team meeting", "Sprint planning", LocalDate.now().plusDays(1)));
    }
}

