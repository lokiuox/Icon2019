
%clausole

incrocio(A).
strada(B).


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

%fatti

incrocio(c).
incrocio(d).
strada(a).
lunghezza(a,5).
strada(b).
lunghezza(b,5).

collega(c,a,d).


