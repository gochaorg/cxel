Литералы
-----------

## Число
### input source

    1

### tokens

 1. ForcedFloatNumberTok "1"

### ast tree
* NumberAST 1.0

### eval result

    1.0 : class java.lang.Double



### input source

    1.0

### tokens

 1. FloatNumberTok "1.0"

### ast tree
* NumberAST 1.0

### eval result

    1.0 : class java.lang.Double



### input source

    NaN

### tokens

 1. IdTok "NaN"

### ast tree
* VarRefAST NaN

### eval result

    NaN : class java.lang.Double



## Булево
### input source

    true

### tokens

 1. KeywordTok "true"

### ast tree
* BooleanAST true

### eval result

    true : class java.lang.Boolean



### input source

    false

### tokens

 1. KeywordTok "false"

### ast tree
* BooleanAST false

### eval result

    false : class java.lang.Boolean



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
