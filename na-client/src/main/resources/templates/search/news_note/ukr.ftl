Результати пошуку за ключем <b>${key}</b>
<#if notes?has_content>
    Знайдено ${total} новин. Сторінка ${page}/${total_pages}

    <#list notes as note>
        <a href="${note.url}">${note.title}</a>
        ${note.source} / ${note.date?date} / ${note.region} / ${note.common}

    </#list>
<#else>
    <b>Нема нічого ;(</b>
</#if>

