Клас для виконання Test. Вивід інформації виконання у консолі.

У папці "база даних" зберігається: дамп MySQL та MongoDB, схема MySQL БД.
Назва БД: wholesale_base

[ПЗ 1-2]
Клас DAOTest приймає у конструктор екземпляр OrderDAO(MySQLOrderDAO, MongoOrderDAO)
Метод test виводить 10 записів з обраної бази даних.
Паттерн singleton використовується у коннекторах MySQLConnector та MongoDBConnector

[ПЗ 3-4]
Observer використовується коли виконуються методи класу MySQLGoodDAO
Вивід інформації про спрацьовану подію виводиться у консоль та у log файл 

[ПЗ 5-6]
Memento використаний у класі GoodHistory

[ПЗ 7-8]
Використовується імітація поточного користувача в системі через singleton
Proxy реалізовано у класі ProxyMySQLGoodDAO