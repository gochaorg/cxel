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

expression
----------------------------
Правило expression [описано в файле JsParser](https://github.com/gochaorg/cxel/blob/cdc4b3a1d1e0b72e44ab951a422da18f59293a3a/src/main/java/xyz/cofe/cxel/js/JsParser.java#L116).

    expression ::= ifOp
    
Формально правило expression ссылаеться на правило *ifOp*, 
которое ссылается на другое, а то на третье и тем самым задеться иерархия правил


literal
---------
Литеральные конструкции - являются непосредственными значениями, 
более подробно рассмотренны [в документе про Литералы](js-literals.md) 
