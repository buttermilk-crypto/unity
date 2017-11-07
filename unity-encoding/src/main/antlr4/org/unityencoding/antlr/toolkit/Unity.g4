grammar Unity;

json
   : array 
   ;

object
   : '{' pair (',' pair)* '}' 
   ;

pair
   : STRING ':' STRING 
   ;

array
   : '[' STRING (',' object)? (',' value)* ']'
   ;                                    

value
   : array  	#ArrayValue
   | STRING 	#StringValue 		
   | NUMBER 	#NumberValue 				
   | 'true'		#Atom			
   | 'false' 	#Atom          
   | 'null'  	#Atom          
   ;


STRING
   : '"' (ESC | ~ ["\\])* '"'
   ;


fragment ESC
   : '\\' (["\\/bfnrt] | UNICODE)
   ;


fragment UNICODE
   : 'u' HEX HEX HEX HEX
   ;


fragment HEX
   : [0-9a-fA-F]
   ;


NUMBER
   : '-'? INT '.' [0-9] + EXP? | '-'? INT EXP | '-'? INT
   ;


fragment INT
   : '0' | [1-9] [0-9]*
   ;

// no leading zeros

fragment EXP
   : [Ee] [+\-]? INT
   ;

// \- since - means "range" inside [...]

WS
   : [ \t\n\r] + -> skip
   ;