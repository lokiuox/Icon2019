
%fatti

incrocio(c).
incrocio(d).
strada(a).
lunghezza(a,5).
peso(a,6).
strada(b).
lunghezza(b,5).
peso(b,5).

collega(c,a,d).
collega(d,b,c).


%clausole

direction(X,M):- strada(X), M == "V"; M == "O".


collega(C,D,E):-
	 incrocio(C),
	 strada(D),
	 incrocio(E).

lunghezza(X,Y):-
	strada(X).



angoloEntrata(X,Z):-
	 strada(X).

angoloUscita(X,Z):-
	 strada(X).



