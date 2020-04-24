const operators = [ 
    { name: '||' }, { name: '&&' },
    { name: '==' },  { name: '!=' },
    { name: '===' }, { name: '!==' },
    { name: '|' }, { name: '&' }, { name: '^' },
    { name: '<' }, { name: '>' }, { name: '<=' }, { name: '>=' },
    { name: 'in' }, { name: 'instanceof' },
    { name: '<<' }, { name: '>>' }, { name: '>>>' },
    { name: '+' }, { name: '-' },
    { name: '*' }, { name: '/' }, { name: '%' },
    { name: '**' }
]

const cycleRef = []
cycleRef.push( cycleRef )

const values = [
    { value: null, name: 'null' }, 
    { value: undefined, name: 'undefined' },
    { value: true, name: 'true'  }, 
    { value: false, name: 'false'  },
    { value: 1, name: '1' }, 
    { value: 0, name: '0' }, 
    { value: 0.1, name: '0.1' }, 
    { value: NaN, name: 'NaN' }, 
    { value: Number.EPSILON   ,name:'epsilon' }, 
    { value: Number.MIN_VALUE ,name:'min number' }, 
    { value: Number.MAX_VALUE ,name:'max number' }, 
    { value: Number.NEGATIVE_INFINITY ,name:'-inf number' }, 
    { value: Number.POSITIVE_INFINITY ,name:'+inf number' },
    { value: '', name: 'empty string' }, 
    { value: 'xyz' },
    { value: [], name: 'empty arr' }, 
    { value: [ 1 ], name: '[ 1 ]' },
    { value: [ null, undefined ], name: '[ null, undefined ]' },
    { value: cycleRef, name: '[ cycle ref ]', cycle:true },
    { value: {}, name: '{}' }, 
    { value: { a: 1 }, name: '{a:1}' },
]

const valPad = 20;
const diffTypes = [];

const summary = [];

operators.forEach( operator=>{
    values.forEach( left=>{
        values.forEach( right=>{
            const lval = left.value;
            const rval = right.value;
            const op = operator.name;
            let cerr = null;
            let res = null;
            let code = 'lval '+op+' rval';
            try {
                res = eval( code );
            } catch( err ){
                cerr = err;
            }

            const ent = {
                operator: op,
                left: {
                    value: left.cycle ? ''+lval : lval,
                    cycle: left.cycle ? "true" : "false",
                    name: left.name || left.value,
                    type: {
                        of: (typeof lval),
                        obj: (typeof(lval)=='object' && lval!==null ? lval.constructor.name : null)
                    }
                },
                right: {
                    value: right.cycle ? ''+rval : rval,
                    cycle: right.cycle ? "true" : "false",
                    name: right.name || right.value,
                    type: {
                        of: (typeof rval),
                        obj: (typeof(rval)=='object' && rval!==null ? rval.constructor.name : null)
                    }
                },
                result: {
                    value: ''+res,
                    type: {
                        of:  (typeof res),
                        obj: (typeof(res)=='object' && res!==null ? res.constructor.name : null)
                    },
                    same: ( res===lval ? 'left' : (res==rval ? 'right' : 'none') )
                },
                error: cerr
            };

            summary.push( ent );
            //console.log( JSON.stringify(ent) );
        })
    })
});

console.log( JSON.stringify(summary, null, 2) );
