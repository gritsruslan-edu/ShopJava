JavaFX додаток для роботи з інформаційною системою товарів магазину.
За допомогою цього додатку робітники магазину зможуть додавати інформацію про товари у БД, змінювати її, видаляти та здійснювати пошук за конкретними полями.


Склад команди:
- Team Lead - Гриценко Р. Р. ІН-31/2
- Developer 1 - Кочура Д. С. ІН-32/1
- Developer 2 - Сєрік М. В. ІН-32/2

Правила створення комітів та злиття гілок:
- Push у гілку `master` дозволений тільки для Team Lead
- Кожний Developer працює у своїй гілці
- Для злиття гілок повинен використовуватися `Pull Request`
- Force Push для гілки `master` заборонений


Програма буде працювати з таблицею products (варіант 19 курсової роботи).
СКБД: PostgreSQL

Схема таблиці:
```postgresql
CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(150) NOT NULL,
    price NUMERIC(10, 2) NOT NULL CHECK (price >= 0),
    type VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0 CHECK (quantity >= 0)
);
```


