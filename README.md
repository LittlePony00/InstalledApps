Этот проект является тестовым для компании "Доктор Веб".

Архитектура: 
MVI (Model-View-Intent)
State — состояние экрана
Intent — намерения пользователя
Action — одноразовые события (навигация, запуск приложения)

Структура пакетов:
core/ — общий код (domain, data, di, navigation, ui)
feature/ — фичи (apps_list, app_detail)

Стек:
UI: Jetpack Compose
DI: Koin
Навигация: Navigation Compose
Архитектура: ViewModel + MVI
Контрольная сумма: MD5 (128-битный хеш)
Min SDK: 24 (Android 7.0)
