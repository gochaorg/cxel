Описание js-expression ситаксиса
================================

Синтаксис будет описан в согласно [нотации eBNF](ebnf.md)

Начальным правилом является правило *expression*

    expression ::= ifOp
    
    # Операторы
    ifOp   ::= or '?' or ':' or | or 
    or     ::= and    { '||' and }
    and    ::= bitOr  { '&&' bitOr  }
    bitOr  ::= bitXor { '|' bitXor  }
    bitXor ::= bitAnd { '^' bitAnd  }
    bitAnd ::= equals { '&' equals  }
    equals ::= compare [ ( '==' | '!=' | '===' | '!==' ) compare  ]
    compare ::= bitShift [( 
                '<' | '<=' | '>' | '>=' | 'in' | 'instanceof'  
                ) bitShift ]
    bitShift ::= addSub { ('<<' | '>>' | '>>>') addSub }
    addSub ::= mulDiv { ('+' | '-') mulDiv }
    mulDiv ::= power { ('*' | '/' | '%' ) power }
    power ::= primary { '**' primary }
    
    # Первичные конструкции
    primary ::= atom [ postfix ]
    postfix ::= { '.' idTok
                | '(' [ expression { ',' expression } ] ')'
                | '[' expression ']'
                }
                
    # Атомарные конструкции
    atom ::= parenthes
           | unaryExression
           | varRef
           | listExpression
           | mapExpression
           | literal
    parenthes ::= '(' expression ')'
    unaryExression ::= ( '-' 
                     |   '+' 
                     |   '!' 
                     |   '~' 
                     |   'delete' 
                     |   'void' 
                     |   'typeof' 
                     ) expression
    varRef ::= idTok
    listExpression ::= '[' [ expression { ',' expression } ] ']'
    mapExpression ::= '{' ( idTok 
                          | literal
                          | '(' expression ')' 
                          ) ':' expression
                      '}'
                      
    # Литералы
    literal ::= number
              | bool
              | nullz
              | string


literal
---------
Литеральные конструкции - являются непосредственными значениями, 
более подробно рассмотренны [в документе про Литералы](js-literals.md) 


atom
----------
Атомарные конструкции - являются конструкциями более сложными, 
для констрирования данных, не только литеральных значений, 
но и ссылок на переменные, списки, карты.

Более подробно рассмотрены [в документе про Атомарные значения](js-atom.md)

primary
-----------
Первичные конструкции служат основой для построения бинарных операторов

К первичным конструкциям относятня
* атомарные конструкции
* вызов метода
* доступ к свойству объекта/значению в карте ( `java.util.Map` )
* доступ к элементу массива/списка  ( `java.util.List` )

Подробная описывается [в документе Первичные конструкции](js-primary.md).

expression
-----------------
Операторы собраны в одну большу группу и будут описаны в отдельных главах

Ключевым моментом при рассмотрении синтаксиса является
* Приоритет операторов
* Наличие или отсуствие конкатенации операторов с одинаковым приоритетом

### Приоритет операторов

Правило expression [описано в файле JsParser](https://github.com/gochaorg/cxel/blob/cdc4b3a1d1e0b72e44ab951a422da18f59293a3a/src/main/java/xyz/cofe/cxel/js/JsParser.java#L116).

Приоритет операторов задается синтаксисом (см выше), 
чем ниже правило тем выше его приоритет.

### Наличие или отсуствие конкатенации операторов

Правило которое содержит возможность конкатенации содержит явное указание
в теле наличие цикла

пример:

    addSub ::= mulDiv { ('+' | '-') mulDiv }

т.е. согласно этому правилу допустима конструкция `1 + 2 - 3`

Правило которое **не содержит** описывается по другому, пример:

    equals ::= compare [ ( '==' | '!=' | '===' | '!==' ) compare  ]

т.е. согласно ему конструкция вида `a == b == c` является **не допустимой**

