package org.notifier.notificationservice.service;

import org.springframework.stereotype.Service;

@Service
public class MessageComposer {

    public String composeCourseMessage(Course course) {
        return String.format(
                "🎓 *Новый курс!* 🎓\n" +
                        "**%s**\n" +
                        "%s\n\n" +
                        "[Ссылка на курс](%s)\n" +
                        "Категория: #%s",
                course.getTitle(),
                course.getDescription(),
                course.getUrl(),
                course.getCategory()
        );
    }
}
