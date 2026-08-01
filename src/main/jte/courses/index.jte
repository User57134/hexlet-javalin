@import org.example.hexlet.dto.courses.CoursesPage
@param CoursesPage page

<html>
    <head>
        <meta charset="utf-8" />
        <title>Хекслет 2026</title>
    </head>
    <body>
        <h1>${page.getHeader()}</h1>
        @if(page.getCourses().isEmpty())
            <p>Пока не добавлено ни одного курса</p>
        @else
            @for(var course : page.getCourses())
               <div>
                   <h2><a href="/courses/${course.getId()}">${course.getName()}</a></h2>
                   <p>${course.getDescription()}</p>
               </div>
            @endfor
        @endif
    </body>
</html>