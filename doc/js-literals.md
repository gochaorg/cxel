Литералы
-----------

Всего в javascript не много литералов

* Числа
* Булево
* Строка
* `undefined`
* `null`

Все числа, без разницы как они были введены 
будут интерпретироваться как 8 байтовые дробные числа (java.lang.Double)

## Числа

* `1`
* `1.0`
* `NaN`    - соответствует Double.NaN
* `0xff`   - шестнадцетиричное число 
* `0b1010` - двоичное
* `0377`   - восмеричное
* `10L`    - целое 8 байтовое
* `10l`    - целое 8 байтовое, регистр суффикса не важен
* `10i`    - целое 4 байтовое
* `10s`    - целое 2 байтовое
* `10b`    - целое 1 байтовое
* `10n`    - целое BigInteger
* `10w`    - целое BigInteger
* `10f`    - дробное 4 байта
* `10d`    - дробное BigDecimal
* `10wf`    - дробное BigDecimal
* `10wd`    - дробное 8 байта
* `2.3`    - дробное 8 байтовое
* `.3`     - дробное 8 байтовое
* `2.`     - дробное 8 байтовое
* `2.3w`    - дробное BigDecimal
* `.3n`     - дробное BigDecimal
* `.3w`     - дробное BigDecimal
* `2.w`     - дробное BigDecimal
* `2.n`     - дробное BigDecimal

### лексема
 Любому числу будет соответствовать следующие лексемы

 * `xyz.cofe.cxel.tok.NumberTok`
   * `xyz.cofe.cxel.tok.IntegerNumberTok`
   * `xyz.cofe.cxel.tok.FloatNumberTok`
     * `xyz.cofe.cxel.js.ForcedFloatNumberTok`
     
В результате работы лексера `xyz.cofe.cxel.js.JsLexer`
все указанные выше лексемы будут возвращать Double, 
только если явно не указан суффикс.
 

### ast tree
Узел в AST дереве будет представлен классом
`xyz.cofe.cxel.ast.NumberAST`

### NaN

`NaN` - является ссылкой на значение.
 
И будет представлен
лексеммой: `IdTok "NaN"`. 

В AST `VarRefAST NaN`


## Булево

    true
    false

являются ключевыми словами.

Соответствие: 
* лексемма - `xyz.cofe.cxel.tok.KeywordTok`,
* AST - `xyz.cofe.cxel.ast.BooleanAST`,
* java - `java.lang.Boolean`


## Строка
### input source

    'single'

### tokens

 1. StringTok "'single'"

### ast tree
* StringAST "single"

### eval result

    single : class java.lang.String



### input source

    'double'

### tokens

 1. StringTok "'double'"

### ast tree
* StringAST "double"

### eval result

    double : class java.lang.String



## Прочее
### input source

    null

### tokens

 1. KeywordTok "null"

### ast tree
* NullAST null

### eval result

    null



### input source

    undefined

### tokens

 1. IdTok "undefined"

### ast tree
* VarRefAST undefined

### eval result

    undefined : class xyz.cofe.cxel.js.Undef
