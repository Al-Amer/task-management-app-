package com.example.taskmanagement;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    public Task createTask(String title, String description, String createdBy) {
        Task task = new Task(title, description, createdBy);
        return taskRepository.save(task);
    }
    public Task createTask(String title, String description) {
        return createTask(title, description, "Unknown");
    }
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }
    public Task updateTask(Long id, String title, String description, String createdBy, boolean done) {
        Task task = getTaskById(id);
        task.setTitle(title);
        task.setDescription(description);
        task.setCreatedBy(createdBy);
        task.setDone(done);
        task.setUpdatedAt(java.time.LocalDateTime.now());
        return taskRepository.save(task);
    }
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
    public Task toggleTaskStatus(Long id) {
        Task task = getTaskById(id);
        task.setDone(!task.isDone());
        task.setUpdatedAt(java.time.LocalDateTime.now());
        return taskRepository.save(task);
    }
    public List<Task> getTasksByStatus(boolean done) {
        return taskRepository.findByDone(done);
    }
    public List<Task> getTasksByCreator(String creator) {
        return taskRepository.findByCreatedBy(creator);
    }
    public void createSampleTasks() {
        if (taskRepository.count() == 0) {
            createTask("Learn JavaFX", "Watch tutorials and practice", "Alice");
            createTask("Build Task App", "Create the desktop application", "Bob");
            createTask("Integrate Spring Boot", "Connect everything together", "Alice");
            createTask("Write Documentation", "Document the project", "Charlie");
            createTask("Design UI", "Create wireframes and mockups", "Bob");
            System.out.println("✅ Sample tasks created!");
        }
    }
}