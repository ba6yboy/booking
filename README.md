# 📅 Booking System API

![Java](https://img.shields.io/badge/Java_17+-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=black)

RESTful API для автоматизованої системи бронювання послуг. Проєкт розроблено як надійне бекенд-ядро для малого бізнесу (салони краси, СТО, приватні консультації тощо). Система дозволяє зручно керувати розкладом, послугами та клієнтами, автоматично запобігаючи накладкам у часі.

---

## ✨ Основний функціонал (MVP)

* **Smart Time Management:** Алгоритм, який вираховує час завершення послуги та блокує спроби подвійного бронювання (Time Overlap Prevention).
* **Безпека даних (DTO Pattern):** Чітке розділення між моделями бази даних (Entity) та об'єктами передачі даних (DTO), що захищає від витоку системної інформації (Tainted Data Prevention).
* **Явна валідація:** Перевірка вхідних HTTP-запитів на рівні контролерів (наприклад, неможливість створити запис у минулому) за допомогою Jakarta Validation.
* **Глобальна обробка помилок:** Стандартизовані JSON-відповіді для клієнтських помилок замість виведення системних Stack Trace.
* **Самодокументованість:** Інтегрований Swagger UI для перегляду та тестування API прямо з браузера.

---

## 🛠 Технологічний стек

* **Мова програмування:** Java 17
* **Фреймворк:** Spring Boot 3
* **Модулі Spring:** Spring Web, Spring Data JPA, Spring Boot Validation
* **База даних:** PostgreSQL
* **Інструменти збірки:** Maven
* **Документація:** Springdoc OpenAPI (Swagger)

---

## 🗄 Архітектура Бази Даних

Система базується на трьох основних сутностях зі зв'язками `Many-to-One`:

| Таблиця            | Опис                     | Ключові поля                                          |
|:-------------------|:-------------------------|:------------------------------------------------------|
| **`users`**        | Дані клієнтів            | `id`, `name`, `phone`, `role`                         |
| **`services`**     | Доступні бізнес-послуги  | `id`, `title`, `duration_minutes`, `price`            |
| **`appointments`** | Фактичні записи клієнтів | `id`, `user_id`, `service_id`, `start_time`, `status` |

---

## 🚀 Локальний запуск (Getting Started)

Щоб запустити проєкт на своєму комп'ютері, виконайте наступні кроки:

### 1. Вимоги
Переконайтеся, що у вас встановлено:
* **JDK 17** або вище.
* **PostgreSQL** сервер (запущений локально на порту `5432`).
* **Maven** (або використовуйте вбудований `mvnw`).

### 2. Налаштування Бази Даних
Створіть порожню базу даних у вашому PostgreSQL:
```sql
CREATE DATABASE booking_db;
```

Відкрийте файл `src/main/resources/application.properties` і вкажіть ваші дані доступу:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/booking_db
spring.datasource.username=postgres
spring.datasource.password=твій_пароль_від_бази
spring.jpa.hibernate.ddl-auto=update
```

### 3. Запуск

Клонуйте репозиторій та запустіть проєкт через термінал:

```bash
git clone [https://github.com/ba6yboy/booking-system-api.git](https://github.com/ВАШ_НІКНЕЙМ/booking-system-api.git)
cd booking-system-api
./mvnw spring-boot:run
```

*(При першому запуску CommandLineRunner автоматично створить таблиці та наповнить їх базовими тестовими даними).*

---

## 📡 API Endpoints

Базовий URL: `http://localhost:8080/api`

| HTTP Метод | Endpoint                              | Опис                               |
|------------|---------------------------------------|------------------------------------|
| `POST`     | `/appointments`                       | Створити нове бронювання           |
| `GET`      | `/appointments`                       | Отримати історію всіх записів      |
| `GET`      | `/appointments/daily?date=YYYY-MM-DD` | Отримати розклад на конкретну дату |

---

## 📚 Документація (Swagger UI)

Повна інтерактивна документація API доступна після запуску сервера за адресою:
👉 **[http://localhost:8080/swagger-ui/index.html](https://www.google.com/search?q=http://localhost:8080/swagger-ui/index.html)**

Використовуйте цей інтерфейс для надсилання тестових запитів без використання Postman.

```

```