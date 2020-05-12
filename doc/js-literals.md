Литералы
-----------

Всего в javascript не много литералов

* Числа
* Булево
* Строка
* `undefined` - не является ключевым словом, 
  а является ссылкой на переменную типа `xyz.cofe.cxel.js.Undef`
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

Соответствие: 
* лексемма - `xyz.cofe.cxel.tok.StringTok`,
* AST - `xyz.cofe.cxel.ast.StringAST`,
* java - `java.lang.String`

Примеры

    'single'          
    "double"
    
      js> "encode\"en\\code\'abc"
    eval> encode"en\code'abc
    
      js> 'a\nb\rc\bd\fe\tf\vg'
    eval> a
          b
          de	fg
          
      js> '\152\x73'
    eval> js
        
      js> '\u0041'
    eval> A
        
      js> '\u{00041}'
    eval> A
        
      js> 'ab\
          ef'
    eval> abef

## Прочее
### null

    null

Соответствие: 
* лексемма - `xyz.cofe.cxel.tok.KeywordTok`,
* AST - `xyz.cofe.cxel.ast.NullAST`,
* java - `null`

### undefined

    undefined

Соответствие: 
* лексемма - `xyz.cofe.cxel.tok.IdTok`,
* AST - `xyz.cofe.cxel.ast.VarRefAST`,
* java - `xyz.cofe.cxel.js.Undef`
