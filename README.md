# SberseasonsJavaTestTask

Test Java task for getting an internship in Sberbank Technologies (Bryansk, Russia, 2020)

Task
-------------------------
You need to make a parser.
Condition: there is a text format for storring arbitrary data that has a hierarchical structure
Syntax is:
```<node_name> - a string of letters, digits and the character '_'; not starting with the digit
<quoted_string> - double quoted arbitrary string that does not contain the characters of line break or double quotes.
<node> ::= <node_name> '=' (<quoted_string> | <list>)
<list> = '{' <node> [node...] '}'

Example:
--------------------------
shape = {
type = "tetrahedron"
vertices = {
point = { x = “1” y = “0” z = “0” }
point = { x = “0” y = “1” z = “0” }
point = { x = “0” y = “0” z = “1” }
point = { x = “1” y = “1” z = “1” }
}
```
You need to make a parser that accepts a file with one root node, and build tree data structure in the memory. Every single node has to have an integer id. Then you need to write this structure to a file with the following structure: (node_id, parent_node_id, node_name, node_value)

If there are any errors in data format, programm has to display message like "Invalid data format" and exit.
