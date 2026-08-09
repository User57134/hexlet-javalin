@import org.example.hexlet.NamedRoutes

<html>
<head>
    <meta charset="utf-8">
    <title>Добавить курс</title>
</head>
<body>
<form action="${NamedRoutes.coursesPath()}" method="post">
    <div>
        <label>
            Название
            <input type="text" name="name"/>
        </label>
    </div>
    <div>
        <label>
            Описание
            <input type="text" required name="description"/>
        </label>
    </div>
    <input type="submit" value="Добавить"/>
</form>
</body>
</html>