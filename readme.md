Парсер + интерпретатор js выражений
===================================

Использование
-------------

### maven

    <dependecy>
        <groupId>xyz.cofe</groupId>
        <artifactId>cxel</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependecy>

### Простые примеры java

Сумма двух переменных

    import xyz.cofe.cxel.eval.Eval;

    Eval.parse("a+b")
        .context( ctx->ctx
            .bind("a",1)
            .bind("b",2)
        ).eval()

Синтаксис
=========