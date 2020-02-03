%fatti

incrocio(c).
incrocio(d).
strada(a).
strada(b).
lunghezza(a,5).
lunghezza(b,6).
peso(b,6).

%clausole

:-dynamic(peso/2).

numerocivico(K,L):-
	strada(K),
	lunghezza(K,X) > 0,
	lunghezza(K,X) > L.

partenza(K,L):-
  	strada(K),
	numerocivico(K,L).

destinazione(K,L):-
  	strada(K),
	numerocivico(K,L).

collega(C,D,E):-
	 incrocio(C),
	 strada(D),
	 incrocio(E).

peso(X,Y):-
	 strada(X).

lunghezza(X,Y):-
	strada(X).

angolo_entrata(X,Z):-
	 strada(X).

angolo_uscita(X,Z):-
	 strada(X).


