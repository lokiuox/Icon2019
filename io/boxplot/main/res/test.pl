%fatti



incrocio(c).
incrocio(d).
strada(a).
orientation(a,"V").
coordinate(a,3).
lunghezza(a,5).
peso(a,6).
strada(b).
orientation(b,"O").
coordinate(b,3).

%clausole

:-dynamic(peso/2).

strada(X) :- orientation(X,Y).
orientation(X,M):-  M == "V"; M == "O".

coordinate(X,Y).


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

peso(X,Y).

lunghezza(X,Y).

angolo_entrata(X,Z):-
	 strada(X).

angolo_uscita(X,Z):-
	 strada(X).


