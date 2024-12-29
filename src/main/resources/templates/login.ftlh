<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Система управления личными финансами с ИИ</title>
    <link rel="stylesheet" href="/styles.css">
    <style>
        body {
            font-family: 'Roboto', Arial, sans-serif;
            background-color: #f9fbfc;
            color: #333;
            margin: 0;
            padding: 0;
        }

        h1, h4 {
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

        a {
            display: block;
            text-align: center;
            margin-top: 15px;
            font-weight: bold;
            color: #4e54c8;
            text-decoration: none;
        }

        a:hover {
            color: #3b3b9e;
        }
    </style>
</head>
<body>
<h1>Система управления личными финансами с ИИ</h1>
<h4>Авторизация</h4>
<form action="/login" method="post">
    <label for="email">Эл. Почта:</label>
    <input type="email" id="email" name="username" required><br><br>

    <label for="password">Пароль:</label>
    <input type="password" id="password" name="password" required><br><br>

    <!-- Добавлен CSRF токен -->
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <input type="submit" value="Вход">
</form>
<a href="/registration">Зарегистрироваться</a>
</body>
</html>
