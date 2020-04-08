import groovy.text.*

def numt = {[
	[ name: 'byte', nullable: false, cast:'(byte)' ],
	[ name: 'Byte', nullable: true, cast:'(byte)' ],

	[ name: 'short', nullable: false, cast:'(short)' ],
	[ name: 'Short', nullable: true, cast:'(short)' ],

	[ name: 'int', nullable: false, cast:'(int)' ],
	[ name: 'Integer', nullable: true, cast:'(int)' ],

	[ name: 'long', nullable: false, cast:'(long)' ],
	[ name: 'Long', nullable: true, cast:'(long)' ],

	[ name: 'float', nullable: false, cast:'(float)' ],
	[ name: 'Float', nullable: true, cast:'(float)' ],

	[ name: 'double', nullable: false, cast:'(double)' ],
	[ name: 'Double', nullable: true, cast:'(double)' ],
]}()

def cmpOp = { op, ltype, rtype ->
	def meth = '?'
	if( op=='==' )meth = 'eq';
	if( op=='!=' )meth = 'notEq';
	if( op=='<' )meth = 'less';
	if( op=='<=' )meth = 'eqOrLess';
	if( op=='>=' )meth = 'eqOrMore';
	if( op=='>' )meth = 'more';

	if( op=='==' ){
		return "@FnName(\"$op\") "+
			"public static Boolean $meth( $ltype.name a, $rtype.name b )"+
			"{ return "+
			"${ltype.cast}a $op ${rtype.cast}b" +
			"; }"
	}else if( op=='!=' ){
		return "@FnName(\"$op\") "+
			"public static Boolean $meth( $ltype.name a, $rtype.name b )"+
			"{ return "+
			"!eq( a, b )" +
			"; }"
	}else if( op in ['<', '<=', '>=', '>'] ){
		return "@FnName(\"$op\") "+
			"public static Boolean $meth( $ltype.name a, $rtype.name b )"+
			"{ return "+
			"a $op b" +
			"; }"
	}
}

['<','<=','>=','>'].each { op ->
	numt.findAll { it.name=='Byte' }.each { a ->
		numt.each { b -> 
			//println " "*4+cmpOp( '==', a, b )
			//println " "*4+cmpOp( '!=', a, b )
			println " "*4+cmpOp( op, a, b )
		}
	}
}