Атомарные конструкции
=======================

Унарные операции
----------------

### Унарный минус

    -1

**Лексемы**

 1. KeywordTok "-"
 2. ForcedFloatNumberTok "1"

**AST дерево**

* UnaryOpAST -
  * NumberAST 1.0

**интерпретация**

    -1.0 : class java.lang.Double

### Унарный плюс

    +(1+2)

**Лексемы**

 1. KeywordTok "+"
 2. KeywordTok "("
 3. ForcedFloatNumberTok "1"
 4. KeywordTok "+"
 5. ForcedFloatNumberTok "2"
 6. KeywordTok ")"

**AST дерево**

* UnaryOpAST +
  * ParenthesAST
    * BinaryOpAST +
      * NumberAST 1.0
      * NumberAST 2.0

**интерпретация**

    3.0 : class java.lang.Double



### Операция НЕ

    ! true

**Лексемы**

 1. KeywordTok "!"
 2. KeywordTok "true"

**AST дерево**

* UnaryOpAST !
  * BooleanAST true

**интерпретация**

    false : class java.lang.Boolean



## Ссылки на переменные (varRef)

    a+b

**Лексемы**

 1. IdTok "a"
 2. KeywordTok "+"
 3. IdTok "b"

**AST дерево**

* BinaryOpAST +
  * VarRefAST a
  * VarRefAST b

**интерпретация**

    22.0 : class java.lang.Double

## Списки

    [ a, b, a+b ]

**Лексемы**

 1. KeywordTok "["
 2. IdTok "a"
 3. KeywordTok ","
 4. IdTok "b"
 5. KeywordTok ","
 6. IdTok "a"
 7. KeywordTok "+"
 8. IdTok "b"
 9. KeywordTok "]"

**AST дерево**

* ListAST
  * VarRefAST a
  * VarRefAST b
  * BinaryOpAST +
    * VarRefAST a
    * VarRefAST b

**интерпретация**

    [10, 12, 22.0] : class java.util.ArrayList

## Карты

    { a: a, 
      b: 12, 
      'c': 23, 
      true: 34, 
      (undefined): 45+5, 
      undefined:333, 
      (1+2): 56, 
      l:[ 0, 1 ], 
      m: {x:7} 
    }

Соответствие: 

* лексемма - специальной лексемы нет,
* AST - `xyz.cofe.cxel.ast.MapAST`,
        `xyz.cofe.cxel.ast.MapPropEntreyAST`,
* java - объект типа `java.util.Map`

**Лексемы**

 1. KeywordTok "{"
 2. IdTok "a"
 3. KeywordTok ":"
 4. IdTok "a"
 5. KeywordTok ","
 6. IdTok "b"
 7. KeywordTok ":"
 8. ForcedFloatNumberTok "12"
 9. KeywordTok ","
 10. StringTok "'c'"
 11. KeywordTok ":"
 12. ForcedFloatNumberTok "23"
 13. KeywordTok ","
 14. KeywordTok "true"
 15. KeywordTok ":"
 16. ForcedFloatNumberTok "34"
 17. KeywordTok ","
 18. KeywordTok "("
 19. IdTok "undefined"
 20. KeywordTok ")"
 21. KeywordTok ":"
 22. ForcedFloatNumberTok "45"
 23. KeywordTok "+"
 24. ForcedFloatNumberTok "5"
 25. KeywordTok ","
 26. IdTok "undefined"
 27. KeywordTok ":"
 28. ForcedFloatNumberTok "333"
 29. KeywordTok ","
 30. KeywordTok "("
 31. ForcedFloatNumberTok "1"
 32. KeywordTok "+"
 33. ForcedFloatNumberTok "2"
 34. KeywordTok ")"
 35. KeywordTok ":"
 36. ForcedFloatNumberTok "56"
 37. KeywordTok ","
 38. IdTok "l"
 39. KeywordTok ":"
 40. KeywordTok "["
 41. ForcedFloatNumberTok "0"
 42. KeywordTok ","
 43. ForcedFloatNumberTok "1"
 44. KeywordTok "]"
 45. KeywordTok ","
 46. IdTok "m"
 47. KeywordTok ":"
 48. KeywordTok "{"
 49. IdTok "x"
 50. KeywordTok ":"
 51. ForcedFloatNumberTok "7"
 52. KeywordTok "}"
 53. KeywordTok "}"

**AST дерево**

* MapAST
  * MapPropEntreyAST
    * VarRefAST a
    * VarRefAST a
  * MapPropEntreyAST
    * VarRefAST b
    * NumberAST 12.0
  * MapLiteralEntreyAST
    * StringAST "c"
    * NumberAST 23.0
  * MapLiteralEntreyAST
    * BooleanAST true
    * NumberAST 34.0
  * MapExprEntreyAST
    * VarRefAST undefined
    * BinaryOpAST +
      * NumberAST 45.0
      * NumberAST 5.0
  * MapPropEntreyAST
    * VarRefAST undefined
    * NumberAST 333.0
  * MapExprEntreyAST
    * BinaryOpAST +
      * NumberAST 1.0
      * NumberAST 2.0
    * NumberAST 56.0
  * MapPropEntreyAST
    * VarRefAST l
    * ListAST
      * NumberAST 0.0
      * NumberAST 1.0
  * MapPropEntreyAST
    * VarRefAST m
    * MapAST
      * MapPropEntreyAST
        * VarRefAST x
        * NumberAST 7.0

**интерпретация**

    { a=10, 
      b=12.0, 
      c=23.0, 
      true=34.0, 
      undefined=50.0, 
      undefined=333.0, 
      3.0=56.0, 
      l=[0.0, 1.0], 
      m={x=7.0}
    } : class java.util.LinkedHashMap
