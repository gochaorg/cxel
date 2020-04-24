const operators = [ 
    { name: '!' }, 
    { name: '-' },
    { name: '+' },
    { name: '~' },
    { name: 'typeof' },
    { name: 'delete' },
    { name: 'void' },
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

const valPad = 25;
const diffTypes = [];

console.log("=== unary =================================");
operators.forEach( operator=>{
    values.forEach( left=>{
        const lval = left.value;
        const op = operator.name;
        const res = eval( op+' lval' );

        const inputs = 
            (op+' '+lval).padEnd(30)+
            ''+op.padEnd(3)+
            (''+lval).padEnd(valPad)+' : '+(typeof(lval)+(typeof(lval)=='object' && lval!==null ? ' of '+lval.constructor.name : '')+'').padEnd(20)+' '+
            (' => '+res).padEnd(valPad)+' : '+typeof(res);

        console.log( inputs );
    })
})
