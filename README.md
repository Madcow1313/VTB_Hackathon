Project for vtb MORE tech 5.0

##Запуск
* для запуска нужно создать BankBranch/src/main/resources/secret.properties
* secret.graphhopper.api.key=
* зарегистрировать токен на https://graphhopper.com/
* запустить проложение в IDEA, в другом не тестили
* если запускать как java -jar, то скорее всего грохнется
  BankBranch/src/main/java/com/example/BankBranch/storage/Storage.java
  String filePath = "BankBranch/src/main/resources/offices.json";
  скорее всего грохнется из за этого
* если все таки запустилось, то зайти на http://localhost:8080/
* наслаждаться)

###*НЕ ТРОИТ МАРШРУТ ЕСЛИ НЕ ВЫБРАНА УСЛУГА

##Стек

###бек
* Java
* Spring Boot
* Spring Web
* lombok

###Сторонние библиотеки
* graphhopper

##Развитие по беку
* Использовать рест и микро сервисную архитектуру
* БД постгрес
* докрутить фичи с электронной очередью
* выдача и сбор статистики
* если пользователь идет в банкомат вести его через магазины
* если ипотека, то через офисы застройщиков (шутка)
* что-то с ИИ надо придумать обязательно, например каждые 3 дня, что бы напоминал купить молоко
* ОООО, фича фича, человек берет талон он лайн, записывается, приходит, все счастливы, а если не приходит, снижать рейтинг и людям с низким рейтингом не давать талоны онлайн. Тут надо аналитика уже, на этом мои полномочия все

###Расширение по фронту
* Выбор города, чтобы можно было до приезда посмотреть
* Городской транспорт
* Экспорт карты с маршрутом в телеграм или в файл или ещё куда
* Кастомные карты
* Экспорт текстового маршрута в телеграм или в файл или ещё куда
* Отслеживание пользователя в реальном времени
