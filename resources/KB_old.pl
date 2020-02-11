
%fatti

incrocio(c).
incrocio(d).
strada(a).
orientation(a,"V").
coordinate(a,3).
lunghezza(a,5).
peso(a,6).
strada(n).
orientation(n,"V").
coordinate(n,3).
lunghezza(n,5).
peso(n,6).
strada(b).
orientation(b,"O").
coordinate(b,3).

lunghezza(b,5).
peso(b,5).

collega(c,a,d).
collega(d,b,c).


%clausole
strada(X) :- orientation(X,Y).
strada(X) :- lunghezza(X,Y).
strada(X) :- peso(X,Y).
strada(X) :- coordinate(X,Y).

orientation(X,M):- ==(M,"V"); ==(M,"O").

collega(C,D,E):-
	 incrocio(C),
	 strada(D),
	 incrocio(E).

lunghezza(X,Y).
peso(X,Y).
coordinate(X,Y).



angoloEntrata(X,Z):-
	 strada(X).

angoloUscita(X,Z):-
	 strada(X).



