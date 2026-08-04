@import gg.jte.Content

@param Content content
@param Content title = null
@param Content footer = null

<!doctype html>
<html>
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1" />

        <title>
        @if (title == null)
            Layout Test
        @else
            ${title}
        @endif
        </title>

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
            @if (footer != null)
               ${footer}
            @endif
        </footer>
    </body>
</html>





