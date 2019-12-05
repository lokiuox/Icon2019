leftMove(Cell(ant)) :- isFree(leftCell(Cell(ant))).
rightMove(Cell(ant)) :- isFree(rightCell(Cell(ant))).
