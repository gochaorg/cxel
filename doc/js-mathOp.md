Математические операторы
=========================

Математические операторы их 6 штук:
`+`, `-`, `*`, `/`, `%`, `**`.
Все они работают своебразно в js.

Для всех них свойственно:

* Тип результата вычисления может отличаться number
* Если один из операндов строка
    * Если строка пустая, тогда она интерпретируется как число 0
    * Если строка не пустая, 
      тогда тип результата может оказаться весьма неожиданным 
      и зависит от соседнего операнда
    * Если **левый операнд** не пустая строка, 
      то в большинстве случаев результат тоже будет строка

Далее приводяться результаты тестов.

Все бинарные операции 
в дереве ast представлены одной конструкцией:
`xyz.cofe.cxel.ast.BinaryOpAST`

Операции над числами
--------------------

| Код       | Реузльтат |
|-----------|-----------|
| `1 + 1`   | `2.0`     |
| `1 - 1`   | `0.0`     |
| `2 * 3`   | `6.0`     |
| `12 / 3`  | `4.0`     | 
| `10 % 3`  | `1.0`     |
| `3 ** 3`  | `27.0`    |

### Приоритеты операций

**Пример с приоритетом операций**

    1 + 2 * 4

**Лексемы**

 1. ForcedFloatNumberTok "1"
 2. KeywordTok "+"
 3. ForcedFloatNumberTok "2"
 4. KeywordTok "*"
 5. ForcedFloatNumberTok "4"

**AST дерево**

* BinaryOpAST +
  * NumberAST 1.0
  * BinaryOpAST *
    * NumberAST 2.0
    * NumberAST 4.0

**интерпретация**

    9.0 : class java.lang.Double

**если бы небыло приоритета**

    (1 + 2) * 4

**Лексемы**

 1. KeywordTok "("
 2. ForcedFloatNumberTok "1"
 3. KeywordTok "+"
 4. ForcedFloatNumberTok "2"
 5. KeywordTok ")"
 6. KeywordTok "*"
 7. ForcedFloatNumberTok "4"

**AST дерево**

* BinaryOpAST *
  * ParenthesAST
    * BinaryOpAST +
      * NumberAST 1.0
      * NumberAST 2.0
  * NumberAST 4.0

**интерпретация**

    12.0 : class java.lang.Double
    
### Пример с левой/правой рекурсией

    12 / 3 * 4

**Лексемы**

 1. ForcedFloatNumberTok "12"
 2. KeywordTok "/"
 3. ForcedFloatNumberTok "3"
 4. KeywordTok "*"
 5. ForcedFloatNumberTok "4"

**AST дерево**

* BinaryOpAST *
  * BinaryOpAST /
    * NumberAST 12.0
    * NumberAST 3.0
  * NumberAST 4.0

**интерпретация**

    16.0 : class java.lang.Double

**Правая рекурсия**

    12 / (3 * 4)

**Лексемы**

 1. ForcedFloatNumberTok "12"
 2. KeywordTok "/"
 3. KeywordTok "("
 4. ForcedFloatNumberTok "3"
 5. KeywordTok "*"
 6. ForcedFloatNumberTok "4"
 7. KeywordTok ")"

**AST дерево**

* BinaryOpAST /
  * NumberAST 12.0
  * ParenthesAST
    * BinaryOpAST *
      * NumberAST 3.0
      * NumberAST 4.0

**интерпретация**

    1.0 : class java.lang.Double

**Приоритет возведения в степень**


    2 * 3 ** 3

**Лексемы**

 1. ForcedFloatNumberTok "2"
 2. KeywordTok "*"
 3. ForcedFloatNumberTok "3"
 4. KeywordTok "**"
 5. ForcedFloatNumberTok "3"

**AST дерево**

* BinaryOpAST *
  * NumberAST 2.0
  * BinaryOpAST **
    * NumberAST 3.0
    * NumberAST 3.0

**интерпретация**

    54.0 : class java.lang.Double

*Еще пример*


    (2 * 3) ** 3

**Лексемы**

 1. KeywordTok "("
 2. ForcedFloatNumberTok "2"
 3. KeywordTok "*"
 4. ForcedFloatNumberTok "3"
 5. KeywordTok ")"
 6. KeywordTok "**"
 7. ForcedFloatNumberTok "3"

**AST дерево**

* BinaryOpAST **
  * ParenthesAST
    * BinaryOpAST *
      * NumberAST 2.0
      * NumberAST 3.0
  * NumberAST 3.0

**интерпретация**

    216.0 : class java.lang.Double



## Мат операции над не числовыми данными

### строки

    'abc' + 'def'

**интерпретация**

    abcdef : class java.lang.String

### булево

    true + false

    1.0 : class java.lang.Double

### булево + null

    true + null

**Лексемы**

 1. KeywordTok "true"
 2. KeywordTok "+"
 3. KeywordTok "null"

**AST дерево**

* BinaryOpAST +
  * BooleanAST true
  * NullAST null

**интерпретация**

    1.0 : class java.lang.Double

### булево + NaN

    true + NaN

**интерпретация**

    NaN : class java.lang.Double

### строка и число

    10 + '2'

**интерпретация**

    102 : class java.lang.String

### строка и число, второй пример

    '2'+4

**интерпретация**

    24 : class java.lang.String

### null 

    null + null

**интерпретация**

    0.0 : class java.lang.Double
