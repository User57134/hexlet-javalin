@import gg.jte.Content

@param Content content


<!doctype html>
<html>
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Layout Test</title>
    </head>
    <body>
        <header>
            <nav>
                <ul>
                    <li>
                        <a href="/"> Главная </a>
                    </li>
                    <li>
                        <a href="/courses"> Курсы </a>
                    </li>
                </ul>
            </nav>
        </header>

        ${content}

        <footer>
            <ul>
                <li>
                    <a href="https://github.com/User57134">Профиль</a>
                </li>
            </ul>
        </footer>
    </body>
</html>