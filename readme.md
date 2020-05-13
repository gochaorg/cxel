Парсер + интерпретатор js выражений
===================================

Данный проект позволяет интерпретировать 
части конструкций java script.

**Отличительные черты**

* Интерпретация унарных, бинарных и других выражений на языке javascript
* Возможность работать с объектами java
    * Получать доступ к методам/свойствами объектов
    * Расширять объекты java путем добавления виртуалных свойств    
* Контролировать доступ к методам/свойствам объектов java

Использование
-------------

### maven

Добавьте зависимость в pom.xml

    <dependecy>
        <groupId>xyz.cofe</groupId>
        <artifactId>cxel</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependecy>

### Синтаксис языка

Синтаксис языка описывается [в документе Синтаксис](doc/js-syntax.md)

### Пример

```java
package xyz.cofe.cxel.js.demo;

import xyz.cofe.cxel.js.*;

/** Демонстрация */
public class Demo1 {
    /** Данный класс будет передав в js */
    public static class Interop1 {
        /** Добавляем свойство str */
        private String str;
        public String getStr(){ return str; }
        public void setStr( String value ){
            this.str = value;
        }
    
        /** Вывод текста в терминал
        @param str отображаемый текст
        */
        public void print( String str ){
            System.out.println("call print: "+str);
        }
    
        /** Повтор текста
        @param str текст
        @param count сколько раз надо повторить
        @return текст повторенный count раз
        */
        public String repeat( String str, int count ){
            if( str==null )return null;
            if( count<=0 )return "";
            if( count==1 )return str;
            if( str.length()==0 )return str;
            StringBuilder sb = new StringBuilder();
            for( int i=0; i<count; i++ ){
                sb.append(str);
            }
            return sb.toString();
        }
    }

    /**
    Входная точка в приложение 
    */
    public static void main(String[] args){
        // Данный объект будет участвовать в js
        Interop1 interop = new Interop1();
    
        // Указываем свойство str
        interop.setStr("xtr");

        // Добавляем интерпретатор
        JsEvaluator evaluator = new JsEvaluator();

        // В контекст исполнения добавляяем три переменных: i, a, b
        evaluator.context()
            .bind("i",interop)
            .bind("a","str")
            .bind("b",10)
            ;

        // Интерпретируем выражение
        Object res = evaluator.eval("i.print( a + b + i.str )");
        System.out.println("result "+res);
        // будет выведено:
        //  call print: str10xtr
        //  result null

        // Можно в более краткой форме
        System.out.println(
            new JsEvaluator().configure(
                ev->ev.context().bind("o",interop)
            ).eval("o.repeat('aB',2)")
        );
        // будет выведено:
        // aBaB
    }
}
```