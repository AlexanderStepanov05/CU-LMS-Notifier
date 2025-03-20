# Telegram Notification Bot для LMS
<!---
🚀 **Высоконагруженный микросервисный бот для уведомлений из LMS Центрального университета**  
Бот обеспечивает асинхронную доставку уведомлений из Learning Management System (LMS) в Telegram, используя современный стек технологий и архитектурные паттерны.

---

## 📋 Содержание
1. [Архитектура](#архитектура)
2. [Технологический стек](#технологии)
3. [Особенности реализации](#особенности)
4. [Запуск проекта](#запуск)
5. [API Endpoints](#api)
6. [Мониторинг и логирование](#мониторинг)
7. [Примеры использования](#примеры)
8. [Планы развития](#планы)

---

<a name="архитектура"></a>
## 🌐 Архитектура

Система построена по **микросервисной архитектуре** с четким разделением ответственности:
- **Telegram Bot Service**: Взаимодействие с Telegram API, обработка команд пользователей.
- **Auth Service**: Управление BFF-токенами, аутентификация.
- **LMS Gateway Service**: Получение данных из LMS через BFF-токен.
- **Notification Service**: Обработка и маршрутизация уведомлений.
- **Config Service**: Централизованное управление конфигурациями.
- **Kafka**: Асинхронная шина событий для уведомлений (опционально).

---

<a name="технологии"></a>
## 🛠 Технологический стек

### Бэкенд
- **Java 17**: Основной язык разработки
- **Spring Boot 3**: Ядро микросервисов
- **Spring Cloud**:
  - **OpenFeign**: Для межсервисного взаимодействия
  - **Config Server**: Централизованные конфигурации
  - **Circuit Breaker**: Обработка ошибок
- **TelegramBots API**: Интеграция с Telegram
- **Kafka/RabbitMQ**: Асинхронная коммуникация
- **PostgreSQL**: Хранение данных пользователей
- **Redis**: Кэширование токенов
- **Lombok**: Упрощение boilerplate-кода

### Инфраструктура
- **Docker**: Контейнеризация сервисов
- **Kubernetes**: Оркестрация (опционально)
- **Prometheus/Grafana**: Мониторинг
- **EFK Stack**: Логирование (Elasticsearch + Fluentd + Kibana)

---

<a name="особенности"></a>
## 🔥 Ключевые особенности

### 1. Аутентификация через BFF-токен
- Многоуровневая валидация токенов
- Автоматическое обновление истекших токенов
- Шифрование токенов в хранилище (AES-256)

### 2. Умная система уведомлений
```java
@KafkaListener(topics = "notifications")
public void handleNotification(NotificationEvent event) {
    notificationService.process(event)
        .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
        .subscribe();
}
```

Приоритезация уведомлений
Повторная отправка при ошибках
Поддержка Markdown-форматирования

3. Проактивный UX в Telegram

Bot Interface

Интерактивные Reply-клавиатуры
Контекстные подсказки команд
Локализованные ответы (поддержка i18n)

4. Производительность

    Кэширование запросов к LMS (Caffeine)

    Пакетная обработка уведомлений

    Horizontal Pod Autoscaling в Kubernetes

---   

<a name="запуск"></a>
## 🚀 Запуск проекта
Требования

    Java 17+

    Docker 20.10+

    Kafka 3.3+ (опционально)

Шаги развертывания

    Клонировать репозиторий:
    bash
    Copy

    git clone https://github.com/your-repo/telegram-lms-bot.git

    Настроить окружение:
    bash
    Copy

    cp env-example .env

    Запустить сервисы:
    bash
    Copy

    docker-compose up -d --build

    Зарегистрировать бота через @BotFather и добавить токен в конфиг.

---

<a name="api"></a>
## 🔌 API Endpoints
Service	Endpoint	Method	Description
Auth Service	/auth/token	POST	Сохранение BFF-токена
Notification	/notifications	GET	Получение истории уведомлений
LMS Gateway	/lms/webhook	POST	Входная точка для данных LMS

Пример запроса:
```bash
curl -X POST "http://auth-service:8080/auth/token" \
  -H "Content-Type: application/json" \
  -d '{"chatId": 12345, "token": "bff_secure_token"}'
```

---

<a name="мониторинг"></a>
## 📊 Мониторинг и метрики

Grafana Dashboard

    Prometheus: Сбор метрик в реальном времени

    Grafana:

        Dashboard с ключевыми метриками:

            Количество активных пользователей

            Время обработки запросов

            Статус интеграции с LMS

    AlertManager: Уведомления о критических ошибках

---

<a name="примеры"></a>
## 💡 Примеры использования
Сценарий 1: Получение уведомления

    Пользователь авторизуется:
    Copy

    /auth bff_123456789

    При появлении нового задания в LMS:
    Copy

    🔔 Новое задание: "Разработка микросервисов"
    Дедлайн: 2023-12-31

Сценарий 2: Обработка ошибок
```java
@FeignClient(name = "auth-service", fallback = AuthFallback.class)
public interface AuthClient {
    @PostMapping("/validate")
    ResponseEntity<TokenValidationResponse> validateToken(@RequestBody TokenRequest request);
}
```
---

<a name="планы"></a>
## 🚧 Планы развития

Интеграция с CI/CD:

 Автоматические тесты

 Canary-деплойменты
 Расширение аналитики:
      Предиктивная аналитика нагрузки
       AI-классификация уведомлений

 Безопасность:
       Ролевая модель доступа (RBAC)
       Audit Logging

---

## 📄 Лицензия

MIT License © 2025 Alexander Stepanov
--->
