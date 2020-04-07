import groovy.text.*

def numt = {[
	[ name: 'byte', nullable: false, cast:'byte' ],
	[ name: 'Byte', nullable: true, cast:'byte' ],

	[ name: 'short', nullable: false, cast:'short' ],
	[ name: 'Short', nullable: true, cast:'short' ],

	[ name: 'int', nullable: false, cast:'int' ],
	[ name: 'Integer', nullable: true, cast:'int' ],

	[ name: 'long', nullable: false, cast:'long' ],
	[ name: 'Long', nullable: true, cast:'long' ],

	[ name: 'float', nullable: false, cast:'float' ],
	[ name: 'Float', nullable: true, cast:'float' ],

	[ name: 'double', nullable: false, cast:'double' ],
	[ name: 'Double', nullable: true, cast:'double' ],
]}()

def mop = { op, ret, ltype, rtype, cast ->
	def m = '';
	if( op=='-' )m = 'sub';
	if( op=='+' )m = 'add';
	if( op=='*' )m = 'mul';
	if( op=='/' )m = 'div';
	
	"@FnName(\"$op\") "+
	"public static $ret $m( $ltype.name a, $rtype.name b )"+
	"{ return $cast(a $op b); }"
}

def ord = [ 'byte','Byte',
	'short','Short',
	'int','Integer','long','Long','float','Float','double','Double' ]
	
def top = { lt, rt ->
	def li = ord.indexOf( lt )
	def ri = ord.indexOf( rt )
	if( li==ri )return lt
	if( li>ri )return lt
	return rt
}

[ ['byte','Byte'], 
  ['short','Short'],
  ['int','Integer'],
  ['long','Long'],
  ['float','Float'],
  ['double','Double'],
].each { lst ->
	[ '+', '-', '*', '/' ].each { op ->
		numt.findAll { it.name in ['byte','Byte'] }.each { lt ->
			numt.each { rt ->
				def t = top(lt.name, rt.name)
				def ct = t
				if( ct=='Byte' ){
					ct = '(Byte)(byte)'
				}else if( ct=='Short' ){
					ct = '(Short)(short)'
				}else {
					ct = "($ct)"
				}
				println " "*4+mop( op, t, lt, rt, ct )
			}
		}
	}
}