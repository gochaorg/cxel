def t = ( op, mn, tr, lt, rt ) -> 
	"@FnName(\"$op\") public static $tr.name $mn($lt.name a, $rt.name b){ return $tr.cast( a | b ); }"


def _long = [ name:'long', cast:'(long)' ]
def _Long = [ name:'Long', cast:'(long)' ]

def _int = [ name:'int', cast:'(int)' ]
def _Int = [ name:'Integer', cast:'(int)' ]

def _short = [ name:'short', cast:'(short)' ]
def _Short = [ name:'Short', cast:'(short)' ]

def _byte = [ name:'byte', cast:'(byte)' ]
def _Byte = [ name:'Byte', cast:'(byte)' ]

def rets = [ _long, _int, _short, _byte ]

println t( '|', 'or', _long, _long, _long )
println t( '|', 'or', _long, _Long, _long )