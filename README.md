# Currency Exchange API

Currency Exchange API - это веб-приложение, предоставляющее методы REST для взаимодействия с базой данных SQLite.
Приложение легко развертывается локально с использованием сервера Tomcat и обрабатывает HTTP-запросы,
предоставляя ответы в формате JSON. Возможности приложения включают работу с валютами, курсами валют, а также
предоставляют функциональность расчета перевода средств между валютами через различные сценарии.

## Запуск локально

Для запуска приложения локально выполните следующие шаги:

1. **Скачайте проект:**
   - Склонируйте репозиторий с помощью Git:
     - git clone https://github.com/YuriyNekludov/CurrencyExchangeAPI.git
   - Или скачайте архив проекта.
2. **Откройте проект:**
    - Откройте проект в вашей IDE.
3. **Запустите приложение:**
    - Запустите приложение на сервере Tomcat, указав в настройках
   конфигурации Tomcat / Local
4. **Использование функционала приложения:**
    - Используйте HTTP-запросы для тестирования методов API,
   обращаясь к локальному серверу.

## Методы API

Приложение предоставляет следующие методы API:

1. **Работа с валютами:**
    - Получение списка всех доступных валют.
    - Получение информации о конкретной валюте по её коду.
    - Добавление новой валюты.

2. **Работа с курсами валют:**
   - Получение текущего курса валюты.
   - Получение списка всех доступных курсов обмена валют.
   - Добавление нового курса валют.
   - Обновление курса валюты.

3. **Расчет перевода средств:**
   - Расчет по прямому курсу.
   - Расчет по обратному курсу.
   - Расчет по кросс-курсу на базе доллара.

## Примеры HTTP-запросов

1. **Получение списка валют:**
    - GET /currencies
2. **Получение информации о валюте по коду:**
    - GET /currency/USD
3. **Добавление новой валюты:**
    - POST /currencies?name=US+Dollar&code=USD&sign=$
4. **Получение текущего курса валюты:**
    - GET /exchangeRate/USDRUB
5. **Добавление нового курса валют:**
   - POST /exchangeRates?baseCurrencyCode=1&targetCurrencyCode=2&rate=0.98
6. **Получение списка всех доступных курсов обмена валют:**
    - GET /exchangeRates
7. **Обновление курса валюты**
    - PATCH /exchangeRate/USDRUB?rate=98.03
8. **Расчет перевода средств:**
    - GET /exchange?from=USD&to=EUR&amount=10

## Обработка ошибок

В случае возникновения ошибок, пользователь получит соответствующий ответ в формате JSON 
с соответствующим кодом ответа HTTP. Обработаны различные сценарии ошибок для 
обеспечения более надежного использования приложения.

## Важно

В данной версии проекта база данных находится внутри
проекта и настройка соединения уже выполнена. Приятного использования!




