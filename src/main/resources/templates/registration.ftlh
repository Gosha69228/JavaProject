<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Регистрация — Система управления личными финансами с ИИ</title>
    <link rel="stylesheet" href="/styles.css">
    <style>
        body {
            font-family: 'Roboto', Arial, sans-serif;
            background-color: #f9fbfc;
            color: #333;
            margin: 0;
            padding: 0;
        }

        h1, h2 {
            text-align: center;
            color: #4e54c8;
        }

        form {
            max-width: 400px;
            margin: 0 auto;
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        label {
            font-weight: bold;
            margin-bottom: 5px;
            display: inline-block;
        }

        input[type="text"],
        input[type="email"],
        input[type="password"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        input[type="submit"] {
            background: linear-gradient(90deg, #4e54c8, #8f94fb);
            color: white;
            border: none;
            padding: 12px 20px;
            border-radius: 25px;
            font-size: 1.2em;
            font-weight: bold;
            cursor: pointer;
            transition: background 0.3s ease;
        }

        input[type="submit"]:hover {
            background: #3b3b9e;
        }

        .error-message {
            color: red;
            font-weight: bold;
            text-align: center;
        }

        footer {
            text-align: center;
            margin-top: 20px;
            font-size: 0.9em;
            color: #777;
        }
    </style>
</head>
<body>
<header>
    <h1>Система управления личными финансами с ИИ</h1>
</header>

<main>
    <section>
        <h2>Регистрация</h2>
        <!-- Форма регистрации -->
        <form action="/registration" method="post">
            <!-- Поле для имени пользователя -->
            <label for="name">Имя пользователя:</label>
            <input type="text" id="name" name="name" required><br><br>

            <!-- Поле для электронной почты -->
            <label for="email">Эл. Почта:</label>
            <input type="email" id="email" name="email" required><br><br>

            <!-- Поле для пароля -->
            <label for="password">Пароль:</label>
            <input type="password" id="password" name="password" required><br><br>

            <!-- CSRF-токен -->
            <input type="hidden" name="_csrf" value="${_csrf.token}">

            <!-- Кнопка отправки -->
            <input type="submit" value="Зарегистрироваться">
        </form>
    </section>

    <!-- Блок отображения ошибок -->
    <section>
        <#if errorMessage??>
            <h2 class="error-message">${errorMessage}</h2>
        </#if>
    </section>
</main>

<footer>
    <p>&copy; 2024 Система управления личными финансами с ИИ</p>
</footer>
</body>
</html>
