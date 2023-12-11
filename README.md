# Тестовое задание

### Тестовое задание

Необходимо разработать веб-сервис для работы с учетными записями клиентов со следующей функциональностью:

* Создание учетной записи
* Чтение учетной записи по id
* Поиск учетной записи по полям: фамилия, имя, отчество, телефон, e-mail. Поиск осуществляется только при условии
  указания хотя 1 поля

Поля учетной записи клиента:

* id
* bank_id (идентификатор клиента в банке)
* фамилия
* имя
* отчество
* дата рождения
* номер паспорта (вместе с серией в формате ХХХХ ХХХХХХ)
* место рождения
* телефон (в формате 7ХХХХХХХХХХ)
* емейл
* адрес регистрации
* адрес проживания

Клиент может быть создан из разных приложений. В зависимости от приложения отличается логика валидации полей при
создании учетной записи. Приложение определяется по обязательному для указания http-заголовку "x-Source".

Список значений http-заголовка и правила валидации полей:

* mail - только имя и емейл обязательные
* mobile - только номер телефона обязательный
* bank - bank_id, фамилия, имя, отчество, дата рождения, номер паспорта обязательные
* gosuslugi - все поля кроме емейла и адреса проживания обязательные

Дополнительные требования:

* Код должен быть приспособлен для появления новых приложений со своими правилами валидации.
* Основная бизнес-логика должна быть покрыта тестами.
* Стек: java 11+, spring boot, spring data jpa, postgres, maven
* Должен быть docker-compose для подготовки окружения для локального запуска сервиса
* Выполненное задание выложить на GitHub или GitLab