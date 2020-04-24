const operators = [ 
    { name: '||' }, 
    //{ name: '&&' },
    //{ name: '==' },  { name: '!=' },
    //{ name: '===' }, { name: '!==' },
    //{ name: '|' }, { name: '&' },
]

const values = [
    { value: null }, { value: undefined },
    { value: true }, { value: false },
    { value: 1 }, { value: 0 }, { value: 0.1 }, { value: NaN }, 
    { value: Number.EPSILON }, { value: Number.MIN_VALUE }, { value: Number.MAX_VALUE }, { value: Number.NEGATIVE_INFINITY }, { value: Number.POSITIVE_INFINITY },
    { value: '' }, { value: 'xyz' },
    { value: [] }, { value: [ 1 ] },
    { value: {} }, { value: { a: 1} },
]

const valPad = 20;
const diffTypes = [];

console.log("=== left ordered =================================");
operators.forEach( operator=>{
    values.forEach( left=>{
        values.forEach( right=>{
            const lval = left.value;
            const rval = right.value;
            const op = operator.name;
            const res = eval( 'lval '+op+' rval' );

            const inputs = 
                (''+(''+lval+''+op+''+rval)+' ').padEnd(50)+
                (''+lval).padEnd(valPad)+' : '+(typeof(lval)+(typeof(lval)=='object' && lval!==null ? ' of '+lval.constructor.name : '')+'').padEnd(20)+' '+
                ''+op.padEnd(3)+
                ''+(''+rval).padEnd(valPad)+' : '+(typeof(rval)+(typeof(rval)=='object' && rval!==null ? ' of '+ rval.constructor.name : '' )+'').padEnd(20)+
                (' => '+res).padEnd(valPad)+' : '+typeof(res);

            console.log( inputs );
        })
    })
})

console.log("=== right ordered =================================");
operators.forEach( operator=>{
    values.forEach( right=>{
        values.forEach( left=>{
            const lval = left.value;
            const rval = right.value;
            const op = operator.name;
            const res = eval( 'lval '+op+' rval' );

            const inputs = 
                (''+(''+lval+''+op+''+rval)+' ').padEnd(50)+
                (''+lval).padEnd(valPad)+' : '+(typeof(lval)+(typeof(lval)=='object' && lval!==null ? ' of '+lval.constructor.name : '')+'').padEnd(20)+' '+
                ''+op.padEnd(3)+
                ''+(''+rval).padEnd(valPad)+' : '+(typeof(rval)+(typeof(rval)=='object' && rval!==null ? ' of '+ rval.constructor.name : '' )+'').padEnd(20)+
                (' => '+res).padEnd(valPad)+' : '+typeof(res);

            console.log( inputs );
        })
    })
})