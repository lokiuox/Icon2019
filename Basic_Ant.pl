interface ant
predicates
	canLeftMove:() ->(boolean Z).	
	canRightMove:() ->(boolean Z).
	canUpMove:() ->(boolean Z).
	canDownMove:() ->(boolean Z).
	getCell:()-> (integer X,integer Y).
end interface ant

class ant:ant
constructors
	new:(integer Id):-
		Id=0.
	/*new :(integer Id,integer X,integer Y):-
		Id=random(),
		Position=(randomX(),randomY()).*/
end class

implement ant
facts
	Id:integer.
	Position:(integer X,integer Y).
/*
clauses
	canLeftMove() :- isFree(leftCell(Cell(ant))).
clauses
	rightMove(Cell(ant)) :- isFree(rightCell(Cell(ant))).
clauses
	upMove(Cell(ant)) :- isFree(upCell(Cell(ant))).
clauses
	downMove(Cell(ant)) :- isFree(downCell(Cell(ant))).
clauses
	Cell(ant,cell):- cell is Position 
*/
end implement ant
