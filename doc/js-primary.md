Первичные значения
==================

К первичным значениям относятся все атомарные 
и литеральные значения. 
Подробна их работа рассмотрена в документах
[Литералы](js-literals.md) и [Атомарные конструкции](js-atom.md).

Так же к первичным значениям относятся
* вызов метода
* доступ к свойству объекта/значению в карте ( `java.util.Map` )
* доступ к элементу массива/списка  ( `java.util.List` )

Они будут рассмотрены далее.

Доступ к свойству объекта/значению в карте
------------------------------------------

Для примера существует объект `obj` с полем `str` 
и значением `propvalue`, доступ к полю осуществляется через точкуЖ

    obj.str

**Лексемы**

 1. IdTok "obj"
 2. KeywordTok "."
 3. IdTok "str"

**AST дерево**
* PropertyAST str
  * VarRefAST obj

**интерпретация**

    propvalue : class java.lang.String

Карта вложенная в карту 

    map.m.a

**Лексемы**

 1. IdTok "map"
 2. KeywordTok "."
 3. IdTok "m"
 4. KeywordTok "."
 5. IdTok "a"

**AST дерево**
* PropertyAST a
  * PropertyAST m
    * VarRefAST map

**интерпретация**

    a : class java.lang.String



## Доступ к элементу массива/списка
### Пример с объектом

    obj['str']

**Лексемы**

 1. IdTok "obj"
 2. KeywordTok "["
 3. StringTok "'str'"
 4. KeywordTok "]"

**AST дерево**
* IndexAST
  * VarRefAST obj
  * StringAST "str"

**интерпретация**

    propvalue : class java.lang.String

### Пример с картой

    map['key1']

**Лексемы**

 1. IdTok "map"
 2. KeywordTok "["
 3. StringTok "'key1'"
 4. KeywordTok "]"

**AST дерево**
* IndexAST
  * VarRefAST map
  * StringAST "key1"

**интерпретация**

    mapvalue : class java.lang.String



### Пример с картой

    map['m']['a']

**Лексемы**

 1. IdTok "map"
 2. KeywordTok "["
 3. StringTok "'m'"
 4. KeywordTok "]"
 5. KeywordTok "["
 6. StringTok "'a'"
 7. KeywordTok "]"

**AST дерево**
* IndexAST
  * IndexAST
    * VarRefAST map
    * StringAST "m"
  * StringAST "a"

**интерпретация**

    a : class java.lang.String



### Пример со списком

    list[0]

**Лексемы**

 1. IdTok "list"
 2. KeywordTok "["
 3. ForcedFloatNumberTok "0"
 4. KeywordTok "]"

**AST дерево**
* IndexAST
  * VarRefAST list
  * NumberAST 0.0

**интерпретация**

    1.0 : class java.lang.Double



### Пример с массивом

    arr[0]

**Лексемы**

 1. IdTok "arr"
 2. KeywordTok "["
 3. ForcedFloatNumberTok "0"
 4. KeywordTok "]"

**AST дерево**
* IndexAST
  * VarRefAST arr
  * NumberAST 0.0

**интерпретация**

    1.0 : class java.lang.Double



### Пример с объектом и массивом

    obj[arr[1]]

**Лексемы**

 1. IdTok "obj"
 2. KeywordTok "["
 3. IdTok "arr"
 4. KeywordTok "["
 5. ForcedFloatNumberTok "1"
 6. KeywordTok "]"
 7. KeywordTok "]"

**AST дерево**

* IndexAST
  * VarRefAST obj
  * IndexAST
    * VarRefAST arr
    * NumberAST 1.0

**интерпретация**

    propvalue : class java.lang.String



Вызов метода
-----------------

О принцепах вызова методов Java будет освещенно в отдельной главе/файле

### Пример

    obj.repeat( 'a', 3 )

**Лексемы**

 1. IdTok "obj"
 2. KeywordTok "."
 3. IdTok "repeat"
 4. KeywordTok "("
 5. StringTok "'a'"
 6. KeywordTok ","
 7. ForcedFloatNumberTok "3"
 8. KeywordTok ")"

**AST дерево**
* CallAST
  * PropertyAST repeat
    * VarRefAST obj
  * StringAST "a"
  * NumberAST 3.0

**интерпретация**

    aaa : class java.lang.String
